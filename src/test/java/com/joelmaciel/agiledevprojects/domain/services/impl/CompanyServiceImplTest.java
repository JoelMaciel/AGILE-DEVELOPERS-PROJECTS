package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.CompanyDTO;
import com.joelmaciel.agiledevprojects.api.dtos.CompanyRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.repositories.CompanyRepository;
import jakarta.validation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    public static final String AND_TRY_AGAIN = "One or more fields are invalid. Fill in the correct form and try again.";
    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private CompanyServiceImpl companyService;

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

    private CompanyRequest getMockCompanyRequest() {
        return CompanyRequest.builder()
                .name("Facebook")
                .build();
    }

}