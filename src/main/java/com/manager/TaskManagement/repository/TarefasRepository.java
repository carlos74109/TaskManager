package com.manager.TaskManagement.repository;

import com.manager.TaskManagement.Enuns.StatusTarefa;
import com.manager.TaskManagement.dto.ConsultaListaObjetosDTo.TarefasConsultaDto;
import com.manager.TaskManagement.models.Tarefas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface TarefasRepository extends JpaRepository<Tarefas, Long> {

    @Query("SELECT t.tituloTarefa, t.statusTarefa, t.descricaoTarefa, t.usuarioTimeTarefa.nomeUsuario, t.usuarioTimeTarefa.email FROM tarefas t WHERE t.usuarioTimeTarefa.idMembro= ?1")
    Page<Tarefas> findByTarefaUsuario(Long id, Pageable paginacao);//Trazer as tarefas de um determinado usuario

    @Query("SELECT t.tituloTarefa, t.statusTarefa, t.descricaoTarefa, t.usuarioTimeTarefa.nomeUsuario, t.usuarioTimeTarefa.email  FROM tarefas t WHERE t.projeto_id.idProjeto= ?1")
    Page<Tarefas> findByProjeto(Long id, Pageable paginacao);//Trazer todas as tarefas de um determinado Projeto com usuarios setados

    @Query("SELECT t.tituloTarefa, t.statusTarefa, t.descricaoTarefa, t.dataCriacao FROM tarefas t WHERE t.projeto_id.idProjeto= ?1 and t.usuarioTimeTarefa IS NULL")
    Page<Tarefas> findByProjetoComUsuarioNull(Long id, Pageable paginacao);//Trazer todas as tarefas de um determinado Projeto com usuarios NÃO setados
    @Query("SELECT t.tituloTarefa, t.statusTarefa, t.descricaoTarefa, t.dataCriacao FROM tarefas t WHERE t.statusTarefa= ?1 AND t.projeto_id.idProjeto= ?2")
    Page<Tarefas> findByStatusTarefaFazer(StatusTarefa valueOf, Long idProjeto, Pageable paginacao);//Trazer todas as tarefas com seu determinado Status

    @Query("SELECT t.tituloTarefa, t.statusTarefa, t.descricaoTarefa, t.usuarioTimeTarefa.nomeUsuario, t.usuarioTimeTarefa.email, t.dataCriacao, t.dataAtualizacao FROM tarefas t WHERE t.statusTarefa= ?1 AND t.projeto_id.idProjeto= ?2")
    Page<Tarefas> findByStatusTarefaFazendo(StatusTarefa valueOf, Long idProjeto, Pageable paginacao);//Trazer todas as tarefas com seu determinado Status

    @Query("SELECT t.tituloTarefa, t.statusTarefa, t.descricaoTarefa, t.usuarioTimeTarefa.nomeUsuario, t.usuarioTimeTarefa.email, t.dataCriacao, t.dataAtualizacao, t.dataFinalizacao FROM tarefas t WHERE t.statusTarefa= ?1 AND t.projeto_id.idProjeto= ?2")
    Page<Tarefas> findByStatusTarefaFeito(StatusTarefa valueOf, Long idProjeto, Pageable paginacao);//Trazer todas as tarefas com seu determinado Status

    @Query("SELECT t.tituloTarefa, t.statusTarefa, t.descricaoTarefa FROM tarefas t WHERE t.statusTarefa= ?1 AND t.usuarioTimeTarefa IS NULL AND t.projeto_id.idProjeto= ?2")
    Page<Tarefas> findByStatusTarefaNull(StatusTarefa valueOf, Long idProjeto, Pageable paginacao);// Trazer todas as tarefas a fazer onde o usuario é null
}
