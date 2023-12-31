package com.joelmaciel.agiledevprojects.domain.services;

import com.joelmaciel.agiledevprojects.api.dtos.DeveloperDTO;
import com.joelmaciel.agiledevprojects.api.dtos.DeveloperRequest;

public interface DeveloperService {
    DeveloperDTO save(DeveloperRequest developerRequest);
}
