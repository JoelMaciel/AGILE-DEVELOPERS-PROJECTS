package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.request.TaskRequest;
import com.joelmaciel.agiledevprojects.api.dtos.request.TaskUpdateRequest;
import com.joelmaciel.agiledevprojects.api.dtos.response.TaskDTO;
import com.joelmaciel.agiledevprojects.domain.entities.Developer;
import com.joelmaciel.agiledevprojects.domain.entities.Project;
import com.joelmaciel.agiledevprojects.domain.entities.Task;
import com.joelmaciel.agiledevprojects.domain.enums.TaskStatus;
import com.joelmaciel.agiledevprojects.domain.exception.TaskNotFoundException;
import com.joelmaciel.agiledevprojects.domain.repositories.TaskRepository;
import com.joelmaciel.agiledevprojects.domain.services.DeveloperService;
import com.joelmaciel.agiledevprojects.domain.services.ProjectService;
import com.joelmaciel.agiledevprojects.domain.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final ProjectService projectService;
    private final DeveloperService developerService;
    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> findAll(Pageable pageable, String developer, String nameProject, String status) {
        Page<Task> tasks = getTaskPage(pageable, developer, nameProject, status);
        return tasks.map(TaskDTO::toDTO);
    }

    @Override
    @Transactional
    public TaskDTO save(TaskRequest taskRequest) {
        Project project = projectService.findByProjectId(taskRequest.getProjectId());
        Developer developer = developerService.findByDeveloperId(taskRequest.getDeveloperId());
        Task task = createTaskFromRequest(taskRequest, developer, project);
        return TaskDTO.toDTO(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO findById(Long taskId) {
        Task task = findByTaskId(taskId);
        return TaskDTO.toDTO(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Task findByTaskId(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }

    @Override
    @Transactional
    public TaskDTO update(Long taskId, TaskUpdateRequest taskRequest) {
        Task newTask = updateTaskFromRequest(taskId, taskRequest);
        return TaskDTO.toDTO(taskRepository.save(newTask));
    }

    private Page<Task> getTaskPage(Pageable pageable, String developer, String nameProject, String status) {
        Page<Task> tasks;
        if (developer != null) {
            tasks = taskRepository.findByDeveloper_NameContainingIgnoreCase(pageable, developer);
        } else if (nameProject != null) {
            tasks = taskRepository.findByProject_NameContainingIgnoreCase(pageable, nameProject);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(pageable, TaskStatus.valueOf(status));
        } else {
            tasks = taskRepository.findAll(pageable);
        }
        return tasks;
    }

    private Task updateTaskFromRequest(Long taskId, TaskUpdateRequest taskRequest) {
        Developer developer = developerService.findByDeveloperId(taskRequest.getDeveloperId());
        Task task = findByTaskId(taskId);
        return task.toBuilder()
                .description(taskRequest.getDescription())
                .developer(developer)
                .status(taskRequest.getStatus())
                .taskDifficulty(taskRequest.getTaskDifficulty())
                .deadlineDays(taskRequest.getDeadlineDays())
                .updateDate(OffsetDateTime.now())
                .build();
    }

    private Task createTaskFromRequest(TaskRequest request, Developer developer, Project project) {
        Task task = TaskRequest.toEntity(request);
        task.setDeveloper(developer);
        task.setProject(project);
        return task;
    }
}
