package com.complymatrix.app.service;

import com.complymatrix.app.entity.ComplianceTracking;
import com.complymatrix.app.repository.ComplianceTrackingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComplianceTrackingServiceTest {

    @Mock
    private ComplianceTrackingRepository repository;

    @InjectMocks
    private ComplianceTrackingService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("findAll returns a list")
    void findAllTest() {
        ComplianceTracking ct = new ComplianceTracking();
        ct.setTrackingId(1L);

        when(repository.findAll()).thenReturn(Collections.singletonList(ct));

        List<ComplianceTracking> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getTrackingId());
    }

    @Test
    @DisplayName("findById returns Optional if found")
    void findByIdTest() {
        ComplianceTracking ct = new ComplianceTracking();
        ct.setTrackingId(2L);

        when(repository.findById(2L)).thenReturn(Optional.of(ct));

        Optional<ComplianceTracking> found = service.findById(2L);
        assertTrue(found.isPresent());
        assertEquals(2L, found.get().getTrackingId());
    }

    @Test
    @DisplayName("create saves entity")
    void createTest() {
        ComplianceTracking toSave = new ComplianceTracking();
        toSave.setComplianceStatus("PASS");

        when(repository.save(toSave)).thenReturn(toSave);

        ComplianceTracking result = service.create(toSave);
        assertEquals("PASS", result.getComplianceStatus());
        verify(repository).save(toSave);
    }

    @Test
    @DisplayName("delete throws if not found")
    void deleteNotFoundTest() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.delete(99L));
    }
}
