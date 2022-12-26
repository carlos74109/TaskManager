package com.manager.TaskManagement.dto;

import com.manager.TaskManagement.Enuns.StatusTarefa;
import com.manager.TaskManagement.models.Usuario;
import com.manager.TaskManagement.models.Tarefas;

import java.time.LocalDateTime;
import java.util.Date;

public record TarefasDTO(String tituloTarefa,

                         String descricaoTarefa,
                         Date dataCriacao,

                         Usuario usuario

                         ) {

    public TarefasDTO(Tarefas tarefas){
        this(tarefas.getTituloTarefa(),
                tarefas.getDescricaoTarefa(),
                tarefas.getDataCriacao(), tarefas.getUsuarioTimeTarefa()
                );
    }
}
