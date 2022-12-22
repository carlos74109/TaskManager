package com.manager.TaskManagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manager.TaskManagement.Enuns.StatusProjeto;
import com.manager.TaskManagement.dto.ConsultaListaObjetosDTo.TarefasConsultaDto;
import com.manager.TaskManagement.dto.DtoEditar.EditarProjetoDto;
import com.manager.TaskManagement.dto.UsuarioDTO;
import com.manager.TaskManagement.dto.ProjetoDTO;
import com.manager.TaskManagement.dto.TarefasDTO;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProjeto;
    private String tituloProjeto;
    @Enumerated(EnumType.STRING)
    private StatusProjeto statusProjeto;
    @Column(columnDefinition="TEXT")
    private String descricaoProjeto;

    @ManyToOne()
    @JoinColumn(name = "time_id")
    private Time timeProjeto;
    @OneToMany(mappedBy = "projeto_id")
    @JsonIgnoreProperties("projeto_id")
    private List<Tarefas> tarefasProjeto;

    public Projeto() {
    }

    public Projeto(String tituloProjeto, StatusProjeto statusProjeto, String descricaoProjeto, Time timeProjeto, List<Tarefas> tarefasProjeto) {
        this.tituloProjeto = tituloProjeto;
        this.statusProjeto = statusProjeto;
        this.descricaoProjeto = descricaoProjeto;
        this.timeProjeto = timeProjeto;
        this.tarefasProjeto = tarefasProjeto;
    }

    public Long getIdProjeto() {
        return idProjeto;
    }

    public String getTituloProjeto() {
        return tituloProjeto;
    }

    public void setTituloProjeto(String tituloProjeto) {
        this.tituloProjeto = tituloProjeto;
    }

    public StatusProjeto getStatusProjeto() {
        return statusProjeto;
    }

    public void setStatusProjeto(StatusProjeto statusProjeto) {
        this.statusProjeto = statusProjeto;
    }

    public String getDescricaoProjeto() {
        return descricaoProjeto;
    }

    public void setDescricaoProjeto(String descricaoProjeto) {
        this.descricaoProjeto = descricaoProjeto;
    }

    public Time getTimeProjeto() {
        return timeProjeto;
    }

    public void setTimeProjeto(Time timeProjeto) {
        this.timeProjeto = timeProjeto;
    }

    public List<Tarefas> getTarefasProjeto() {
        return tarefasProjeto;
    }

    public void setTarefasProjeto(List<Tarefas> tarefasProjeto) {
        this.tarefasProjeto = tarefasProjeto;
    }

    public void salvarProjetoDto(ProjetoDTO projetoDTO) {
        this.tituloProjeto = projetoDTO.tituloProjeto();
        this.descricaoProjeto = projetoDTO.descricaoProjeto();
        this.statusProjeto = projetoDTO.statusProjeto();
    }

    public List<UsuarioDTO> atualizarMembroDto() {
        return getTimeProjeto().getTimesUsuarios().stream().map(UsuarioDTO::new).collect(Collectors.toList());
    }

    public List<TarefasConsultaDto> atualizarTarefasProjeto() {
        return getTarefasProjeto().stream().map(TarefasConsultaDto::new).collect(Collectors.toList());
    }

    public void converterEditarDto(EditarProjetoDto editarProjetoDto) {
        if(editarProjetoDto.tituloProjeto() != null && !editarProjetoDto.tituloProjeto().isEmpty()){
            this.tituloProjeto = editarProjetoDto.tituloProjeto();
        }
        if(editarProjetoDto.statusProjeto() != null && !editarProjetoDto.statusProjeto().equals("")){
            this.statusProjeto = editarProjetoDto.statusProjeto();
        }
        if(editarProjetoDto.descricaoProjeto() != null && !editarProjetoDto.descricaoProjeto().isEmpty()){
            this.descricaoProjeto = editarProjetoDto.descricaoProjeto();
        }

    }
}
