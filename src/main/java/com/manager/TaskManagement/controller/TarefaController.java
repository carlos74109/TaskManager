package com.manager.TaskManagement.controller;

import com.manager.TaskManagement.Enuns.PapelUsuario;
import com.manager.TaskManagement.Enuns.StatusTarefa;
import com.manager.TaskManagement.dto.ConsultaListaObjetosDTo.TarefasConsultaDto;
import com.manager.TaskManagement.dto.DtoEditar.EditarTarefaDto;
import com.manager.TaskManagement.dto.TarefasDTO;
import com.manager.TaskManagement.dto.UsuarioConsultaDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tarefa")
public class TarefaController {
    @Autowired
    TarefasRepository tarefasRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    ProjetoRepository projetoRepository;

    @PreAuthorize("hasRole('ROLE_GESTOR')")
    @PostMapping("/criar/{idProjeto}")//criar tarefas
    public ResponseEntity criarTarefas(@RequestBody TarefasDTO tarefasDTO, @PathVariable Long idProjeto){
        try {
            if(projetoRepository.findById(idProjeto).isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }else {
                Projeto projeto = projetoRepository.findById(idProjeto).get();

                Tarefas tarefa = new Tarefas();
                tarefa.criarTarefaDto(tarefasDTO);
                tarefa.setProjeto_id(projeto);
                tarefasRepository.save(tarefa);
                return new ResponseEntity(HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR')")
    @GetMapping("/listas/projetos/{idProjeto}")//trazer todas as tarefas de um determinado Projeto
    public Page<Tarefas> listasTarefas(@PageableDefault(size = 10) Pageable paginacao, @PathVariable Long idProjeto){
        if(projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        Page<Tarefas> listasTarefa = tarefasRepository.findByProjeto(idProjeto, paginacao);
        return listasTarefa;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR', 'ROLE_COMUM')")
    @GetMapping("/listas/usuario/{idUsuario}")//encontrar todas as tarefas de um determinado Usuario
    public Page<Tarefas> listasDeTarefaUsurario(@PageableDefault(size = 10) Pageable paginacao, @PathVariable Long idUsuario){

        return tarefasRepository.findByTarefaUsuario(idUsuario, paginacao);
    }

    @PreAuthorize("hasRole('ROLE_GESTOR')")
    @Transactional
    @PostMapping("/usuario/{idUsuario}/{idTarefa}")//encontrar o usuario e adicionar a tarefa
    public ResponseEntity addTarefaParaUsuario(@PathVariable Long idUsuario, @PathVariable Long idTarefa){

        if(usuarioRepository.findById(idUsuario).get() == null || tarefasRepository.findById(idTarefa).get()== null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Usuario usuario = usuarioRepository.findById(idUsuario).get();
            Tarefas tarefas = tarefasRepository.findById(idTarefa).get();

            usuario.getListasTarefas().add(tarefas);
            usuarioRepository.save(usuario);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR', 'ROLE_COMUM')")
    @Transactional
    @PostMapping("/editar/{idTarefas}")// editar os atributos das tarefas
    public ResponseEntity editarTarefa(@PathVariable Long idTarefas, @RequestBody EditarTarefaDto editarTarefaDto) throws ParseException {
        try {
                Tarefas tarefas = tarefasRepository.findById(idTarefas).get();

                if(tarefas.getUsuarioTimeTarefa() != null){
                    if (editarTarefaDto.statusTarefa().toString().equals("FAZENDO")){
                        tarefas.setDataAtualizacao(new Date());
                    }
                    if (editarTarefaDto.statusTarefa().toString().equals("FEITO")){
                        tarefas.setDataFinalizacao(new Date());
                    }
                    tarefas.converterEditarTarefaDto(editarTarefaDto);
                    tarefasRepository.save(tarefas);
                    return new ResponseEntity(HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR')")
    @PostMapping("/deletar/{id}")//deleta tarefa
    public ResponseEntity deletaTarefa (@PathVariable Long idTarefa){

        if(tarefasRepository.findById(idTarefa).get() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Tarefas tarefa = tarefasRepository.findById(idTarefa).get();
            tarefasRepository.delete(tarefa);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR')")
    @GetMapping("/listas/{idProjeto}/{status}")// pesquisar Tarefas de acordo com os status
    public Page<Tarefas> listasDeStatusTarefa(@PathVariable String status, @PathVariable Long idProjeto , @PageableDefault(size = 10) Pageable paginacao){

        if(status.equals("fazer")){
            return  tarefasRepository.findByStatusTarefaFazer(StatusTarefa.valueOf(status.toUpperCase()), idProjeto, paginacao);
        }
        if(status.equals("fazendo")){
            return  tarefasRepository.findByStatusTarefaFazendo(StatusTarefa.valueOf(status.toUpperCase()), idProjeto, paginacao);
        }
        if(status.equals("feito")){
            return  tarefasRepository.findByStatusTarefaFeito(StatusTarefa.valueOf(status.toUpperCase()), idProjeto, paginacao);
        }
        if(status.equals("todos")){
            return tarefasRepository.findAll(paginacao);
        }
        return null;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR')")
    @GetMapping("/listas/{idProjeto}/nulo")// pesquisar Tarefas de acordo com os status FAZER onde o ususario é Null
    public Page<Tarefas> listasDeStatusTarefaUsuarioNull(@PathVariable Long idProjeto, @PageableDefault(size = 10) Pageable paginacao){
        return  tarefasRepository.findByStatusTarefaNull(StatusTarefa.FAZER,idProjeto, paginacao);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR')")
    @GetMapping("/listas/projetos/null/{idProjeto}")//trazer todas as tarefas de um determinado Projeto onde o usuario é null
    public Page<Tarefas> listasTarefasUsuarioNull(@PageableDefault(size = 10) Pageable paginacao, @PathVariable Long idProjeto){
        if(projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        Page<Tarefas> listasTarefa = tarefasRepository.findByProjetoComUsuarioNull(idProjeto, paginacao);
        return listasTarefa;
    }
}
