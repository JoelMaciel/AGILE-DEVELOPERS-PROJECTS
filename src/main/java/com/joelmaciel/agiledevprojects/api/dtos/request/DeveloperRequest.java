package com.joelmaciel.agiledevprojects.api.dtos.request;

import com.joelmaciel.agiledevprojects.domain.entities.Developer;
import com.joelmaciel.agiledevprojects.domain.enums.ExperienceLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeveloperRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String phoneNumber;
    @NotNull
    private Integer age;
    @NotBlank
    private String position;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ExperienceLevel experienceLevel;

    @NotNull
    private Integer yearsOfExperience;

    @NotNull
    private Long companyId;

    public static Developer toEntity(DeveloperRequest request) {
        return Developer.builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .age(request.getAge())
                .position(request.getPosition())
                .experienceLevel(request.getExperienceLevel())
                .yearsOfExperience(request.getYearsOfExperience())
                .build();
    }
}
