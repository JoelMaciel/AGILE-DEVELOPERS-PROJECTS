package com.joelmaciel.agiledevprojects.api.controllers;

import com.joelmaciel.agiledevprojects.api.dtos.request.ProjectUpdateRequest;
import com.joelmaciel.agiledevprojects.api.dtos.response.ProjectDTO;
import com.joelmaciel.agiledevprojects.api.dtos.request.ProjectRequest;
import com.joelmaciel.agiledevprojects.domain.services.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/{projectId}")
    public ProjectDTO getOne(@PathVariable Long projectId) {
        return projectService.findById(projectId);
    }

    @PatchMapping("/{projectId}")
    public ProjectDTO update(@PathVariable Long projectId, @RequestBody @Valid ProjectUpdateRequest projectUpdateRequest) {
        return projectService.update(projectId,projectUpdateRequest);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDTO save(@RequestBody @Valid ProjectRequest projectRequest) {
        return projectService.save(projectRequest);
    }

}
