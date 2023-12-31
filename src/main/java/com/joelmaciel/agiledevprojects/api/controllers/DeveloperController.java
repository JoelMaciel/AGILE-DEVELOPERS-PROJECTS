package com.joelmaciel.agiledevprojects.api.controllers;

import com.joelmaciel.agiledevprojects.api.dtos.DeveloperDTO;
import com.joelmaciel.agiledevprojects.api.dtos.DeveloperRequest;
import com.joelmaciel.agiledevprojects.domain.services.DeveloperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/developers")
public class DeveloperController {
    private final DeveloperService developerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperDTO save(@RequestBody @Valid DeveloperRequest developerRequest) {
        return developerService.save(developerRequest);
    }
}
