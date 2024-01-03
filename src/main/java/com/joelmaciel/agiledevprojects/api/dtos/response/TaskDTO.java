package com.joelmaciel.agiledevprojects.api.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joelmaciel.agiledevprojects.domain.entities.Task;
import com.joelmaciel.agiledevprojects.domain.enums.Difficulty;
import com.joelmaciel.agiledevprojects.domain.enums.TaskStatus;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long taskId;
    private String description;
    private TaskStatus status;
    private String developer;
    private String project;
    private Difficulty taskDifficulty;
    private Integer deadlineDays;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private OffsetDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private OffsetDateTime updateDate;

    public static TaskDTO toDTO(Task task) {
        return TaskDTO.builder()
                .taskId(task.getTaskId())
                .description(task.getDescription())
                .status(task.getStatus())
                .developer(task.getDeveloper().getName())
                .project(task.getProject().getName())
                .taskDifficulty(task.getTaskDifficulty())
                .deadlineDays(task.getDeadlineDays())
                .creationDate(task.getCreationDate())
                .updateDate(task.getUpdateDate())
                .build();
    }
}
