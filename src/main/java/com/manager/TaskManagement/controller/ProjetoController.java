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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/criar")// criar o projeto
    public void criarPjeto(@RequestBody ProjetoDTO projetoDTO){
        Time time = timeRepository.findById(0l).get();
        Projeto projeto = new Projeto();
        projeto.salvarProjetoDto(projetoDTO);
        System.out.println(projetoDTO.timeProjeto());
        if(projetoDTO.timeProjeto() == null){
            projeto.setTimeProjeto(time);
        }
        projetoRepository.save(projeto);
    }

    @GetMapping("/listas")
    public List<ConsultaProjetoTarefaTime> listasProjeto(@PageableDefault(size = 10) Pageable paginacao) {
        return projetoRepository.findAll(paginacao).stream().map(ConsultaProjetoTarefaTime::new).collect(Collectors.toList());
    }

    @GetMapping("/listas/a")
    public List<Projeto> listasProjetos() {
        return projetoRepository.findAll();
    }

    @Transactional
    @PostMapping("/alocar/time/{idTime}/{idProjeto}")//alocar time ao projeto
    public void alocarTime(@PathVariable Long idTime, @PathVariable Long idProjeto){

        if(timeRepository.findById(idProjeto).isEmpty()){
            throw new Error("Usuario inexistente");
        }

        if(projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        Time time = timeRepository.findById(idTime).get();
        Projeto projeto = projetoRepository.findById(idProjeto).get();
        projeto.setTimeProjeto(time);
        projetoRepository.save(projeto);

    }

    @Transactional
    @PostMapping("/tarefa/adicionar/{idProjeto}/{idTarefa}/{idMembro}")//adicionar tarefa ao usuario
    public void editarProjeto (@PathVariable Long idProjeto, @PathVariable Long idTarefa, @PathVariable Long idMembro){

        if (projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }
        if(tarefasRepository.findById(idTarefa).isEmpty()){
            throw new Error("Tarefas inexistente");
        }
        if(usuarioRepository.findById(idProjeto).isEmpty()){
            throw new Error("Usuario inexistente");
        }

        Projeto projeto = projetoRepository.findById(idProjeto).get();
        Tarefas tarefa = tarefasRepository.findById(idTarefa).get();
        Usuario usuario = usuarioRepository.findById(idMembro).get();

        tarefa.setUsuarioTimeTarefa(usuario);
        projeto.getTarefasProjeto().add(tarefa);
        projetoRepository.save(projeto);

    }
    @Transactional
    @PostMapping("/editar/{idProjeto}")//Editar atributos de projeto
    public void editarProjeto(@PathVariable Long idProjeto, @RequestBody EditarProjetoDto editarProjetoDto){

        if(projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        Projeto projeto = projetoRepository.findById(idProjeto).get();
        projeto.converterEditarDto(editarProjetoDto);
        projetoRepository.save(projeto);

    }
    @Transactional
    @PostMapping("/deleta/{idProjeto}")//Deleta Projeto
    public void removerProjeto(@PathVariable Long idProjeto){

        if (projetoRepository.findById(idProjeto).isEmpty()){
            throw new Error("Projeto inexistente");
        }

        Projeto projeto = projetoRepository.findById(idProjeto).get();
        System.out.println(projeto);

        if(projeto.getTarefasProjeto().isEmpty() && projeto.getTimeProjeto() == null || projeto.getTimeProjeto().getIdTime() == 0){
            projetoRepository.delete(projeto);
            System.out.println(("Removido com sucesso"));
        }else {
            throw new Error("O projeto tem time alocado ou alguna tarefa não removida");
        }


    }

}
