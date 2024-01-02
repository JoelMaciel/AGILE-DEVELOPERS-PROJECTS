package com.joelmaciel.agiledevprojects.domain.services;

import com.joelmaciel.agiledevprojects.api.dtos.request.ProjectUpdateRequest;
import com.joelmaciel.agiledevprojects.api.dtos.response.ProjectDTO;
import com.joelmaciel.agiledevprojects.api.dtos.request.ProjectRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    ProjectDTO save(ProjectRequest projectRequest);

    ProjectDTO findById(Long projectId);

    Project findByProjectId(Long projectId);

    ProjectDTO update(Long projectId, ProjectUpdateRequest projectUpdateRequest);

    Page<ProjectDTO> findAll(Pageable pageable, String name, String status);
}
