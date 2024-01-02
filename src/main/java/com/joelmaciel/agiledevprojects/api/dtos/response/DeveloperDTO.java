package com.joelmaciel.agiledevprojects.api.dtos.response;

import com.joelmaciel.agiledevprojects.domain.entities.Developer;
import com.joelmaciel.agiledevprojects.domain.enums.ExperienceLevel;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperDTO {
    private Long developerId;
    private String name;
    private String companyName;
    private String position;
    private ExperienceLevel experienceLevel;
    private Integer yearsOfExperience;
    private String phoneNumber;
    private Integer age;

    public static DeveloperDTO toDTO(Developer developer) {
        return DeveloperDTO.builder()
                .developerId(developer.getDeveloperId())
                .name(developer.getName())
                .phoneNumber(developer.getPhoneNumber())
                .age(developer.getAge())
                .position(developer.getPosition())
                .experienceLevel(developer.getExperienceLevel())
                .yearsOfExperience(developer.getYearsOfExperience())
                .companyName(developer.getCompany().getName())
                .build();
    }
}
