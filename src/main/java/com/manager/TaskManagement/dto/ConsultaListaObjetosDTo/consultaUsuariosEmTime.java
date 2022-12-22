package com.manager.TaskManagement.dto.ConsultaListaObjetosDTo;

import com.manager.TaskManagement.Enuns.PapelUsuario;
import com.manager.TaskManagement.dto.UsuarioDTO;
import com.manager.TaskManagement.models.Time;
import com.manager.TaskManagement.models.Usuario;

import java.util.List;

public record consultaUsuariosEmTime(List<UsuarioDTO> usuarioDTOS) {

    public consultaUsuariosEmTime(Time time){
        this(time.atualizarDtoUsuario());
    }

}
