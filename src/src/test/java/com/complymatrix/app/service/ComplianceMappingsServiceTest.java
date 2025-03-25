package com.complymatrix.app.service;

import com.complymatrix.app.entity.ComplianceMappings;
import com.complymatrix.app.repository.ComplianceMappingsRepository;
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
class ComplianceMappingsServiceTest {

    @Mock
    private ComplianceMappingsRepository repository;

    @InjectMocks
    private ComplianceMappingsService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("findAll returns list from repository")
    void findAllTest() {
        ComplianceMappings cm = new ComplianceMappings();
        cm.setMappingId(1L);

        when(repository.findAll()).thenReturn(Collections.singletonList(cm));

        List<ComplianceMappings> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getMappingId());
    }

    @Test
    @DisplayName("findById returns optional from repository")
    void findByIdTest() {
        ComplianceMappings cm = new ComplianceMappings();
        cm.setMappingId(2L);

        when(repository.findById(2L)).thenReturn(Optional.of(cm));

        Optional<ComplianceMappings> found = service.findById(2L);
        assertTrue(found.isPresent());
        assertEquals(2L, found.get().getMappingId());
    }

    @Test
    @DisplayName("create saves entity")
    void createTest() {
        ComplianceMappings toSave = new ComplianceMappings();
        toSave.setSourceStandard("Source");
        when(repository.save(toSave)).thenReturn(toSave);

        ComplianceMappings result = service.create(toSave);
        assertEquals("Source", result.getSourceStandard());
        verify(repository).save(toSave);
    }

    @Test
    @DisplayName("delete throws if not found")
    void deleteNotFoundTest() {
        when(repository.existsById(99L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.delete(99L));
    }
}
