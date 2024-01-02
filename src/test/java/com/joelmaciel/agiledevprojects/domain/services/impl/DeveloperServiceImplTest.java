package com.joelmaciel.agiledevprojects.domain.services.impl;

import com.joelmaciel.agiledevprojects.api.dtos.response.DeveloperDTO;
import com.joelmaciel.agiledevprojects.api.dtos.request.DeveloperRequest;
import com.joelmaciel.agiledevprojects.domain.entities.Company;
import com.joelmaciel.agiledevprojects.domain.entities.Developer;
import com.joelmaciel.agiledevprojects.domain.enums.ExperienceLevel;
import com.joelmaciel.agiledevprojects.domain.exception.DeveloperNotFoundException;
import com.joelmaciel.agiledevprojects.domain.repositories.DeveloperRepository;
import com.joelmaciel.agiledevprojects.domain.services.CompanyService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeveloperServiceImplTest {

    public static final String NAME_IS_REQUIRED = "Name is required";
    public static final String DEVELOPER_NOT_FOUND = "There is no developer with this id 999 saved in the database";
    @Mock
    private CompanyService companyService;
    @Mock
    private DeveloperRepository developerRepository;
    @InjectMocks
    private DeveloperServiceImpl developerService;

    @Test
    @DisplayName("Given valid DeveloperRequest and pageable, when saving a developer, then return DeveloperDTO successfully")
    void givenValidDeveloperRequestAndPageable_whenSavingDeveloper_thenReturnDeveloperDTOSuccessfully() {
        String name = "Joel Maciel";
        Pageable pageable = PageRequest.of(0, 10);

        Developer mockDeveloper = getMockDeveloper();

        List<Developer> developerList = Collections.singletonList(mockDeveloper);
        when(developerRepository.findByNameContaining(pageable, name)).thenReturn(new PageImpl<>(developerList));

        Page<DeveloperDTO> developerDTOPage = developerService.findAll(pageable, name, null);

        assertNotNull(developerDTOPage);
        assertEquals(1, developerDTOPage.getSize());
        assertEquals(name, developerDTOPage.getContent().get(0).getName());

        verify(developerRepository, times(1)).findByNameContaining(pageable, name);
    }

    @Test
    @DisplayName("Given position, when finding all developers, then return DeveloperDTOs successfully")
    void givenPosition_whenFindingAllDevelopers_thenReturnDeveloperDTOSuccessfully() {
        String position = "FullStack";
        PageRequest pageable = PageRequest.of(0, 10);

        Developer mockDeveloper = getMockDeveloper();
        List<Developer> developerList = Collections.singletonList(mockDeveloper);

        when(developerRepository.findByPositionContaining(pageable, position)).thenReturn(new PageImpl<>(developerList));

        Page<DeveloperDTO> developerDTOPage = developerService.findAll(pageable, null, position);
        assertNotNull(developerDTOPage);
        assertEquals(position, developerDTOPage.getContent().get(0).getPosition());
        assertEquals(1, developerDTOPage.getSize());
    }

    @Test
    @DisplayName("Given no name or position, when finding all developers, then return DeveloperDTOs successfully")
    void givenNoNameOrPosition_whenFindingAllDevelopers_thenReturnDeveloperDTOSuccessfully() {
        PageRequest pageable = PageRequest.of(0, 10);
        Developer mockDeveloper = getMockDeveloper();

        List<Developer> developerList = Collections.singletonList(mockDeveloper);
        when(developerRepository.findAll(pageable)).thenReturn(new PageImpl<>(developerList));

        Page<DeveloperDTO> developerDTOPage = developerService.findAll(pageable, null, null);

        assertNotNull(developerDTOPage);
        assertEquals(1, developerDTOPage.getSize());
        verify(developerRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Given valid DeveloperRequest, when updating a developer, then return DeveloperDTO successfully")
    void givenValidDeveloperRequest_whenUpdatingDeveloper_thenReturnDeveloperDTOSuccessfully() {
        DeveloperRequest mockDeveloperRequest = getMockDeveloperRequest();
        Developer mockDeveloper = getMockDeveloper();

        mockDeveloper = buildUpdatedDeveloper(mockDeveloper, mockDeveloperRequest);

        when(developerRepository.findById(mockDeveloper.getDeveloperId())).thenReturn(Optional.of(mockDeveloper));
        when(developerRepository.save(any(Developer.class))).thenReturn(mockDeveloper);

        DeveloperDTO developerDTO = developerService.update(mockDeveloper.getDeveloperId(), mockDeveloperRequest);

        assertNotNull(developerDTO);
        assertEquals(mockDeveloperRequest.getName(), developerDTO.getName());
        assertEquals(mockDeveloperRequest.getPhoneNumber(), developerDTO.getPhoneNumber());
        assertEquals(mockDeveloperRequest.getAge(), developerDTO.getAge());
        assertEquals(mockDeveloperRequest.getPosition(), developerDTO.getPosition());
        assertEquals(mockDeveloperRequest.getExperienceLevel(), developerDTO.getExperienceLevel());
        assertEquals(mockDeveloperRequest.getYearsOfExperience(), developerDTO.getYearsOfExperience());

        verify(developerRepository, times(1)).findById(mockDeveloper.getDeveloperId());
        verify(developerRepository, times(1)).save(any(Developer.class));
    }

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

    @Test
    @DisplayName("Given valid developerId, when finding a developer by id, then return DeveloperDTO successfully")
    void givenValidDeveloperId_whenFindingDeveloperById_thenReturnDeveloperDTOSuccessfully() {
        Developer mockDeveloper = getMockDeveloper();

        when(developerRepository.findById(mockDeveloper.getDeveloperId())).thenReturn(Optional.of(mockDeveloper));
        DeveloperDTO developerDTO = developerService.findById(mockDeveloper.getDeveloperId());

        assertNotNull(developerDTO);
        assertEquals(mockDeveloper.getDeveloperId(), developerDTO.getDeveloperId());
        assertEquals(mockDeveloper.getName(), developerDTO.getName());
        assertEquals(mockDeveloper.getPosition(), developerDTO.getPosition());
        assertEquals(mockDeveloper.getAge(), developerDTO.getAge());
        verify(developerRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Given invalid developerId, when finding a developer by id, then throw DeveloperNotFoundException")
    void givenInvalidDeveloperId_whenFindingDeveloperById_thenThrowDeveloperNotFoundException() {
        Long invalidDeveloperId = 999L;
        when(developerRepository.findById(invalidDeveloperId)).thenReturn(Optional.empty());

        DeveloperNotFoundException exception = assertThrows(DeveloperNotFoundException.class,
                () -> developerService.findById(invalidDeveloperId)
        );

        assertEquals(exception.getMessage(), DEVELOPER_NOT_FOUND);
        assertTrue(exception.getMessage().contains(String.valueOf(invalidDeveloperId)));
        verify(developerRepository, times(1)).findById(invalidDeveloperId);
    }

    private static Developer buildUpdatedDeveloper(Developer mockDeveloper, DeveloperRequest mockDeveloperRequest) {
        mockDeveloper = mockDeveloper.toBuilder()
                .name(mockDeveloperRequest.getName())
                .phoneNumber(mockDeveloperRequest.getPhoneNumber())
                .age(mockDeveloperRequest.getAge())
                .position(mockDeveloperRequest.getPosition())
                .experienceLevel(mockDeveloperRequest.getExperienceLevel())
                .yearsOfExperience(mockDeveloperRequest.getYearsOfExperience())
                .company(mockDeveloper.getCompany())
                .build();
        return mockDeveloper;
    }

    private DeveloperRequest getMockDeveloperRequest() {
        return DeveloperRequest.builder()
                .name("Maciel Viana")
                .companyId(2L)
                .experienceLevel(ExperienceLevel.INTERN)
                .position("FrontEnd")
                .yearsOfExperience(1)
                .phoneNumber("8599777733")
                .age(21)
                .build();
    }

    private Developer getMockDeveloper() {
        return Developer.builder()
                .developerId(1L)
                .name("Joel Maciel")
                .company(getMockCompany())
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