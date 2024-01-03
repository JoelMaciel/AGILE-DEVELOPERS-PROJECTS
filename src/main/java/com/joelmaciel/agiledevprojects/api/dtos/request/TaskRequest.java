package com.joelmaciel.agiledevprojects.api.dtos.request;

import com.joelmaciel.agiledevprojects.domain.entities.Task;
import com.joelmaciel.agiledevprojects.domain.enums.Difficulty;
import com.joelmaciel.agiledevprojects.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {

    @NotBlank
    private String description;
    @NotNull
    private TaskStatus status;
    @NotNull
    private Long developerId;
    @NotNull
    private Long projectId;
    @NotNull
    private Difficulty taskDifficulty;
    @NotNull
    private Integer deadlineDays;
    private OffsetDateTime creationDate;
    private OffsetDateTime updateDate;

    public static Task toEntity(TaskRequest request) {
        return Task.builder()
                .description(request.getDescription())
                .status(request.getStatus())
                .taskDifficulty(request.getTaskDifficulty())
                .deadlineDays(request.deadlineDays)
                .creationDate(OffsetDateTime.now())
                .updateDate(OffsetDateTime.now())
                .build();
    }
}
