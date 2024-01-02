package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.request.ProjectRequest;
import com.joelmaciel.agiledevprojects.api.dtos.request.ProjectUpdateRequest;
import com.joelmaciel.agiledevprojects.api.dtos.response.ProjectDTO;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.entities.Project;
import com.joelmaciel.agiledevprojects.domain.enums.ProjectStatus;
import com.joelmaciel.agiledevprojects.domain.exception.CompanyNotFoundException;
import com.joelmaciel.agiledevprojects.domain.repositories.ProjectRepository;
import com.joelmaciel.agiledevprojects.domain.services.CompanyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    public static final String NULL_SAVED_IN_THE_DATABASE = "There is no company with this id null saved in the database";
    @Mock
    private CompanyService companyService;
    @Mock
    private ProjectRepository projectRepository;
    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    @DisplayName("Given valid ProjectRequest, when saving a project, then return ProjectDTO successfully")
    void givenValidProjectRequest_whenSavingProject_thenReturnProjectDTOSuccessfully() {
        Company mockCompany = getMockCompany();
        ProjectRequest mockProjectRequest = getMockProjectRequest();
        Project mockProject = ProjectRequest.toEntity(mockProjectRequest);
        mockProject.setCompany(mockCompany);

        when(companyService.findByCompanyId(mockCompany.getCompanyId())).thenReturn(mockCompany);
        when(projectRepository.save(any(Project.class))).thenReturn(mockProject);

        ProjectDTO projectDTO = projectService.save(mockProjectRequest);

        assertNotNull(projectDTO);
        assertEquals(mockProjectRequest.getName(), projectDTO.getName());
    }

    @Test
    @DisplayName("Given invalid CompanyId in ProjectRequest, when saving a project, then throw exception with correct message")
    void givenInvalidCompanyIdInProjectRequest_whenSavingProject_thenThrowException() {
        ProjectRequest mockProjectRequest = getMockProjectRequest();
        mockProjectRequest.setCompanyId(null);

        when(companyService.findByCompanyId(null))
                .thenThrow(new CompanyNotFoundException(NULL_SAVED_IN_THE_DATABASE));

        CompanyNotFoundException exception = assertThrows(
                CompanyNotFoundException.class,
                () -> projectService.save(mockProjectRequest)
        );

        assertEquals(NULL_SAVED_IN_THE_DATABASE, exception.getMessage());
        verify(companyService, times(1)).findByCompanyId(null);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    @DisplayName("Given valid ProjectId, when finding a project by id, then return ProjectDTO successfully")
    void givenValidProjectId_whenFindingProjectById_thenReturnProjectDTOSuccessfully() {
        Company mockCompany = getMockCompany();
        Project mockProject = getMockProject();
        mockProject.setCompany(mockCompany);

        when(projectRepository.findById(mockProject.getProjectId())).thenReturn(Optional.of(mockProject));

        ProjectDTO projectDTO = projectService.findById(mockProject.getProjectId());

        assertNotNull(projectDTO);
        assertEquals(mockProject.getName(), projectDTO.getName());
        assertEquals(mockProject.getStatus(), projectDTO.getStatus());

        verify(projectRepository, times(1)).findById(mockProject.getProjectId());
    }

    @Test
    @DisplayName("Given valid ProjectId and ProjectUpdateRequest, when updating a project, then return ProjectDTO successfully")
    void givenValidProjectIdAndProjectUpdateRequest_whenUpdatingProject_thenReturnProjectDTOSuccessfully() {
        ProjectUpdateRequest mockProjectUpdateRequest = getMockProjectUpdateRequest();

        Project mockProject = getMockProjectWithUpdateRequest(mockProjectUpdateRequest);

        when(projectRepository.findById(mockProject.getProjectId())).thenReturn(Optional.of(mockProject));
        when(projectRepository.save(any(Project.class))).thenReturn(mockProject);

        ProjectDTO projectDTO = projectService.update(mockProject.getProjectId(), mockProjectUpdateRequest);

        assertNotNull(projectDTO);
        assertEquals(mockProjectUpdateRequest.getName(), projectDTO.getName());
        assertEquals(mockProjectUpdateRequest.getStatus(), projectDTO.getStatus());

        verify(projectRepository, times(1)).findById(mockProject.getProjectId());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    private ProjectRequest getMockProjectRequest() {
        return ProjectRequest.builder()
                .name("Sample Project")
                .status(ProjectStatus.IN_PROGRESS)
                .companyId(1L)
                .build();
    }

    private ProjectUpdateRequest getMockProjectUpdateRequest() {
        return ProjectUpdateRequest.builder()
                .name("Updated Project")
                .status(ProjectStatus.COMPLETED)
                .build();
    }

    private Project getMockProjectWithUpdateRequest(ProjectUpdateRequest updateRequest) {
        Company mockCompany = getMockCompany();

        Project mockProject = getMockProject();
        mockProject.setCompany(mockCompany);

        mockProject = mockProject.toBuilder()
                .name(updateRequest.getName())
                .status(updateRequest.getStatus())
                .build();

        return mockProject;
    }

    private Project getMockProject() {
        return Project.builder()
                .projectId(1L)
                .name("Sample Project")
                .status(ProjectStatus.IN_PROGRESS)
                .build();
    }

    private Company getMockCompany() {
        return Company.builder()
                .companyId(1L)
                .name("IBM")
                .build();
    }
}