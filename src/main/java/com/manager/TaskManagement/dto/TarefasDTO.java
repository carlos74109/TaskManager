package com.manager.TaskManagement.dto;

import com.manager.TaskManagement.Enuns.StatusTarefa;
import com.manager.TaskManagement.models.Usuario;
import com.manager.TaskManagement.models.Tarefas;

import java.time.LocalDateTime;

public record TarefasDTO(String tituloTarefa,
                         StatusTarefa statusTarefa,
                         String descricaoTarefa,
                         LocalDateTime dataCriacao,

                         Usuario usuario

                         ) {

    public TarefasDTO(Tarefas tarefas){
        this(tarefas.getTituloTarefa(),
                tarefas.getStatusTarefa(),
                tarefas.getDescricaoTarefa(),
                tarefas.getDataCriacao(), tarefas.getUsuarioTimeTarefa()
                );
    }
}
