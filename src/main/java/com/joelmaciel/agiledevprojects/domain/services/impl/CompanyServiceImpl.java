package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.CompanyDTO;
import com.joelmaciel.agiledevprojects.api.dtos.CompanyRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.exception.CompanyNotFoundException;
import com.joelmaciel.agiledevprojects.domain.repositories.CompanyRepository;
import com.joelmaciel.agiledevprojects.domain.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CompanyDTO> findAll(Pageable pageable, String name) {
        Page<Company> companies;
        if (name != null) {
            companies = companyRepository.findByNameContaining(pageable, name);
        } else {
            companies = companyRepository.findAll(pageable);
        }
        return companies.map(CompanyDTO::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDTO findById(Long companyId) {
        Company company = findByCompanyId(companyId);
        return CompanyDTO.toDTO(company);
    }

    @Override
    @Transactional
    public CompanyDTO save(CompanyRequest companyRequest) {
        Company company = CompanyRequest.toEntity(companyRequest);
        return CompanyDTO.toDTO(companyRepository.save(company));
    }

    @Override
    @Transactional
    public void delete(Long companyId) {
        Company company = findByCompanyId(companyId);
        companyRepository.delete(company);
    }

    @Override
    @Transactional(readOnly = true)
    public Company findByCompanyId(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException(companyId));
    }
}
