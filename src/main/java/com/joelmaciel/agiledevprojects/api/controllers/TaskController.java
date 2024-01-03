package com.joelmaciel.agiledevprojects.api.controllers;

import com.joelmaciel.agiledevprojects.api.dtos.request.TaskRequest;
import com.joelmaciel.agiledevprojects.api.dtos.request.TaskUpdateRequest;
import com.joelmaciel.agiledevprojects.api.dtos.response.TaskDTO;
import com.joelmaciel.agiledevprojects.domain.services.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public Page<TaskDTO> getAll(
            @PageableDefault(page = 0, size = 10, sort = "taskId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String developer,
            @RequestParam(required = false) String nameProject,
            @RequestParam(required = false) String status
    ) {
        return taskService.findAll(pageable, developer, nameProject, status);
    }


    @GetMapping("/{taskId}")
    public TaskDTO getOne(@PathVariable Long taskId) {
        return taskService.findById(taskId);
    }

    @PatchMapping("/{taskId}")
    public TaskDTO update(@PathVariable Long taskId, @RequestBody @Valid TaskUpdateRequest taskRequest) {
        return taskService.update(taskId, taskRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO save(@RequestBody @Valid TaskRequest taskRequest) {
        return taskService.save(taskRequest);
    }

}
