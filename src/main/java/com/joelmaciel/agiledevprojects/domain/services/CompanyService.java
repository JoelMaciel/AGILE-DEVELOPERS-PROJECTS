package com.joelmaciel.agiledevprojects.domain.services;

import com.joelmaciel.agiledevprojects.api.dtos.response.CompanyDTO;
import com.joelmaciel.agiledevprojects.api.dtos.request.CompanyRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {
    CompanyDTO save(CompanyRequest companyRequest);

    CompanyDTO findById(Long companyId);

    Company findByCompanyId(Long companyId);

    Page<CompanyDTO> findAll(Pageable pageable, String name);

    void delete(Long companyId);
}
