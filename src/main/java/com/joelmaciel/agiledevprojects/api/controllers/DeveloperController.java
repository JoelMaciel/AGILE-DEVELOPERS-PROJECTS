package com.joelmaciel.agiledevprojects.api.controllers;

import com.joelmaciel.agiledevprojects.api.dtos.DeveloperDTO;
import com.joelmaciel.agiledevprojects.api.dtos.DeveloperRequest;
import com.joelmaciel.agiledevprojects.domain.services.DeveloperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/developers")
public class DeveloperController {

    private final DeveloperService developerService;

    @GetMapping
    public Page<DeveloperDTO> getAll(
            @PageableDefault(page = 0, size = 10, sort = "developerId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String position
    ) {
        return developerService.findAll(pageable, name, position);
    }

    @GetMapping("/{developerId}")
    public DeveloperDTO getOne(@PathVariable Long developerId) {
        return developerService.findById(developerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeveloperDTO save(@RequestBody @Valid DeveloperRequest developerRequest) {
        return developerService.save(developerRequest);
    }
}
