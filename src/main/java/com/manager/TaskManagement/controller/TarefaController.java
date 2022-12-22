package com.manager.TaskManagement.controller;

import com.manager.TaskManagement.dto.DtoEditar.EditarTarefaDto;
import com.manager.TaskManagement.dto.TarefasDTO;
import com.manager.TaskManagement.models.Projeto;
import com.manager.TaskManagement.models.Tarefas;
import com.manager.TaskManagement.models.Usuario;
import com.manager.TaskManagement.repository.ProjetoRepository;
import com.manager.TaskManagement.repository.TarefasRepository;
import com.manager.TaskManagement.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {
    @Autowired
    TarefasRepository tarefasRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ProjetoRepository projetoRepository;
    @PostMapping("/criar/{idProjeto}")//criar tarefas
    public void criarTarefas(@RequestBody TarefasDTO tarefasDTO, @PathVariable Long idProjeto){

        if(projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        Projeto projeto = projetoRepository.findById(idProjeto).get();

        Tarefas tarefa = new Tarefas();
        tarefa.criarTarefaDto(tarefasDTO);
        tarefa.setProjeto_id(projeto);
        tarefasRepository.save(tarefa);

    }
    @GetMapping("/listas/projetos/{idProjeto}")//trazer todas as tarefas de um determinado Projeto
    public Page<Tarefas> listasTarefas(@PageableDefault(size = 10) Pageable paginacao, @PathVariable Long idProjeto){
        if(projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        Page<Tarefas> listasTarefa = tarefasRepository.findByProjeto(idProjeto, paginacao);
        return listasTarefa;
    }

    @GetMapping("/listas/projetos/null/{idProjeto}")//trazer todas as tarefas de um determinado Projeto onde o usuario é null
    public Page<Tarefas> listasTarefasUsuarioNull(@PageableDefault(size = 10) Pageable paginacao, @PathVariable Long idProjeto){
        if(projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        Page<Tarefas> listasTarefa = tarefasRepository.findByProjetoComUsuarioNull(idProjeto, paginacao);
        return listasTarefa;
    }

    @GetMapping("/listas/usuario/{idUsuario}")//encontrar todas as tarefas de um determinado Usuario
    public Page<Tarefas> listasDeTarefaUsurario(@PageableDefault(size = 10) Pageable paginacao, @PathVariable Long idUsuario){

        return tarefasRepository.findByTarefaUsuario(idUsuario, paginacao);
    }

    @Transactional
    @PostMapping("/usuario/{idUsuario}/{idTarefa}")//encontrar o usuario e adicionar a tarefa
    public void addTarefaParaUsuario(@PathVariable Long idUsuario, @PathVariable Long idTarefa){
        Usuario usuario = usuarioRepository.findById(idUsuario).get();
        Tarefas tarefas = tarefasRepository.findById(idTarefa).get();

        usuario.getListasTarefas().add(tarefas);
        usuarioRepository.save(usuario);
    }

    @Transactional
    @PostMapping("/editar/{idTarefas}")// editar os atributos das tarefas
    public void editarTarefa(@PathVariable Long idTarefas, @RequestBody EditarTarefaDto editarTarefaDto){
        Tarefas tarefas = tarefasRepository.findById(idTarefas).get();
        tarefas.converterEditarTarefaDto(editarTarefaDto);
        tarefasRepository.save(tarefas);
    }

    @PostMapping("/deletar/{id}")//deleta tarefa
    public void deletaTarefa (@PathVariable Long idTarefa){
        Tarefas tarefa = tarefasRepository.findById(idTarefa).get();
        tarefasRepository.delete(tarefa);
    }

}
