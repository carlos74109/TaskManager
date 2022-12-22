package com.manager.TaskManagement.repository;

import com.manager.TaskManagement.models.Time;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TimeRepository extends JpaRepository<Time, Long> {
    @Query(value = "Select t from time t JOIN FETCH t.timesUsuarios f")
    List<Time> findTimeUsuario();

}
