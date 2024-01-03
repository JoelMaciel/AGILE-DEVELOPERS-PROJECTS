package com.joelmaciel.agiledevprojects.domain.repositories;

import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CompanyRepositoryTest {

    @Autowired
    private CompanyRepository companyRepository;

    @Test
    @DisplayName("Given Company RequestValid WhenSave Then Return Saved Company")
    void givenCompanyRequestValid_WhenSave_ThenReturnSavedCompany() {
        Company company = TestUtils.createCompany("Company Test");
        Company newCompany = companyRepository.save(company);

        assertNotNull(company);
        assertTrue(company.getCompanyId() > 0);
        assertEquals(company.getName(), newCompany.getName());
    }

    @Test
    @DisplayName("Given List of Companies When Get All Then Return Page of CompanyDTO")
    void givenListOfCompanies_WhenGetAll_ThenReturnPageOfCompany() {
        Company company = TestUtils.createCompany("Company Test");
        Company companyTwo = TestUtils.createCompany("Company Test Two");

        companyRepository.save(company);
        companyRepository.save(companyTwo);
        List<Company> companies = companyRepository.findAll();

        assertNotNull(companies);
        assertEquals(2, companies.size());
        assertEquals("Company Test", companies.get(0).getName());
        assertEquals("Company Test Two", companies.get(1).getName());
    }

    @Test
    @DisplayName("Given CompanyId Valid When FindById Then Return Company")
    void givenCompanyIdValid_WhenFindById_ThenReturnCompany() {
        Company company = TestUtils.createCompany("Company Test");
        companyRepository.save(company);

        Optional<Company> savedCompany = companyRepository.findById(company.getCompanyId());

        assertNotNull(savedCompany);
        assertEquals(company.getCompanyId(), savedCompany.get().getCompanyId());
        assertEquals("Company Test", savedCompany.get().getName());
    }

    @Test
    @DisplayName("Given CompanyId Valid When Update Company Then Return Updated Company")
    void givenCompanyIdValid_WhenUpdate_ThenReturnUpdatedCompany() {
        Company company = TestUtils.createCompany("Company Test");
        companyRepository.save(company);

        Optional<Company> savedCompanyOptional = companyRepository.findById(company.getCompanyId());
        assertTrue(savedCompanyOptional.isPresent());

        Company savedCompany = savedCompanyOptional.get();
        savedCompany.setName("Maciel");

        Company updatedCompany = companyRepository.save(savedCompany);

        assertNotNull(updatedCompany);
        assertEquals(company.getCompanyId(), updatedCompany.getCompanyId());
        assertEquals("Maciel", updatedCompany.getName());
    }
}