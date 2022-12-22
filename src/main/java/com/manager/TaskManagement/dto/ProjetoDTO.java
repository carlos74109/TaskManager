package com.manager.TaskManagement.dto;

import com.manager.TaskManagement.Enuns.StatusProjeto;
import com.manager.TaskManagement.models.Projeto;
import com.manager.TaskManagement.models.Tarefas;
import com.manager.TaskManagement.models.Time;

import java.util.List;

public record ProjetoDTO(String tituloProjeto,
                         StatusProjeto statusProjeto,
                         String descricaoProjeto,
                         List<Tarefas> tarefasList,
                         Time timeProjeto) {

    public ProjetoDTO(Projeto projeto){
        this(projeto.getTituloProjeto(), projeto.getStatusProjeto(), projeto.getDescricaoProjeto(), projeto.getTarefasProjeto(), projeto.getTimeProjeto());
    }
}
