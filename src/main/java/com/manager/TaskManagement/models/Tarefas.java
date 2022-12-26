package com.manager.TaskManagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.manager.TaskManagement.Enuns.StatusTarefa;
import com.manager.TaskManagement.dto.DtoEditar.EditarTarefaDto;
import com.manager.TaskManagement.dto.TarefasDTO;
import com.manager.TaskManagement.dto.UsuarioDTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "tarefas")
public class Tarefas {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long idTarefas;
    @NotBlank
    private String tituloTarefa;
    @Enumerated(EnumType.STRING)
    private StatusTarefa statusTarefa;
    @Column(columnDefinition="TEXT")
    private String descricaoTarefa;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    @JsonIgnoreProperties("tarefasProjeto")
    private Projeto projeto_id;
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuarioTimeTarefa;
    private float tempoPrevistoTarefa;
    private Date dataCriacao;
    private Date dataFinalizacao;
    private Date dataAtualizacao;

    public Tarefas(String tituloTarefa, StatusTarefa statusTarefa, String descricaoTarefa, Usuario menbroTimeTarefa, float tempoPrevistoTarefa, Date dataCriacao, Date dataAtualizacao) {
        this.tituloTarefa = tituloTarefa;
        this.statusTarefa = statusTarefa;
        this.descricaoTarefa = descricaoTarefa;
        this.usuarioTimeTarefa = menbroTimeTarefa;
        this.tempoPrevistoTarefa = tempoPrevistoTarefa;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Tarefas() {
    }

    public Date getDataFinalizacao() {
        return dataFinalizacao;
    }

    public void setDataFinalizacao(Date dataFinalizacao) {
        this.dataFinalizacao = dataFinalizacao;
    }

    public String getDescricaoTarefa() {
        return descricaoTarefa;
    }

    public void setDescricaoTarefa(String descricaoTarefa) {
        this.descricaoTarefa = descricaoTarefa;
    }

    public Long getIdTarefas() {
        return idTarefas;
    }

    public String getTituloTarefa() {
        return tituloTarefa;
    }

    public void setTituloTarefa(String tituloTarefa) {
        this.tituloTarefa = tituloTarefa;
    }

    public StatusTarefa getStatusTarefa() {
        return statusTarefa;
    }

    public void setStatusTarefa(StatusTarefa statusTarefa) {
        this.statusTarefa = statusTarefa;
    }

    public Usuario getUsuarioTimeTarefa() {
        return usuarioTimeTarefa;
    }

    public void setUsuarioTimeTarefa(Usuario usuarioTimeTarefa) {
        this.usuarioTimeTarefa = usuarioTimeTarefa;
    }

    public float getTempoPrevistoTarefa() {
        return tempoPrevistoTarefa;
    }

    public void setTempoPrevistoTarefa(float tempoPrevistoTarefa) {
        this.tempoPrevistoTarefa = tempoPrevistoTarefa;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public Projeto getProjeto_id() {
        return projeto_id;
    }

    public void setProjeto_id(Projeto projeto_id) {
        this.projeto_id = projeto_id;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public void criarTarefaDto(TarefasDTO tarefasDTO) {
        this.dataCriacao = new Date();
        this.tituloTarefa = tarefasDTO.tituloTarefa();
        setStatusTarefa(StatusTarefa.FAZER);
        this.descricaoTarefa = tarefasDTO.descricaoTarefa();

    }


    public List<TarefasDTO> converterTarefasDto() {
        return this.getUsuarioTimeTarefa().getListasTarefas().stream().map(TarefasDTO::new).collect(Collectors.toList());
    }


    public void converterEditarTarefaDto(EditarTarefaDto editarTarefaDto) {
        if(editarTarefaDto.tituloTarefa() != null && !editarTarefaDto.tituloTarefa().isEmpty()){
            this.tituloTarefa = editarTarefaDto.tituloTarefa();
        }
        if(editarTarefaDto.descricaoTarefa() != null && !editarTarefaDto.descricaoTarefa().isEmpty()){
            this.descricaoTarefa = editarTarefaDto.descricaoTarefa();
        }
        if(editarTarefaDto.statusTarefa() != null){
            this.statusTarefa = editarTarefaDto.statusTarefa();
        }
    }
}
