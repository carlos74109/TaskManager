package com.manager.TaskManagement.dto.ConsultaListaObjetosDTo;

import com.manager.TaskManagement.Enuns.StatusProjeto;
import com.manager.TaskManagement.models.Projeto;

public record ConsultaProjetoDTO(String tituloProjeto, StatusProjeto statusProjeto) {

    public ConsultaProjetoDTO(Projeto projeto){
        this(projeto.getTituloProjeto(), projeto.getStatusProjeto());
    }

}
