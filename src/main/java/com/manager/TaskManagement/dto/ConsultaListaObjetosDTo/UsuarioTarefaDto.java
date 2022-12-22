package com.manager.TaskManagement.dto.ConsultaListaObjetosDTo;

import com.manager.TaskManagement.dto.TarefasDTO;
import com.manager.TaskManagement.models.Tarefas;
import com.manager.TaskManagement.models.Usuario;

import java.util.List;

public record UsuarioTarefaDto(String nomeUsuario, List<TarefasDTO> tarefasDTO) {

    public UsuarioTarefaDto(Tarefas tarefas){
        this(tarefas.getUsuarioTimeTarefa().getNomeUsuario(), tarefas.converterTarefasDto());
    }



}
