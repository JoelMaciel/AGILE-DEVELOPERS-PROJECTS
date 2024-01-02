package com.joelmaciel.agiledevprojects.api.dtos.request;

import com.joelmaciel.agiledevprojects.domain.enums.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectUpdateRequest {

    @NotBlank
    private String name;
    @NotNull
    private ProjectStatus status;

    private OffsetDateTime updateDate;

}
