package com.joelmaciel.agiledevprojects.api.dtos.request;

import com.joelmaciel.agiledevprojects.domain.entities.Project;
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
public class ProjectRequest {

    @NotBlank
    private String name;
    @NotNull
    private ProjectStatus status;
    @NotNull
    private Long companyId;
    private OffsetDateTime creationDate;
    private OffsetDateTime updateDate;

    public static Project toEntity(ProjectRequest request) {
        return Project.builder()
                .name(request.getName())
                .status(request.getStatus())
                .creationDate(OffsetDateTime.now())
                .updateDate(OffsetDateTime.now())
                .build();
    }

}
