package com.manager.TaskManagement.repository;

import com.manager.TaskManagement.models.Tarefas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TarefasRepository extends JpaRepository<Tarefas, Long> {

    @Query("SELECT t.tituloTarefa, t.statusTarefa, t.descricaoTarefa, t.usuarioTimeTarefa.nomeUsuario, t.usuarioTimeTarefa.email FROM tarefas t WHERE t.usuarioTimeTarefa.idMembro= ?1")
    Page<Tarefas> findByTarefaUsuario(Long id, Pageable paginacao);//Trazer as tarefas de um determinado usuario


    @Query("SELECT t.tituloTarefa, t.statusTarefa, t.descricaoTarefa, t.usuarioTimeTarefa.nomeUsuario, t.usuarioTimeTarefa.email  FROM tarefas t WHERE t.projeto_id.idProjeto= ?1")
    Page<Tarefas> findByProjeto(Long id, Pageable paginacao);//Trazer todas as tarefas de um determinado Projeto com usuarios setados

    @Query("SELECT t.tituloTarefa, t.statusTarefa, t.descricaoTarefa FROM tarefas t WHERE t.projeto_id.idProjeto= ?1 and t.usuarioTimeTarefa IS NULL")
    Page<Tarefas> findByProjetoComUsuarioNull(Long id, Pageable paginacao);//Trazer todas as tarefas de um determinado Projeto com usuarios NÃO setados

}
