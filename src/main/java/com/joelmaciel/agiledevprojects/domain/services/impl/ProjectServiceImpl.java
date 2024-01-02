package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.request.ProjectRequest;
import com.joelmaciel.agiledevprojects.api.dtos.response.ProjectDTO;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.entities.Project;
import com.joelmaciel.agiledevprojects.domain.exception.ProjectNotFoundException;
import com.joelmaciel.agiledevprojects.domain.repositories.ProjectRepository;
import com.joelmaciel.agiledevprojects.domain.services.CompanyService;
import com.joelmaciel.agiledevprojects.domain.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final CompanyService companyService;
    private final ProjectRepository projectRepository;

    @Override
    public ProjectDTO findById(Long projectId) {
        Project project = findByProjectId(projectId);
        return ProjectDTO.toDTO(project);
    }

    @Override
    public ProjectDTO save(ProjectRequest projectRequest) {
        Company company = companyService.findByCompanyId(projectRequest.getCompanyId());
        Project project = ProjectRequest.toEntity(projectRequest);
        project.setCompany(company);

        return ProjectDTO.toDTO(projectRepository.save(project));
    }

    @Override
    public Project findByProjectId(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
    }

}
