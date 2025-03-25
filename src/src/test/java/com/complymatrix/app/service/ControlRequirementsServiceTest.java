package com.complymatrix.app.service;

import com.complymatrix.app.entity.ControlRequirements;
import com.complymatrix.app.repository.ControlRequirementsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControlRequirementsServiceTest {

    @Mock
    private ControlRequirementsRepository repository;

    @InjectMocks
    private ControlRequirementsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("findAll returns list")
    void findAllTest() {
        ControlRequirements cr = new ControlRequirements();
        cr.setControlId(1L);

        when(repository.findAll()).thenReturn(Collections.singletonList(cr));

        List<ControlRequirements> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getControlId());
    }

    @Test
    @DisplayName("findById returns optional if found")
    void findByIdTest() {
        ControlRequirements cr = new ControlRequirements();
        cr.setControlId(2L);

        when(repository.findById(2L)).thenReturn(Optional.of(cr));

        Optional<ControlRequirements> found = service.findById(2L);
        assertTrue(found.isPresent());
        assertEquals(2L, found.get().getControlId());
    }

    @Test
    @DisplayName("create saves entity")
    void createTest() {
        ControlRequirements toSave = new ControlRequirements();
        toSave.setName("New CR");
        when(repository.save(toSave)).thenReturn(toSave);

        ControlRequirements result = service.create(toSave);
        assertEquals("New CR", result.getName());
    }

    @Test
    @DisplayName("delete throws if not found")
    void deleteNotFoundTest() {
        when(repository.existsById(999L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.delete(999L));
    }
}
