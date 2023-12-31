package com.joelmaciel.agiledevprojects.api.controllers;

import com.joelmaciel.agiledevprojects.api.dtos.CompanyDTO;
import com.joelmaciel.agiledevprojects.api.dtos.CompanyRequest;
import com.joelmaciel.agiledevprojects.domain.services.CompanyService;
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
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping
    public Page<CompanyDTO> getAll(
            @PageableDefault(page = 0, size = 10, sort = "companyId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String name
    ) {
        return companyService.findAll(pageable, name);
    }

    @GetMapping("/{companyId}")
    public CompanyDTO getOne(@PathVariable Long companyId) {
        return companyService.findById(companyId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyDTO save(@RequestBody @Valid CompanyRequest companyRequest) {
        return companyService.save(companyRequest);
    }

    @DeleteMapping("/{companyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long companyId) {
        companyService.delete(companyId);
    }
}
