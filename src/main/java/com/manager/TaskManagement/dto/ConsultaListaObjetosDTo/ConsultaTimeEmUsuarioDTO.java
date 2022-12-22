package com.manager.TaskManagement.dto.ConsultaListaObjetosDTo;

import com.manager.TaskManagement.models.Time;

import java.util.List;

public record ConsultaTimeEmUsuarioDTO(Long id, String nomeTime, List<ConsultaProjetoDTO> listProjetos) {

    public ConsultaTimeEmUsuarioDTO(Time time){
        this(time.getIdTime() ,time.getNomeTime(), time.converterConsultaProjetoDTo());
    }

}
