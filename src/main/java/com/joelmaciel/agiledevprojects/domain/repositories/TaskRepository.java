package com.joelmaciel.agiledevprojects.domain.repositories;

import com.joelmaciel.agiledevprojects.domain.entities.Task;
import com.joelmaciel.agiledevprojects.domain.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByDeveloper_NameContainingIgnoreCase(Pageable pageable, String developer);

    Page<Task> findByProject_NameContainingIgnoreCase(Pageable pageable, String nameProject);

    Page<Task> findByStatus(Pageable pageable, TaskStatus taskStatus);
}
