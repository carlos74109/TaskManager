package com.manager.TaskManagement.dto;

import com.manager.TaskManagement.Enuns.PapelUsuario;
import com.manager.TaskManagement.models.Usuario;

public record UsuarioDTO(String nomeMembro, PapelUsuario papelUsuario, String email, String senha) {

    public UsuarioDTO(Usuario usuario){
        this(usuario.getNomeUsuario(),
                usuario.getPapelMembro(), usuario.getEmail(), usuario.getSenha());
    }



}
