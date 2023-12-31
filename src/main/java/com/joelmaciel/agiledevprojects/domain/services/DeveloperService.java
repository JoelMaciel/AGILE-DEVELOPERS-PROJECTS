package com.joelmaciel.agiledevprojects.domain.services;

import com.joelmaciel.agiledevprojects.api.dtos.DeveloperDTO;
import com.joelmaciel.agiledevprojects.api.dtos.DeveloperRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Developer;

public interface DeveloperService {
    DeveloperDTO save(DeveloperRequest developerRequest);

    DeveloperDTO findById(Long developerId);
    Developer findByDeveloperId(Long developerId);
}
