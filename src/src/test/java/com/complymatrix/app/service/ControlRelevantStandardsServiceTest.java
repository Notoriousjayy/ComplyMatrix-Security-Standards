package com.complymatrix.app.service;

import com.complymatrix.app.entity.ControlRelevantStandards;
import com.complymatrix.app.entity.ControlRelevantStandardsId;
import com.complymatrix.app.repository.ControlRelevantStandardsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControlRelevantStandardsServiceTest {

    @Mock
    private ControlRelevantStandardsRepository repository;

    @InjectMocks
    private ControlRelevantStandardsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("findById returns Optional if found")
    void findByIdTest() {
        ControlRelevantStandards crs = new ControlRelevantStandards();
        ControlRelevantStandardsId id = new ControlRelevantStandardsId();
        id.setControlId(1L);
        id.setStandardId(2L);

        when(repository.findById(id)).thenReturn(Optional.of(crs));

        Optional<ControlRelevantStandards> found = service.findById(1L, 2L);
        assertTrue(found.isPresent());
    }

    @Test
    @DisplayName("create saves entity")
    void createTest() {
        ControlRelevantStandards toSave = new ControlRelevantStandards();
        when(repository.save(toSave)).thenReturn(toSave);

        ControlRelevantStandards result = service.create(toSave);
        verify(repository).save(toSave);
        assertNotNull(result);
    }

    @Test
    @DisplayName("delete throws if not found")
    void deleteNotFoundTest() {
        ControlRelevantStandardsId id = new ControlRelevantStandardsId();
        id.setControlId(99L);
        id.setStandardId(100L);

        when(repository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.delete(99L, 100L));
    }
}
