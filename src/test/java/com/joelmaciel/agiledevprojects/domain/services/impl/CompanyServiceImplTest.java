package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.CompanyDTO;
import com.joelmaciel.agiledevprojects.api.dtos.CompanyRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.exception.CompanyNotFoundException;
import com.joelmaciel.agiledevprojects.domain.repositories.CompanyRepository;
import jakarta.validation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    public static final String AND_TRY_AGAIN = "One or more fields are invalid. Fill in the correct form and try again.";
    public static final String COMPANY_NOT_FOUND = "There is no company with this id 999 saved in the database";
    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private CompanyServiceImpl companyService;

    @Test
    @DisplayName("Given valid pageable and optional name, when finding all companies, then a Page of CompanyDTOs is returned")
    void givenValidPageableAndOptionalName_whenFindingAllCompanies_thenReturnPageOfCompanyDTOs() {
        Pageable pageable = mock(Pageable.class);
        String name = "Facebook";

        Company mockCompany = getMockCompany();
        Page<Company> companyPage = new PageImpl<>(Collections.singletonList(mockCompany));

        when(companyRepository.findByNameContaining(pageable, name)).thenReturn(companyPage);

        Page<CompanyDTO> resultPage = companyService.findAll(pageable, name);

        verify(companyRepository, times(1)).findByNameContaining(pageable, name);
        verify(companyRepository, never()).findAll(pageable);

        assertNotNull(resultPage);
        assertEquals(
                getMockCompanyDTOPage().getContent().get(0).getCreationDate().withNano(0),
                resultPage.getContent().get(0).getCreationDate().withNano(0)
        );
    }

    @Test
    @DisplayName("Given valid pageable and no name, when finding all companies, then a Page of all CompanyDTOs is returned")
    void givenValidPageableAndNoName_whenFindingAllCompanies_thenReturnPageOfAllCompanyDTOs() {
        Pageable pageable = mock(Pageable.class);
        String name = null;

        Company mockCompany = getMockCompany();
        Company mockCompanyTwo = getMockCompanyTwo();
        List<Company> companyList = Arrays.asList(mockCompany, mockCompanyTwo);
        Page<Company> companyPage = new PageImpl<>(companyList);

        when(companyRepository.findAll(pageable)).thenReturn(companyPage);

        Page<CompanyDTO> resultPage = companyService.findAll(pageable, name);

        verify(companyRepository, never()).findByNameContaining(ArgumentMatchers.any(), ArgumentMatchers.anyString());
        verify(companyRepository, times(1)).findAll(pageable);

        assertNotNull(resultPage);
        assertEquals(2, resultPage.getTotalElements());
        assertEquals(mockCompany.getName(), resultPage.getContent().get(0).getName());
        assertEquals(mockCompanyTwo.getName(), resultPage.getContent().get(1).getName());
    }

    @DisplayName("Given a valid CompanyRequest, when saving a company, then a CompanyDTO with correct data is returned")
    @Test
    void givenValidCompanyRequest_whenSavingCompany_thenReturnCompanyDTOSuccessfully() {
        CompanyRequest companyRequest = getMockCompanyRequest();
        Company mockCompany = CompanyRequest.toEntity(companyRequest);

        when(companyRepository.save(any(Company.class))).thenReturn(mockCompany);
        CompanyDTO companyDTO = companyService.save(companyRequest);

        verify(companyRepository, times(1)).save(any(Company.class));
        assertNotNull(companyDTO);
        assertEquals(companyDTO.getName(), companyRequest.getName());

    }

    @DisplayName("Given a CompanyRequest without a name, when saving a company, then an exception is thrown")
    @Test
    void givenCompanyRequestWithoutName_whenSavingCompany_thenExceptionIsThrown() {
        CompanyRequest companyRequest = getMockCompanyRequest();
        companyRequest.setName(null);

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<CompanyRequest>> violations = validator.validate(companyRequest);

        when(companyRepository.save(any(Company.class)))
                .thenThrow(new ConstraintViolationException(AND_TRY_AGAIN, violations));

        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class, () -> companyService.save(companyRequest)
        );

        assertEquals(AND_TRY_AGAIN, exception.getMessage());
        assertNotNull(exception.getConstraintViolations());

        assertEquals(1, exception.getConstraintViolations().size());

        verify(companyRepository, times(1)).save(any(Company.class));
        validatorFactory.close();
    }

    @DisplayName("Given a valid CompanyId, when finding a company, then the correct CompanyDTO is returned")
    @Test
    void givenValidCompanyId_whenFindingCompany_thenReturnCorrectCompanyDTO() {
        Company company = getMockCompany();
        when(companyRepository.findById(company.getCompanyId())).thenReturn(Optional.of(company));

        CompanyDTO companyDTO = companyService.findById(company.getCompanyId());

        verify(companyRepository, times(1)).findById(company.getCompanyId());
        assertNotNull(companyDTO);
        assertEquals(company.getName(), companyDTO.getName());
        assertEquals(company.getCreationDate(), companyDTO.getCreationDate());

    }

    @DisplayName("Given an invalid CompanyId, when finding a company, then a CompanyNotFoundException is thrown")
    @Test
    void givenInvalidCompanyId_whenFindingCompany_thenThrowCompanyNotFoundException() {
        Long invalidCompanyId = 999L;
        when(companyRepository.findById(invalidCompanyId)).thenReturn(Optional.empty());

        CompanyNotFoundException exception = assertThrows(CompanyNotFoundException.class,
                () -> companyService.findById(invalidCompanyId));

        assertEquals(COMPANY_NOT_FOUND, exception.getMessage());
        verify(companyRepository, times(1)).findById(invalidCompanyId);

    }

    @Test
    @DisplayName("Given a valid companyId, when deleting a company, then it should delete successfully")
    void givenValidCompanyId_whenDeletingCompany_thenDeleteSuccessfully() {
        Company company = getMockCompany();

        when(companyRepository.findById(company.getCompanyId())).thenReturn(Optional.of(company));

        assertDoesNotThrow(() -> companyService.delete(company.getCompanyId()));
        verify(companyRepository, times(1)).findById(company.getCompanyId());
        verify(companyRepository, times(1)).delete(company);

    }


    @Test
    @DisplayName("Given an invalid companyId, when deleting a company, then throw CompanyNotFoundException")
    void givenInvalidCompanyId_whenDeletingCompany_thenThrowCompanyNotFoundException() {
        Long companyId = 999L;

        when(companyRepository.findById(companyId)).thenReturn(Optional.empty());

        CompanyNotFoundException exception = assertThrows(
                CompanyNotFoundException.class,
                () -> companyService.delete(companyId));

        assertEquals(CompanyServiceImplTest.COMPANY_NOT_FOUND, exception.getMessage());
        verify(companyRepository, times(1)).findById(companyId);
        verify(companyRepository, never()).delete(any(Company.class));

    }

    private Company getMockCompany() {
        return Company.builder()
                .companyId(1L)
                .name("Adobe")
                .creationDate(OffsetDateTime.now())
                .build();
    }

    private Company getMockCompanyTwo() {
        return Company.builder()
                .companyId(2L)
                .name("Facebook")
                .creationDate(OffsetDateTime.now())
                .build();
    }

    private CompanyRequest getMockCompanyRequest() {
        return CompanyRequest.builder()
                .name("Facebook")
                .build();
    }

    private Page<CompanyDTO> getMockCompanyDTOPage() {
        Company company = getMockCompany();
        List<CompanyDTO> companyDTOList = Collections.singletonList(getMockCompanyDTO(company));
        return new PageImpl<>(companyDTOList);
    }

    private CompanyDTO getMockCompanyDTO(Company company) {
        return CompanyDTO.builder()
                .companyId(company.getCompanyId())
                .name(company.getName())
                .creationDate(company.getCreationDate())
                .build();
    }

}