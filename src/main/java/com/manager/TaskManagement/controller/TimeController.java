package com.manager.TaskManagement.controller;

import com.manager.TaskManagement.dto.ConsultaListaObjetosDTo.consultaUsuariosEmTime;
import com.manager.TaskManagement.dto.UsuarioDTO;
import com.manager.TaskManagement.dto.TimeDTO;
import com.manager.TaskManagement.dto.consultaTimeAndProjetoDto;
import com.manager.TaskManagement.models.Usuario;
import com.manager.TaskManagement.models.Time;
import com.manager.TaskManagement.repository.UsuarioRepository;
import com.manager.TaskManagement.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    @PostMapping("/criar")// criar o time
    public void criarTime(@RequestBody TimeDTO timeDTO){
        Time time = new Time();
        time.salvarTime(timeDTO);
        timeRepository.save(time);

    }
    @GetMapping("/lista/membros")//procurar todos os membros de todos os times
    public Page<TimeDTO> membrosDisponiveis(@PageableDefault(size=10) Pageable paginacao){
        return timeRepository.findAll(paginacao).map(TimeDTO::new);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @PostMapping("/alocar/{idUsuario}/{idTime}")//alocar usuario no time
    public void salvarUsuarioETime(@PathVariable Long idUsuario, @PathVariable Long idTime){
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        Time time = timeRepository.findById(idTime).get();

        time.getTimesUsuarios().add(usuario);

        timeRepository.save(time);

    }
    @PreAuthorize("hasRole('ROLE_ADMIN)")
    @Transactional
    @PostMapping("/remover/usuario/time/{idUsuario}/{idTime}")//removendo o usuario daquele time
    public void removerUsuarioDoTime(@PathVariable Long idUsuario, @PathVariable Long idTime){
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        Time time = timeRepository.findById(idTime).get();

        if(usuario.getListasTarefas().isEmpty()){
            time.getTimesUsuarios().remove(usuario);
            timeRepository.save(time);
        }else {
            System.out.println("Retire todas as atividades do usuario antes de remove-lo");
        }


    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR')")
    @GetMapping("/lista/usuario/{idTime}")//Listar Usuarios de um determinado Time
    public List<UsuarioDTO> listasUsuario (@PathVariable Long idTime){
        Time time = timeRepository.findById(idTime).get();

        return time.getTimesUsuarios().stream().map(UsuarioDTO::new).collect(Collectors.toList());

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @PostMapping("/deleta/{idTime}")//remover o time
    public void deletaTime(@PathVariable Long idTime){
        Time time = timeRepository.findById(idTime).get();

        if(time.getTimesUsuarios().isEmpty() && time.getTimeListProjetos().isEmpty()){
            timeRepository.delete(time);
        }else {
            System.out.println("Retire o time do projeto e remova todos os usuarios");
        }

    }

    @GetMapping("/listas")
    public List<consultaTimeAndProjetoDto> listasTime(){
        return timeRepository.findTimeUsuario().stream().map(consultaTimeAndProjetoDto::new).collect(Collectors.toList());
    }
}
