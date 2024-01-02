package com.joelmaciel.agiledevprojects.domain.services;

import com.joelmaciel.agiledevprojects.api.dtos.response.ProjectDTO;
import com.joelmaciel.agiledevprojects.api.dtos.request.ProjectRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Project;

public interface ProjectService {
    ProjectDTO save(ProjectRequest projectRequest);

    ProjectDTO findById(Long projectId);

    Project findByProjectId(Long projectId);
}
