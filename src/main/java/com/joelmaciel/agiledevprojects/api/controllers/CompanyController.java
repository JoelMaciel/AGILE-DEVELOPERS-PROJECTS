package com.joelmaciel.agiledevprojects.api.controllers;

import com.joelmaciel.agiledevprojects.api.dtos.CompanyDTO;
import com.joelmaciel.agiledevprojects.api.dtos.CompanyRequest;
import com.joelmaciel.agiledevprojects.domain.services.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDTO save(@RequestBody @Valid CompanyRequest companyRequest) {
        return companyService.save(companyRequest);
    }
}
