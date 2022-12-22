package com.manager.TaskManagement.repository;

import com.manager.TaskManagement.Enuns.PapelUsuario;
import com.manager.TaskManagement.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByPapelUsuario(PapelUsuario papelUsuario);


    Usuario findByEmail(String email);

    Optional<Usuario> findByNomeUsuario(String nomeUsuario);

}
