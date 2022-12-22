package com.manager.TaskManagement.dto;

import com.manager.TaskManagement.Enuns.PapelUsuario;
import com.manager.TaskManagement.dto.ConsultaListaObjetosDTo.ConsultaTimeEmUsuarioDTO;
import com.manager.TaskManagement.models.Usuario;

import java.util.List;

public record UsuarioConsultaDTO(Long id, String nomeUsuario, PapelUsuario papelUsuario, List<ConsultaTimeEmUsuarioDTO> membrosTimes) {

    public UsuarioConsultaDTO(Usuario usuario){
        this(usuario.getIdMembro(), usuario.getNomeUsuario(), usuario.getPapelMembro(), usuario.atualizarListaDto());
    }

}
