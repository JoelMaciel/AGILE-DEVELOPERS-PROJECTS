package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.DeveloperDTO;
import com.joelmaciel.agiledevprojects.api.dtos.DeveloperRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.entities.Developer;
import com.joelmaciel.agiledevprojects.domain.enums.ExperienceLevel;
import com.joelmaciel.agiledevprojects.domain.repositories.DeveloperRepository;
import com.joelmaciel.agiledevprojects.domain.services.CompanyService;
import jakarta.validation.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceImplTest {

    public static final String NAME_IS_REQUIRED = "Name is required";
    @Mock
    private CompanyService companyService;
    @Mock
    private DeveloperRepository developerRepository;

    @InjectMocks
    private DeveloperServiceImpl developerService;

    @Test
    @DisplayName("Given valid DeveloperRequest, when saving a developer, then return DeveloperDTO successfully")
    void givenValidDeveloperRequest_whenSavingDeveloper_thenReturnDeveloperDTOSuccessfully() {
        DeveloperRequest mockDeveloperRequest = getMockDeveloperRequest();
        Company mockCompany = getMockCompany();
        Developer developer = DeveloperRequest.toEntity(mockDeveloperRequest);
        developer.setCompany(mockCompany);

        when(companyService.findByCompanyId(mockDeveloperRequest.getCompanyId())).thenReturn(mockCompany);
        when(developerRepository.save(any(Developer.class))).thenReturn(developer);

        DeveloperDTO developerDTO = developerService.save(mockDeveloperRequest);

        assertNotNull(developerDTO);
        assertEquals(mockDeveloperRequest.getName(), developerDTO.getName());
        assertEquals(mockDeveloperRequest.getPhoneNumber(), developerDTO.getPhoneNumber());
        assertEquals(mockDeveloperRequest.getExperienceLevel(), developerDTO.getExperienceLevel());
        assertEquals(mockDeveloperRequest.getPosition(), developerDTO.getPosition());
        assertEquals(mockDeveloperRequest.getAge(), developerDTO.getAge());

        verify(developerRepository, times(1)).save(any(Developer.class));
        verify(companyService, times(1)).findByCompanyId(anyLong());
    }


    @Test
    @DisplayName("Given DeveloperRequest without a name, when saving a developer, then throw exception")
    void givenDeveloperRequestWithoutName_whenSavingDeveloper_thenThrowException() {
        DeveloperRequest mockDeveloperRequest = getMockDeveloperRequest();
        mockDeveloperRequest.setName(null);

        Company mockCompany = getMockCompany();
        when(companyService.findByCompanyId(mockDeveloperRequest.getCompanyId())).thenReturn(mockCompany);

        when(developerRepository.save(argThat(developer -> developer.getName() == null)))
                .thenThrow(new ConstraintViolationException(NAME_IS_REQUIRED, Set.of()));

        ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> developerService.save(mockDeveloperRequest)
        );

        assertTrue(exception.getMessage().contains("Name is required"));
        verify(developerRepository, times(1)).save(any(Developer.class));
        verify(companyService, times(1)).findByCompanyId(anyLong());

    }

    private DeveloperRequest getMockDeveloperRequest() {
        return DeveloperRequest.builder()
                .name("Joel Maciel")
                .companyId(1L)
                .experienceLevel(ExperienceLevel.PLENO)
                .position("FullStack")
                .yearsOfExperience(8)
                .phoneNumber("85999898989")
                .age(35)
                .build();
    }

    private Company getMockCompany() {
        return Company.builder()
                .companyId(1L)
                .name("IBM")
                .build();
    }
}