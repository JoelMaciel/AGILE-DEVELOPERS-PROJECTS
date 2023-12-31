package com.joelmaciel.agiledevprojects.domain.services;

import com.joelmaciel.agiledevprojects.api.dtos.CompanyDTO;
import com.joelmaciel.agiledevprojects.api.dtos.CompanyRequest;

public interface CompanyService {
    CompanyDTO save(CompanyRequest companyRequest);
}
