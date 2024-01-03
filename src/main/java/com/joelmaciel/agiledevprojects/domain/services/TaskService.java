package com.joelmaciel.agiledevprojects.domain.services;

import com.joelmaciel.agiledevprojects.api.dtos.request.TaskRequest;
import com.joelmaciel.agiledevprojects.api.dtos.request.TaskUpdateRequest;
import com.joelmaciel.agiledevprojects.api.dtos.response.TaskDTO;
import com.joelmaciel.agiledevprojects.domain.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskDTO save(TaskRequest taskRequest);

    TaskDTO findById(Long taskId);

    Task findByTaskId(Long taskId);

    TaskDTO update(Long taskId, TaskUpdateRequest taskRequest);

    Page<TaskDTO> findAll(Pageable pageable, String developer, String nameProject, String status);
}
