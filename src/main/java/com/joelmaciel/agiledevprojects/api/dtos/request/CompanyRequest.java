package com.joelmaciel.agiledevprojects.api.dtos.request;

import com.joelmaciel.agiledevprojects.domain.entities.Company;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequest {

    @NotBlank
    private String name;

    public static Company toEntity(CompanyRequest companyRequest) {
        return Company.builder()
                .name(companyRequest.getName())
                .build();
    }

}
