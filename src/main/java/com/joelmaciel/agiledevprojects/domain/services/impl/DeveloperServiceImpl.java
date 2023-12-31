package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.DeveloperDTO;
import com.joelmaciel.agiledevprojects.api.dtos.DeveloperRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.entities.Developer;
import com.joelmaciel.agiledevprojects.domain.exception.DeveloperNotFoundException;
import com.joelmaciel.agiledevprojects.domain.repositories.DeveloperRepository;
import com.joelmaciel.agiledevprojects.domain.services.CompanyService;
import com.joelmaciel.agiledevprojects.domain.services.DeveloperService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {
    private final DeveloperRepository developerRepository;
    private final CompanyService companyService;

    @Override
    public DeveloperDTO save(DeveloperRequest developerRequest) {
        Company company = companyService.findByCompanyId(developerRequest.getCompanyId());
        Developer developer = DeveloperRequest.toEntity(developerRequest);
        developer.setCompany(company);
        return DeveloperDTO.toDTO(developerRepository.save(developer));
    }

    @Override
    public DeveloperDTO findById(Long developerId) {
        Developer developer = findByDeveloperId(developerId);
        return DeveloperDTO.toDTO(developer);
    }

    @Override
    public Developer findByDeveloperId(Long developerId) {
        return developerRepository.findById(developerId)
                .orElseThrow(() -> new DeveloperNotFoundException(developerId));
    }
}
