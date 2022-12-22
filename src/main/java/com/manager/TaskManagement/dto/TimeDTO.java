package com.manager.TaskManagement.dto;

import com.manager.TaskManagement.models.Time;
import com.manager.TaskManagement.models.Usuario;

import java.util.List;

public record TimeDTO(String nomeTime, List<UsuarioDTO> usuarios) {

    public TimeDTO(Time time){
        this(time.getNomeTime(), time.atualizarDtoUsuario());
    }

}
