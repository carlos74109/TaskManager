package com.manager.TaskManagement.repository;

import com.manager.TaskManagement.models.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
