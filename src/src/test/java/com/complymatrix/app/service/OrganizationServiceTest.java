package com.complymatrix.app.service;

import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.entity.Organizations;
import com.complymatrix.app.entity.Standards;
import com.complymatrix.app.repository.OrganizationStandardsRepository;
import com.complymatrix.app.repository.OrganizationsRepository;
import com.complymatrix.app.repository.StandardsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationsRepository organizationsRepository;

    @Mock
    private StandardsRepository standardsRepository;

    @Mock
    private OrganizationStandardsRepository organizationStandardsRepository;

    @InjectMocks
    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("linkOrganizationToStandard saves the new OrganizationStandards")
    void linkOrganizationToStandardTest() {
        Organizations org = new Organizations();
        org.setOrganizationId(1L);
        Standards std = new Standards();
        std.setStandardId(2L);

        when(organizationsRepository.findById(1L)).thenReturn(java.util.Optional.of(org));
        when(standardsRepository.findById(2L)).thenReturn(java.util.Optional.of(std));

        organizationService.linkOrganizationToStandard(1L, 2L);

        ArgumentCaptor<OrganizationStandards> captor = ArgumentCaptor.forClass(OrganizationStandards.class);
        verify(organizationStandardsRepository, times(1)).save(captor.capture());
        OrganizationStandards saved = captor.getValue();
        assertEquals(org, saved.getOrganization());
        assertEquals(std, saved.getStandard());
    }

    @Test
    @DisplayName("linkOrganizationToStandard throws if org not found")
    void linkOrgToStdOrgNotFound() {
        when(organizationsRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
        assertThrows(RuntimeException.class, () -> organizationService.linkOrganizationToStandard(1L, 2L));
    }

    @Test
    @DisplayName("linkOrganizationToStandard throws if standard not found")
    void linkOrgToStdStandardNotFound() {
        Organizations org = new Organizations();
        org.setOrganizationId(1L);
        when(organizationsRepository.findById(1L)).thenReturn(java.util.Optional.of(org));
        when(standardsRepository.findById(2L)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> organizationService.linkOrganizationToStandard(1L, 2L));
    }
}
