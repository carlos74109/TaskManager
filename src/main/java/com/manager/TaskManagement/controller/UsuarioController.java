package com.manager.TaskManagement.controller;

import com.manager.TaskManagement.Enuns.PapelUsuario;

import com.manager.TaskManagement.dto.DtoEditar.EditarUsuarioDto;
import com.manager.TaskManagement.dto.UsuarioConsultaDTO;
import com.manager.TaskManagement.dto.UsuarioDTO;

import com.manager.TaskManagement.models.Roles;
import com.manager.TaskManagement.models.Usuario;
import com.manager.TaskManagement.repository.ProjetoRepository;
import com.manager.TaskManagement.repository.RolesRepository;
import com.manager.TaskManagement.repository.TarefasRepository;
import com.manager.TaskManagement.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    ProjetoRepository projetoRepository;

    @Autowired
    TarefasRepository tarefasRepository;

    @Autowired
    RolesRepository rolesRepository;

    BCryptPasswordEncoder criptografia (){
        return new BCryptPasswordEncoder();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR', 'ROLE_COMUM')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @Transactional
    @PostMapping("/criar")//criar usuario
    public ResponseEntity<Usuario> criarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        Usuario usuario = new Usuario();
        Usuario emailExiste = usuarioRepository.findByEmail(usuarioDTO.email());

        if(emailExiste != null){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Roles roles = rolesRepository.findById(4l).get();
            usuario.atualizarDto(usuarioDTO);

            usuario.setSenha(criptografia().encode(usuario.getSenha()));
            usuario.getRoles().add(roles);

            usuarioRepository.save(usuario);

            return ResponseEntity.ok().build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @GetMapping("/listas/{status}")// pesquisar usuarios de acordo com os status do usuario
    public ResponseEntity listasDeMembros(@PathVariable String status){
        List<UsuarioConsultaDTO> membros = null;

        switch (status){
            case "admin", "gestor", "comum":
                membros = usuarioRepository.findByPapelUsuario(PapelUsuario.valueOf(status.toUpperCase()))
                        .stream().map(UsuarioConsultaDTO::new)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(membros);

        }
        if(status.equals("todos")){
            membros = usuarioRepository.findAll().stream().map(UsuarioConsultaDTO::new).collect(Collectors.toList());
            return ResponseEntity.ok(membros);
        }
        return null;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @Transactional
    @PostMapping("/editar/{idUsuario}")//editar Usuario
    public ResponseEntity editarUsuario(@PathVariable Long idUsuario, @RequestBody EditarUsuarioDto editarUsuarioDto){

        if(usuarioRepository.findById(idUsuario).get() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Usuario usuario = usuarioRepository.findById(idUsuario).get();
            usuario.converterEditarUsuarioDto(editarUsuarioDto);
            usuarioRepository.save(usuario);
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
    @PostMapping("/delete/{idUsuario}")
    public ResponseEntity deletarUsuario(@PathVariable Long idUsuario){// deletar usuario


        if(usuarioRepository.findById(idUsuario).get() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Usuario usuario = usuarioRepository.findById(idUsuario).get();
            usuarioRepository.delete(usuario);
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
    @PostMapping("/teste/{idUsuario}/{idRoles}")//Editar função do usuario
    public ResponseEntity editarPapelUsuario(@PathVariable Long idUsuario, @PathVariable Long idRoles){

        if(usuarioRepository.findById(idUsuario).get() == null || rolesRepository.findById(idRoles).get() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Usuario usuario = usuarioRepository.findById(idUsuario).get();
            usuario.getRoles().clear();
            usuarioRepository.save(usuario);

            Roles roles = rolesRepository.findById(idRoles).get();
            usuario.getRoles().add(roles);

            usuarioRepository.save(usuario);
            return new ResponseEntity(HttpStatus.OK);
        }
    }
    @PreAuthorize("hasRole('ROLE_MASTER')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @PostMapping("/master/{idUsuario}")//
    public ResponseEntity alocarUsuarioAdminMaster(@PathVariable Long idUsuario){
        Roles role = rolesRepository.findById(1l).get();
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        usuario.getRoles().clear();
        usuario.getRoles().add(role);

        usuarioRepository.save(usuario);
        return new ResponseEntity(HttpStatus.OK);
    }

}
