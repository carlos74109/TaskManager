package com.manager.TaskManagement.controller;

import com.manager.TaskManagement.dto.ConsultaListaObjetosDTo.ConsultaProjetoTarefaTime;
import com.manager.TaskManagement.dto.DtoEditar.EditarProjetoDto;
import com.manager.TaskManagement.dto.ProjetoDTO;
import com.manager.TaskManagement.dto.TarefasDTO;
import com.manager.TaskManagement.models.Usuario;
import com.manager.TaskManagement.models.Projeto;
import com.manager.TaskManagement.models.Tarefas;
import com.manager.TaskManagement.models.Time;
import com.manager.TaskManagement.repository.UsuarioRepository;
import com.manager.TaskManagement.repository.ProjetoRepository;
import com.manager.TaskManagement.repository.TarefasRepository;
import com.manager.TaskManagement.repository.TimeRepository;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/projeto")
public class ProjetoController {
    @Autowired
    ProjetoRepository projetoRepository;

    @Autowired
    TarefasRepository tarefasRepository;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    TimeRepository timeRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @PostMapping("/criar")// criar o projeto
    public ResponseEntity criarPjeto(@RequestBody ProjetoDTO projetoDTO){
        Time time = timeRepository.findById(0l).get();
        Projeto projeto = new Projeto();
        projeto.salvarProjetoDto(projetoDTO);
        System.out.println(projetoDTO.timeProjeto());
        if(projetoDTO.timeProjeto() == null){
            projeto.setTimeProjeto(time);
        }
        projetoRepository.save(projeto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/listas")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    public ResponseEntity listasProjeto(@PageableDefault(size = 10) Pageable paginacao) {
        return ResponseEntity.ok(projetoRepository.findAll(paginacao).stream().map(ConsultaProjetoTarefaTime::new).collect(Collectors.toList()));
    }

    @GetMapping("/listas/a")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    public ResponseEntity listasProjetos() {
        return ResponseEntity.ok(projetoRepository.findAll());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @Transactional
    @PostMapping("/alocar/time/{idTime}/{idProjeto}")//alocar time ao projeto
    public ResponseEntity alocarTime(@PathVariable Long idTime, @PathVariable Long idProjeto){

       if(timeRepository.findById(idTime).get() == null || projetoRepository.findById(idProjeto).get() == null){
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }else {
           Time time = timeRepository.findById(idTime).get();
           Projeto projeto = projetoRepository.findById(idProjeto).get();
           projeto.setTimeProjeto(time);
           projetoRepository.save(projeto);
           return new ResponseEntity(HttpStatus.OK);
       }
    }

    @PreAuthorize("hasRole('ROLE_GESTOR')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @Transactional
    @PostMapping("/tarefa/adicionar/{idProjeto}/{idTarefa}/{idMembro}")//adicionar tarefa ao usuario
    public ResponseEntity editarProjeto (@PathVariable Long idProjeto, @PathVariable Long idTarefa, @PathVariable Long idMembro){

        if(projetoRepository.findById(idProjeto).get() == null || tarefasRepository.findById(idTarefa).get() == null || usuarioRepository.findById(idProjeto).get() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            Projeto projeto = projetoRepository.findById(idProjeto).get();
            Tarefas tarefa = tarefasRepository.findById(idTarefa).get();
            Usuario usuario = usuarioRepository.findById(idMembro).get();

            tarefa.setUsuarioTimeTarefa(usuario);
            projeto.getTarefasProjeto().add(tarefa);
            projetoRepository.save(projeto);
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
    @Transactional
    @PostMapping("/editar/{idProjeto}")//Editar atributos de projeto
    public ResponseEntity editarProjeto(@PathVariable Long idProjeto, @RequestBody EditarProjetoDto editarProjetoDto){

        if(projetoRepository.findById(idProjeto) == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Projeto projeto = projetoRepository.findById(idProjeto).get();
            projeto.converterEditarDto(editarProjetoDto);
            projetoRepository.save(projeto);
            return new ResponseEntity(HttpStatus.OK);
        }

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema intero")
    @PostMapping("/deleta/{idProjeto}")//Deleta Projeto
    public ResponseEntity removerProjeto(@PathVariable Long idProjeto){

        if (projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        if(projetoRepository.findById(idProjeto).get() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            Projeto projeto = projetoRepository.findById(idProjeto).get();
            if(projeto.getTarefasProjeto().isEmpty() && projeto.getTimeProjeto() == null || projeto.getTimeProjeto().getIdTime() == 0){
                projetoRepository.delete(projeto);
                return new ResponseEntity(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

}
