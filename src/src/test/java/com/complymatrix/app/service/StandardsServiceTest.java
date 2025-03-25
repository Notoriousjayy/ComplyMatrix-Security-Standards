package com.complymatrix.app.service;

import com.complymatrix.app.entity.Standards;
import com.complymatrix.app.repository.StandardsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StandardsServiceTest {

    @Mock
    private StandardsRepository repository;

    @InjectMocks
    private StandardsService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("findAll returns list")
    void findAllTest() {
        Standards s = new Standards();
        s.setStandardId(1L);

        when(repository.findAll()).thenReturn(Collections.singletonList(s));

        List<Standards> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getStandardId());
    }

    @Test
    @DisplayName("findById returns optional if found")
    void findByIdTest() {
        Standards s = new Standards();
        s.setStandardId(2L);

        when(repository.findById(2L)).thenReturn(Optional.of(s));

        Optional<Standards> found = service.findById(2L);
        assertTrue(found.isPresent());
        assertEquals(2L, found.get().getStandardId());
    }

    @Test
    @DisplayName("createStandard saves entity")
    void createStandardTest() {
        Standards toSave = new Standards();
        toSave.setName("ISO 27001");
        when(repository.save(toSave)).thenReturn(toSave);

        Standards result = service.createStandard(toSave);
        assertEquals("ISO 27001", result.getName());
    }

    @Test
    @DisplayName("deleteStandard throws if not found")
    void deleteStandardNotFoundTest() {
        when(repository.existsById(999L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> service.deleteStandard(999L));
    }
}
