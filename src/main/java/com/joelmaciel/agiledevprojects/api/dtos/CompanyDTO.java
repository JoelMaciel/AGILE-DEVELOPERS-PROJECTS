package com.joelmaciel.agiledevprojects.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    private Long companyId;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    @CreationTimestamp
    private OffsetDateTime creationDate;

    public static CompanyDTO toDTO(Company company) {
        return CompanyDTO.builder()
                .companyId(company.getCompanyId())
                .name(company.getName())
                .creationDate(company.getCreationDate())
                .build();
    }
}
