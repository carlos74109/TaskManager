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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.catalina.connector.Response;
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
import org.springframework.web.util.UriComponentsBuilder;
import javax.websocket.server.PathParam;
import java.net.URI;
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
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @PostMapping("/criar/{idProjeto}")//criar tarefas
    public ResponseEntity<Tarefas> criarTarefas(@RequestBody TarefasDTO tarefasDTO, @PathVariable Long idProjeto){
        try {
            if(projetoRepository.findById(idProjeto).isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }else {
                Projeto projeto = projetoRepository.findById(idProjeto).get();

                Tarefas tarefa = new Tarefas();
                tarefa.criarTarefaDto(tarefasDTO);
                tarefa.setProjeto_id(projeto);
                tarefasRepository.save(tarefa);

                return ResponseEntity.ok().build();
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @GetMapping("/listas/projetos/{idProjeto}")//trazer todas as tarefas de um determinado Projeto
    public ResponseEntity listasTarefas(@PageableDefault(size = 10) Pageable paginacao, @PathVariable Long idProjeto){
        if(projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        Page<Tarefas> listasTarefa = tarefasRepository.findByProjeto(idProjeto, paginacao);
        return ResponseEntity.ok(listasTarefa);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR', 'ROLE_COMUM')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema intero")
    @GetMapping("/listas/usuario/{idUsuario}")//encontrar todas as tarefas de um determinado Usuario
    public ResponseEntity listasDeTarefaUsurario(@PageableDefault(size = 10) Pageable paginacao, @PathVariable Long idUsuario){

        return ResponseEntity.ok(tarefasRepository.findByTarefaUsuario(idUsuario, paginacao));
    }

    @PreAuthorize("hasRole('ROLE_GESTOR')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
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
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
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
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
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
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @GetMapping("/listas/{idProjeto}/{status}")// pesquisar Tarefas de acordo com os status
    public ResponseEntity listasDeStatusTarefa(@PathVariable String status, @PathVariable Long idProjeto , @PageableDefault(size = 10) Pageable paginacao){

        if(status.equals("fazer")){
            return ResponseEntity.ok(tarefasRepository.findByStatusTarefaFazer(StatusTarefa.valueOf(status.toUpperCase()), idProjeto, paginacao));
        }
        if(status.equals("fazendo")){
            return  ResponseEntity.ok(tarefasRepository.findByStatusTarefaFazendo(StatusTarefa.valueOf(status.toUpperCase()), idProjeto, paginacao));
        }
        if(status.equals("feito")){
            return  ResponseEntity.ok(tarefasRepository.findByStatusTarefaFeito(StatusTarefa.valueOf(status.toUpperCase()), idProjeto, paginacao));
        }
        if(status.equals("todos")){
            return ResponseEntity.ok(tarefasRepository.findAll(paginacao));
        }
        return null;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @GetMapping("/listas/{idProjeto}/nulo")// pesquisar Tarefas de acordo com os status FAZER onde o ususario é Null
    public ResponseEntity listasDeStatusTarefaUsuarioNull(@PathVariable Long idProjeto, @PageableDefault(size = 10) Pageable paginacao){
        return ResponseEntity.ok(tarefasRepository.findByStatusTarefaNull(StatusTarefa.FAZER,idProjeto, paginacao));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GESTOR')")
    @ApiResponse(responseCode = "200", description = "Sucesso")
    @ApiResponse(responseCode = "400", description = "Má requisição")
    @ApiResponse(responseCode = "401", description = "Não autorizado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "404", description = "Pagina não encontrado")
    @ApiResponse(responseCode = "500", description = "Problema interno")
    @GetMapping("/listas/projetos/null/{idProjeto}")//trazer todas as tarefas de um determinado Projeto onde o usuario é null
    public ResponseEntity listasTarefasUsuarioNull(@PageableDefault(size = 10) Pageable paginacao, @PathVariable Long idProjeto){
        if(projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        Page<Tarefas> listasTarefa = tarefasRepository.findByProjetoComUsuarioNull(idProjeto, paginacao);
        return ResponseEntity.ok(listasTarefa);
    }
}
