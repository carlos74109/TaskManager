package com.manager.TaskManagement.controller;

import com.manager.TaskManagement.Enuns.PapelUsuario;

import com.manager.TaskManagement.dto.DtoEditar.EditarUsuarioDto;
import com.manager.TaskManagement.dto.UsuarioConsultaDTO;
import com.manager.TaskManagement.dto.UsuarioDTO;

import com.manager.TaskManagement.models.Usuario;
import com.manager.TaskManagement.repository.ProjetoRepository;
import com.manager.TaskManagement.repository.TarefasRepository;
import com.manager.TaskManagement.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
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

    BCryptPasswordEncoder criptografia (){
        return new BCryptPasswordEncoder();
    }

    @PostMapping("/criar")//criar usuario
    public void criarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        Usuario usuario = new Usuario();
        Usuario emailExiste = usuarioRepository.findByEmail(usuarioDTO.email());

        if(emailExiste != null){
           throw new Error("Email já existe");
        }

        usuario.atualizarDto(usuarioDTO);
        usuario.setSenha(criptografia().encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
    }
    @GetMapping("/listas/{status}")// pesquisar usuarios de acordo com os status do usuario
    public List<UsuarioConsultaDTO> listasDeMembros(@PathVariable String status){
        List<UsuarioConsultaDTO> membros = null;

        switch (status){
            case "admin", "gestor", "comum":
                membros = usuarioRepository.findByPapelUsuario(PapelUsuario.valueOf(status.toUpperCase()))
                        .stream().map(UsuarioConsultaDTO::new)
                        .collect(Collectors.toList());
                return membros;

        }
        if(status.equals("todos")){
            membros = usuarioRepository.findAll().stream().map(UsuarioConsultaDTO::new).collect(Collectors.toList());
            return membros;
        }
        return null;
    }

    @Transactional
    @PostMapping("/editar/{idUsuario}")
    public void editarUsuario(@PathVariable Long idUsuario, @RequestBody EditarUsuarioDto editarUsuarioDto){

        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        usuario.converterEditarUsuarioDto(editarUsuarioDto);
        usuarioRepository.save(usuario);


    }
    @Transactional
    @PostMapping("/delete/{idUsuario}")
    public void deletarUsuario(@PathVariable Long idUsuario){
        Usuario usuario = usuarioRepository.findById(idUsuario).get();

        if(usuario.getListasTarefas().isEmpty() && usuario.getUsuarioTimes().isEmpty()){
            usuarioRepository.delete(usuario);
        }else {
            throw new Error("Usuario pertence a algum time ou tarefa");
        }
    }



//    @GetMapping("/home/{idUsuario}")
//    public List<Object> usuario(@PathVariable Long idUsuario){
//
//    }



}
