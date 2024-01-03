package com.joelmaciel.agiledevprojects.api.dtos.request;

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
public class TaskUpdateRequest {

    @NotBlank
    private String description;
    @NotNull
    private TaskStatus status;
    @NotNull
    private Long developerId;
    @NotNull
    private Difficulty taskDifficulty;
    @NotNull
    private Integer deadlineDays;
    private OffsetDateTime updateDate;

}
