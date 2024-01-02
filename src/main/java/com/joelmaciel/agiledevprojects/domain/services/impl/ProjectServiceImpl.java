package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.request.ProjectRequest;
import com.joelmaciel.agiledevprojects.api.dtos.request.ProjectUpdateRequest;
import com.joelmaciel.agiledevprojects.api.dtos.response.ProjectDTO;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.entities.Project;
import com.joelmaciel.agiledevprojects.domain.enums.ProjectStatus;
import com.joelmaciel.agiledevprojects.domain.exception.ProjectNotFoundException;
import com.joelmaciel.agiledevprojects.domain.repositories.ProjectRepository;
import com.joelmaciel.agiledevprojects.domain.services.CompanyService;
import com.joelmaciel.agiledevprojects.domain.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final CompanyService companyService;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable, String name, String status) {
        Page<Project> projects;
        if (name != null) {
            projects = projectRepository.findByNameContaining(pageable, name);
        } else if (status != null) {
            projects = projectRepository.findByStatus(pageable, ProjectStatus.valueOf(status));
        } else {
            projects = projectRepository.findAll(pageable);
        }
        return projects.map(ProjectDTO::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO findById(Long projectId) {
        Project project = findByProjectId(projectId);
        return ProjectDTO.toDTO(project);
    }

    @Override
    @Transactional
    public ProjectDTO update(Long projectId, ProjectUpdateRequest projectUpdateRequest) {
        Project project = findByProjectId(projectId);
        Project updatedProject = mapProductRequestToProduct(projectUpdateRequest, project);

        return ProjectDTO.toDTO(projectRepository.save(updatedProject));
    }

    @Override
    @Transactional
    public ProjectDTO save(ProjectRequest projectRequest) {
        Company company = companyService.findByCompanyId(projectRequest.getCompanyId());
        Project project = ProjectRequest.toEntity(projectRequest);
        project.setCompany(company);

        return ProjectDTO.toDTO(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public Project findByProjectId(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

    private Project mapProductRequestToProduct(ProjectUpdateRequest request, Project project) {
        return project.toBuilder()
                .name(request.getName())
                .status(request.getStatus())
                .updateDate(OffsetDateTime.now())
                .build();
    }
}
