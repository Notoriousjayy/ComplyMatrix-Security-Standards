package com.complymatrix.app.service;

import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.entity.OrganizationStandardsId;
import com.complymatrix.app.repository.OrganizationStandardsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationStandardsServiceTest {

    @Mock
    private OrganizationStandardsRepository repository;

    @InjectMocks
    private OrganizationStandardsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("findAll returns a list")
    void findAllTest() {
        OrganizationStandards os = new OrganizationStandards();
        when(repository.findAll()).thenReturn(Collections.singletonList(os));

        List<OrganizationStandards> result = service.findAll();
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("findById returns optional if found")
    void findByIdTest() {
        OrganizationStandardsId id = new OrganizationStandardsId();
        id.setOrganizationId(1L);
        id.setStandardId(2L);

        OrganizationStandards os = new OrganizationStandards();
        when(repository.findById(id)).thenReturn(Optional.of(os));

        Optional<OrganizationStandards> found = service.findById(1L, 2L);
        assertTrue(found.isPresent());
    }

    @Test
    @DisplayName("create saves entity")
    void createTest() {
        OrganizationStandards toSave = new OrganizationStandards();
        when(repository.save(toSave)).thenReturn(toSave);

        OrganizationStandards result = service.create(toSave);
        verify(repository).save(toSave);
        assertNotNull(result);
    }

    @Test
    @DisplayName("delete throws if not found")
    void deleteNotFoundTest() {
        OrganizationStandardsId id = new OrganizationStandardsId();
        id.setOrganizationId(1L);
        id.setStandardId(2L);

        when(repository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.delete(1L, 2L));
    }
}
