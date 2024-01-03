package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.request.TaskRequest;
import com.joelmaciel.agiledevprojects.api.dtos.request.TaskUpdateRequest;
import com.joelmaciel.agiledevprojects.api.dtos.response.TaskDTO;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.entities.Developer;
import com.joelmaciel.agiledevprojects.domain.entities.Project;
import com.joelmaciel.agiledevprojects.domain.entities.Task;
import com.joelmaciel.agiledevprojects.domain.enums.Difficulty;
import com.joelmaciel.agiledevprojects.domain.enums.ExperienceLevel;
import com.joelmaciel.agiledevprojects.domain.enums.ProjectStatus;
import com.joelmaciel.agiledevprojects.domain.enums.TaskStatus;
import com.joelmaciel.agiledevprojects.domain.exception.DeveloperNotFoundException;
import com.joelmaciel.agiledevprojects.domain.exception.ProjectNotFoundException;
import com.joelmaciel.agiledevprojects.domain.exception.TaskNotFoundException;
import com.joelmaciel.agiledevprojects.domain.repositories.TaskRepository;
import com.joelmaciel.agiledevprojects.domain.services.DeveloperService;
import com.joelmaciel.agiledevprojects.domain.services.ProjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    public static final String TASK_NOT_FOUND = "There is no task with this id 100 saved in the database";
    @Mock
    private ProjectService projectService;
    @Mock
    private DeveloperService developerService;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    TaskServiceImpl taskService;

    @Test
    @DisplayName("Given developer name, when finding tasks, then return Page<TaskDTO>")
    void givenDeveloperName_whenFindingTasks_thenReturnPageOfTaskDTO() {
        String developerName = "Joel";
        PageRequest pageable = PageRequest.of(0, 10);

        when(taskRepository.findByDeveloper_NameContainingIgnoreCase(pageable, developerName))
                .thenReturn(new PageImpl<>(Collections.singletonList(getMockTask())));

        Page<TaskDTO> result = taskService.findAll(pageable, developerName, null, null);

        assertNotNull(result);
        assertEquals(1, result.getSize());

        verify(taskRepository, times(1)).findByDeveloper_NameContainingIgnoreCase(pageable, developerName);
        verify(taskRepository, never()).findByProject_NameContainingIgnoreCase(any(), any());
        verify(taskRepository, never()).findByStatus(any(), any());
        verify(taskRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Given project name, when finding tasks, then return Page<TaskDTO>")
    void givenProjectName_whenFindingTasks_thenReturnPageOfTaskDTO() {
        String projectName = "Sample Project";
        PageRequest pageable = PageRequest.of(0, 10);

        when(taskRepository.findByProject_NameContainingIgnoreCase(pageable, projectName))
                .thenReturn(new PageImpl<>(Collections.singletonList(getMockTask())));

        Page<TaskDTO> result = taskService.findAll(pageable, null, projectName, null);

        assertNotNull(result);
        assertEquals(1, result.getSize());

        verify(taskRepository, never()).findByDeveloper_NameContainingIgnoreCase(any(), any());
        verify(taskRepository, times(1)).findByProject_NameContainingIgnoreCase(pageable, projectName);
        verify(taskRepository, never()).findByStatus(any(), any());
        verify(taskRepository, never()).findAll(any(Pageable.class));
    }


    @Test
    @DisplayName("Given status, when finding tasks, then return Page<TaskDTO>")
    void givenStatus_whenFindingTasks_thenReturnPageOfTaskDTO() {
        String status = "IN_PROGRESS";
        PageRequest pageable = PageRequest.of(0, 10);

        when(taskRepository.findByStatus(pageable, TaskStatus.valueOf(status)))
                .thenReturn(new PageImpl<>(Collections.singletonList(getMockTask())));

        Page<TaskDTO> result = taskService.findAll(pageable, null, null, status);

        assertNotNull(result);
        assertEquals(1, result.getSize());

        verify(taskRepository, never()).findByDeveloper_NameContainingIgnoreCase(any(), any());
        verify(taskRepository, never()).findByProject_NameContainingIgnoreCase(any(), any());
        verify(taskRepository, times(1)).findByStatus(pageable, TaskStatus.valueOf(status));
        verify(taskRepository, never()).findAll(any(Pageable.class));
    }


    @Test
    @DisplayName("Given no specific parameters, when finding tasks, then return Page<TaskDTO>")
    void givenNoParameters_whenFindingTasks_thenReturnPageOfTaskDTO() {
        PageRequest pageable = PageRequest.of(0, 10);

        when(taskRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(getMockTask())));

        Page<TaskDTO> result = taskService.findAll(pageable, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.getSize());

        verify(taskRepository, never()).findByDeveloper_NameContainingIgnoreCase(any(), any());
        verify(taskRepository, never()).findByProject_NameContainingIgnoreCase(any(), any());
        verify(taskRepository, never()).findByStatus(any(), any());
        verify(taskRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Given valid TaskRequest, when saving a task, then return TaskDTO successfully")
    void givenValidTaskRequest_whenSavingTask_thenReturnTaskDTOSuccessfully() {
        Project mockProject = getMockProject();
        Developer mockDeveloper = getMockDeveloper();
        TaskRequest taskRequest = getMockTaskRequest();

        when(projectService.findByProjectId(taskRequest.getProjectId())).thenReturn(mockProject);
        when(developerService.findByDeveloperId(taskRequest.getDeveloperId())).thenReturn(mockDeveloper);
        when(taskRepository.save(any(Task.class))).thenReturn(getMockTask());

        TaskDTO taskDTO = taskService.save(taskRequest);

        assertNotNull(taskDTO);
        assertEquals(taskRequest.getDescription(), taskDTO.getDescription());
        assertEquals(taskRequest.getStatus(), taskDTO.getStatus());
        assertEquals(taskRequest.getTaskDifficulty(), taskDTO.getTaskDifficulty());
    }


    @Test
    @DisplayName("Given invalid ProjectId, when saving a task, then throw exception")
    void givenInvalidProject_whenSavingTask_thenThrowException() {
        TaskRequest invalidTaskRequest = getMockInvalidTaskRequest();
        when(projectService.findByProjectId(any())).thenThrow(new ProjectNotFoundException("Project not found"));

        assertThrows(ProjectNotFoundException.class, () -> taskService.save(invalidTaskRequest));
        verify(projectService, times(1)).findByProjectId(any());
        verify(developerService, never()).findByDeveloperId(any());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Given invalid DeveloperId, when saving a task, then throw exception")
    void givenInvalidDeveloper_whenSavingTask_thenThrowException() {
        TaskRequest invalidTaskRequest = getMockInvalidTaskRequest();
        invalidTaskRequest.setProjectId(1L);
        when(developerService.findByDeveloperId(any())).thenThrow(new DeveloperNotFoundException("Project not found"));

        assertThrows(DeveloperNotFoundException.class, () -> taskService.save(invalidTaskRequest));
        verify(projectService, times(1)).findByProjectId(any());
        verify(developerService, times(1)).findByDeveloperId(any());
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    @DisplayName("Given a valid taskId, when finding a task by id, then return TaskDTO")
    void givenValidTaskId_whenFindingTaskById_thenReturnTaskDTO() {
        Task mockTask = getMockTask();
        when(taskRepository.findById(mockTask.getTaskId())).thenReturn(Optional.of(mockTask));

        TaskDTO taskDTO = taskService.findById(mockTask.getTaskId());

        assertNotNull(taskDTO);
        assertEquals(mockTask.getTaskId(), taskDTO.getTaskId());
        assertEquals(mockTask.getDescription(), taskDTO.getDescription());
        assertEquals(mockTask.getTaskDifficulty(), taskDTO.getTaskDifficulty());

        verify(taskRepository, times(1)).findById(mockTask.getTaskId());
    }

    @Test
    @DisplayName("Given an invalid taskId, when finding a task by id, then throw TaskNotFoundException")
    void givenInvalidTaskId_whenFindingTaskById_thenThrowTaskNotFoundException() {
        Long invalidTaskId = 100L;
        when(taskRepository.findById(invalidTaskId)).thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class, () -> taskService.findById(invalidTaskId)
        );

        String expecteMessage = TASK_NOT_FOUND;
        assertEquals(expecteMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Given valid TaskUpdateRequest, when updating a task, then return updated TaskDTO successfully")
    void givenValidTaskUpdateRequest_whenUpdatingTask_thenReturnUpdatedTaskDTOSuccessfully() {
        Long taskId = 1L;
        TaskUpdateRequest mockValidTaskUpdateRequest = getMockValidTaskUpdateRequest();
        Task mockTask = getMockTask();
        Developer mockDeveloper = getMockDeveloper();

        when(developerService.findByDeveloperId(mockValidTaskUpdateRequest.getDeveloperId())).thenReturn(mockDeveloper);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        TaskDTO updatedTaskDTO = taskService.update(taskId, mockValidTaskUpdateRequest);

        assertNotNull(updatedTaskDTO);
        assertEquals(mockValidTaskUpdateRequest.getDescription(), updatedTaskDTO.getDescription());
        assertEquals(mockValidTaskUpdateRequest.getStatus(), updatedTaskDTO.getStatus());
        assertEquals(mockValidTaskUpdateRequest.getTaskDifficulty(), updatedTaskDTO.getTaskDifficulty());

    }

    @Test
    @DisplayName("Given invalid TaskUpdateRequest, when updating a task, then throw exception")
    void givenInvalidTaskUpdateRequest_whenUpdatingTask_thenThrowException() {
        Long taskId = 1L;
        TaskUpdateRequest invalidTaskUpdateRequest = getMockInvalidTaskUpdateRequest();

        when(developerService.findByDeveloperId(invalidTaskUpdateRequest.getDeveloperId()))
                .thenThrow(new DeveloperNotFoundException("Developer not found"));

        assertThrows(DeveloperNotFoundException.class, () -> taskService.update(taskId, invalidTaskUpdateRequest));
    }

    private TaskUpdateRequest getMockValidTaskUpdateRequest() {
        return TaskUpdateRequest.builder()
                .description("Updated Test description")
                .status(TaskStatus.COMPLETED)
                .developerId(1L)
                .taskDifficulty(Difficulty.HIGH)
                .deadlineDays(5)
                .build();
    }

    private TaskUpdateRequest getMockInvalidTaskUpdateRequest() {
        return TaskUpdateRequest.builder()
                .description("Invalid Test description")
                .status(TaskStatus.IN_PROGRESS)
                .taskDifficulty(Difficulty.LOW)
                .deadlineDays(8)
                .build();
    }

    private TaskRequest getMockTaskRequest() {
        return TaskRequest.builder()
                .description("Test description")
                .status(TaskStatus.PENDING)
                .developerId(1L)
                .projectId(1L)
                .taskDifficulty(Difficulty.MEDIUM)
                .deadlineDays(10)
                .creationDate(OffsetDateTime.now())
                .updateDate(OffsetDateTime.now())
                .build();
    }

    private TaskRequest getMockInvalidTaskRequest() {
        return TaskRequest.builder()
                .description("Test description")
                .status(TaskStatus.PENDING)
                .developerId(null)
                .projectId(null)
                .taskDifficulty(Difficulty.MEDIUM)
                .deadlineDays(10)
                .creationDate(OffsetDateTime.now())
                .updateDate(OffsetDateTime.now())
                .build();
    }

    private Task getMockTask() {
        return Task.builder()
                .description("Test description")
                .status(TaskStatus.PENDING)
                .developer(getMockDeveloper())
                .project(getMockProject())
                .taskDifficulty(Difficulty.MEDIUM)
                .deadlineDays(10)
                .creationDate(OffsetDateTime.now())
                .updateDate(OffsetDateTime.now())
                .build();
    }

    private Project getMockProject() {
        return Project.builder()
                .projectId(1L)
                .name("Sample Project")
                .status(ProjectStatus.IN_PROGRESS)
                .company(getMockCompany())
                .build();
    }

    private Developer getMockDeveloper() {
        return Developer.builder()
                .developerId(1L)
                .name("Joel Maciel")
                .company(getMockCompany())
                .experienceLevel(ExperienceLevel.PLENO)
                .position("FullStack")
                .yearsOfExperience(8)
                .phoneNumber("85999898989")
                .age(35)
                .build();
    }

    private Company getMockCompany() {
        return Company.builder()
                .companyId(1L)
                .name("IBM")
                .build();
    }
}