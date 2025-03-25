package com.complymatrix.app.service;

import com.complymatrix.app.entity.Organizations;
import com.complymatrix.app.repository.OrganizationsRepository;
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
class OrganizationsServiceTest {

    @Mock
    private OrganizationsRepository repository;

    @InjectMocks
    private OrganizationsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("findAll returns list")
    void findAllTest() {
        Organizations org = new Organizations();
        org.setOrganizationId(1L);
        when(repository.findAll()).thenReturn(Collections.singletonList(org));

        List<Organizations> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getOrganizationId());
    }

    @Test
    @DisplayName("findById returns optional if found")
    void findByIdTest() {
        Organizations org = new Organizations();
        org.setOrganizationId(2L);

        when(repository.findById(2L)).thenReturn(Optional.of(org));

        Optional<Organizations> found = service.findById(2L);
        assertTrue(found.isPresent());
        assertEquals(2L, found.get().getOrganizationId());
    }

    @Test
    @DisplayName("create saves entity")
    void createTest() {
        Organizations toSave = new Organizations();
        toSave.setName("Acme Corp");
        when(repository.save(toSave)).thenReturn(toSave);

        Organizations result = service.create(toSave);
        assertEquals("Acme Corp", result.getName());
    }

    @Test
    @DisplayName("delete throws if not found")
    void deleteNotFoundTest() {
        when(repository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.delete(999L));
    }
}
