package com.manager.TaskManagement.dto.ConsultaListaObjetosDTo;

import com.manager.TaskManagement.Enuns.StatusProjeto;
import com.manager.TaskManagement.dto.UsuarioDTO;
import com.manager.TaskManagement.dto.TarefasDTO;
import com.manager.TaskManagement.dto.TimeDTO;
import com.manager.TaskManagement.models.Projeto;

import java.util.List;


public record ConsultaProjetoTarefaTime(String tituloProjeto,
                                        StatusProjeto statusProjeto,
                                        String descricaoProjeto,

                                        TimeDTO timeProjeto,
                                        List<TarefasConsultaDto> tarefasProjeto


                                        ) {

    public ConsultaProjetoTarefaTime(Projeto projeto){
        this(projeto.getTituloProjeto(), projeto.getStatusProjeto(),
                projeto.getDescricaoProjeto(), new TimeDTO(projeto.getTimeProjeto()),
                 projeto.atualizarTarefasProjeto());
    }
}
