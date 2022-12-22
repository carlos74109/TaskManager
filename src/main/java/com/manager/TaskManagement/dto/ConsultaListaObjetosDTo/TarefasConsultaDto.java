package com.manager.TaskManagement.dto.ConsultaListaObjetosDTo;

import com.manager.TaskManagement.Enuns.StatusTarefa;
import com.manager.TaskManagement.dto.UsuarioDTO;
import com.manager.TaskManagement.models.Tarefas;
import com.manager.TaskManagement.models.Usuario;

import java.time.LocalDateTime;

public record TarefasConsultaDto(String tituloTarefa,
                                 StatusTarefa statusTarefa,
                                 String descricaoTarefa,
                                 LocalDateTime dataCriacao,

                                 UsuarioDTO usuarioDTO) {
    public TarefasConsultaDto(Tarefas tarefas){
        this(tarefas.getTituloTarefa(),
                tarefas.getStatusTarefa(),
                tarefas.getDescricaoTarefa(),
                tarefas.getDataCriacao(), new UsuarioDTO(tarefas.getUsuarioTimeTarefa())
        );
    }
}
