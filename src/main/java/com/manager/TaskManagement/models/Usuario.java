package com.manager.TaskManagement.models;

import com.fasterxml.jackson.annotation.*;
import com.manager.TaskManagement.Enuns.PapelUsuario;
import com.manager.TaskManagement.dto.ConsultaListaObjetosDTo.ConsultaTimeEmUsuarioDTO;
import com.manager.TaskManagement.dto.DtoEditar.EditarUsuarioDto;
import com.manager.TaskManagement.dto.TarefasDTO;
import com.manager.TaskManagement.dto.UsuarioDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "idMembro")
@Entity(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMembro;
    private String nomeUsuario;
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    private PapelUsuario papelUsuario;
    @ManyToMany
    @JoinTable(name= "usuario_time", joinColumns = {@JoinColumn(name = "usuario_id")},
    inverseJoinColumns = {@JoinColumn(name = "time_id")})
    private List<Time> usuarioTimes;

    @ManyToMany
    @JoinTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Roles> roles;

    @OneToMany(mappedBy = "usuarioTimeTarefa")
    private List<Tarefas> listasTarefas;

    public Usuario() {
    }

    public Usuario(String nomeUsuario, PapelUsuario papelUsuario) {
        this.nomeUsuario = nomeUsuario;
        this.papelUsuario = papelUsuario;
    }

    public Long getIdMembro() {
        return idMembro;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public PapelUsuario getPapelMembro() {
        return papelUsuario;
    }

    public void setPapelMembro(PapelUsuario papelUsuario) {
        this.papelUsuario = papelUsuario;
    }

    public List<Time> getUsuarioTimes() {
        return usuarioTimes;
    }

    public void setUsuarioTimes(List<Time> usuarioTimes) {
        this.usuarioTimes = usuarioTimes;
    }

    public List<Roles> getRoles() {
        return roles;
    }

    public void setRoles(List<Roles> roles) {
        this.roles = roles;
    }

    public List<Tarefas> getListasTarefas() {
        return listasTarefas;
    }

    public void setListasTarefas(List<Tarefas> listasTarefas) {
        this.listasTarefas = listasTarefas;
    }

    public void atualizarDto(UsuarioDTO usuarioDTO) {
        this.nomeUsuario = usuarioDTO.nomeMembro();
        this.papelUsuario = usuarioDTO.papelUsuario();
        this.email = usuarioDTO.email();
        this.senha = usuarioDTO.senha();
    }

    public List<ConsultaTimeEmUsuarioDTO> atualizarListaDto() {
         return this.getUsuarioTimes().stream().map(ConsultaTimeEmUsuarioDTO::new).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.nomeUsuario;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<TarefasDTO> converterTarefas() {
        return this.getListasTarefas().stream().map(TarefasDTO::new).collect(Collectors.toList());
    }

    public void converterEditarUsuarioDto(EditarUsuarioDto editarUsuarioDto) {
        if(editarUsuarioDto.nomeMembro() != null && !editarUsuarioDto.nomeMembro().isEmpty()){
            this.nomeUsuario = editarUsuarioDto.nomeMembro();
        }
        if(editarUsuarioDto.email() != null && !editarUsuarioDto.email().isEmpty()){
            this.email = editarUsuarioDto.email();
        }
    }
}
