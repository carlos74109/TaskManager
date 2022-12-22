package com.manager.TaskManagement.dto.DtoEditar;

import com.manager.TaskManagement.Enuns.StatusTarefa;

public record EditarTarefaDto(String tituloTarefa, StatusTarefa statusTarefa, String descricaoTarefa) {



}
