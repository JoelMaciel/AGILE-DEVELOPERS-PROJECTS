package com.joelmaciel.agiledevprojects.api.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joelmaciel.agiledevprojects.domain.entities.Project;
import com.joelmaciel.agiledevprojects.domain.enums.ProjectStatus;
import lombok.*;

import java.time.OffsetDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {

    private Long projectId;
    private String name;
    private ProjectStatus status;
    private String companyName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private OffsetDateTime creationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private OffsetDateTime updateDate;

    public static ProjectDTO toDTO(Project project) {
        return ProjectDTO.builder()
                .projectId(project.getProjectId())
                .name(project.getName())
                .status(project.getStatus())
                .companyName(project.getCompany().getName())
                .creationDate(project.getCreationDate())
                .updateDate(project.getUpdateDate())
                .build();
    }
}
