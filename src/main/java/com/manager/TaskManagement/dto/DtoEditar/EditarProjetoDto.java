package com.manager.TaskManagement.dto.DtoEditar;

import com.manager.TaskManagement.Enuns.StatusProjeto;

public record EditarProjetoDto(String tituloProjeto, StatusProjeto statusProjeto, String descricaoProjeto) {
}
