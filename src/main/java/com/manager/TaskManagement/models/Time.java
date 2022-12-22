package com.manager.TaskManagement.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.manager.TaskManagement.dto.ConsultaListaObjetosDTo.ConsultaProjetoDTO;
import com.manager.TaskManagement.dto.TimeDTO;
import com.manager.TaskManagement.dto.UsuarioDTO;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "idTime")
@Entity(name = "time")
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTime;

    private String nomeTime;
    @ManyToMany
    @JoinTable(name= "usuario_time", joinColumns = {@JoinColumn(name = "time_id")},
            inverseJoinColumns = {@JoinColumn(name = "usuario_id")})
    private List<Usuario> timesUsuarios;

    @OneToMany(mappedBy = "timeProjeto")
    private List<Projeto> timeListProjetos;
    public Time() {
    }

    public Long getIdTime() {
        return idTime;
    }

    public String getNomeTime() {
        return nomeTime;
    }

    public void setNomeTime(String nomeTime) {
        this.nomeTime = nomeTime;
    }

    public List<Usuario> getTimesUsuarios() {
        return timesUsuarios;
    }

    public void setTimesUsuarios(List<Usuario> timesUsuarios) {
        this.timesUsuarios = timesUsuarios;
    }


    public List<Projeto> getTimeListProjetos() {
        return timeListProjetos;
    }

    public void setTimeListProjetos(List<Projeto> timeListProjetos) {
        this.timeListProjetos = timeListProjetos;
    }

    public void salvarTime(TimeDTO timeDTO) {
        this.nomeTime = timeDTO.nomeTime();
    }

    public List<ConsultaProjetoDTO> converterConsultaProjetoDTo() {
        return this.getTimeListProjetos().stream().map(ConsultaProjetoDTO::new).collect(Collectors.toList());
    }

    public List<UsuarioDTO> atualizarDtoUsuario() {
        return getTimesUsuarios().stream().map(UsuarioDTO::new).collect(Collectors.toList());
    }


}
