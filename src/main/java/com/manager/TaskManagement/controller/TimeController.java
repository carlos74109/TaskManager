package com.manager.TaskManagement.controller;

import com.manager.TaskManagement.dto.ConsultaListaObjetosDTo.consultaUsuariosEmTime;
import com.manager.TaskManagement.dto.UsuarioDTO;
import com.manager.TaskManagement.dto.TimeDTO;
import com.manager.TaskManagement.dto.consultaTimeAndProjetoDto;
import com.manager.TaskManagement.models.Usuario;
import com.manager.TaskManagement.models.Time;
import com.manager.TaskManagement.repository.UsuarioRepository;
import com.manager.TaskManagement.repository.TimeRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/time")
public class TimeController {

    @Autowired
    TimeRepository timeRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @PostMapping("/criar")// criar o time
    public ResponseEntity criarTime(@RequestBody TimeDTO timeDTO){
        Time time = new Time();
        time.salvarTime(timeDTO);
        timeRepository.save(time);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @Transactional
    @PostMapping("/alocar/{idUsuario}/{idTime}")//alocar usuario no time
    public ResponseEntity salvarUsuarioETime(@PathVariable Long idUsuario, @PathVariable Long idTime){

        if(usuarioRepository.findById(idUsuario).get() == null || timeRepository.findById(idTime).get() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Usuario usuario = usuarioRepository.findById(idUsuario).get();
            Time time = timeRepository.findById(idTime).get();

            time.getTimesUsuarios().add(usuario);

            timeRepository.save(time);
            return new ResponseEntity(HttpStatus.OK);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @Transactional
    @PostMapping("/remover/usuario/time/{idUsuario}/{idTime}")//removendo o usuario daquele time
    public ResponseEntity removerUsuarioDoTime(@PathVariable Long idUsuario, @PathVariable Long idTime){

       if(usuarioRepository.findById(idUsuario).get() == null || timeRepository.findById(idTime).get() == null){
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }else {
           Usuario usuario = usuarioRepository.findById(idUsuario).get();
           Time time = timeRepository.findById(idTime).get();
           time.getTimesUsuarios().remove(usuario);
           timeRepository.save(time);
           return new ResponseEntity(HttpStatus.OK);
       }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @GetMapping("/lista/usuario/{idTime}")//Listar Usuarios de um determinado Time
    public ResponseEntity listasUsuario (@PathVariable Long idTime){
        Time time = timeRepository.findById(idTime).get();

        return ResponseEntity.ok(time.getTimesUsuarios().stream().map(UsuarioDTO::new).collect(Collectors.toList()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @Transactional
    @PostMapping("/deleta/{idTime}")//remover o time
    public ResponseEntity deletaTime(@PathVariable Long idTime){
        Time time = timeRepository.findById(idTime).get();

        if(timeRepository.findById(idTime).get() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            if(time.getTimesUsuarios().isEmpty() && time.getTimeListProjetos().isEmpty()){
                timeRepository.delete(time);
                return new ResponseEntity(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }

}
