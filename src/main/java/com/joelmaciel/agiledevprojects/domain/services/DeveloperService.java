package com.joelmaciel.agiledevprojects.domain.services;

import com.joelmaciel.agiledevprojects.api.dtos.response.DeveloperDTO;
import com.joelmaciel.agiledevprojects.api.dtos.request.DeveloperRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Developer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeveloperService {
    DeveloperDTO save(DeveloperRequest developerRequest);

    DeveloperDTO findById(Long developerId);

    Developer findByDeveloperId(Long developerId);

    Page<DeveloperDTO> findAll(Pageable pageable, String name, String position);

    DeveloperDTO update(Long developerId, DeveloperRequest developerRequest);
}
