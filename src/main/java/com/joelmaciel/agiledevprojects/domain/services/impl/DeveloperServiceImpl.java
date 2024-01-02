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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeveloperServiceImpl implements DeveloperService {
    private final DeveloperRepository developerRepository;
    private final CompanyService companyService;

    @Override
    @Transactional(readOnly = true)
    public Page<DeveloperDTO> findAll(Pageable pageable, String name, String position) {
        Page<Developer> developers;
        if (name != null) {
            developers = developerRepository.findByNameContaining(pageable, name);
        } else if (position != null) {
            developers = developerRepository.findByPositionContaining(pageable, position);
        } else {
            developers = developerRepository.findAll(pageable);
        }
        return developers.map(DeveloperDTO::toDTO);
    }

    @Override
    @Transactional
    public DeveloperDTO update(Long developerId, DeveloperRequest developerRequest) {
        Developer developer = findByDeveloperId(developerId);
        Developer updatedDeveloper = mapDeveloperRequestToDeveloper(developerRequest,developer);

        return DeveloperDTO.toDTO(developerRepository.save(updatedDeveloper));
    }

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
    private Developer mapDeveloperRequestToDeveloper(DeveloperRequest developerRequest, Developer developer) {
        return developer.toBuilder()
                .name(developerRequest.getName())
                .phoneNumber(developerRequest.getPhoneNumber())
                .age(developerRequest.getAge())
                .position(developerRequest.getPosition())
                .experienceLevel(developerRequest.getExperienceLevel())
                .yearsOfExperience(developerRequest.getYearsOfExperience())
                .build();
    }
}
