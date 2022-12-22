package com.manager.TaskManagement.dto;

import com.manager.TaskManagement.models.Projeto;
import com.manager.TaskManagement.models.Time;

import java.util.List;

public record consultaTimeAndProjetoDto(Long idTime ,String nomeTime, List<Projeto> timeListProjetos) {

    public consultaTimeAndProjetoDto(Time time){
        this(time.getIdTime(), time.getNomeTime(), time.getTimeListProjetos());
    }

}
