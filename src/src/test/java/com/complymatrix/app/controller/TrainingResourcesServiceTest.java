package com.complymatrix.app.service;

import com.complymatrix.app.entity.TrainingResources;
import com.complymatrix.app.repository.TrainingResourcesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingResourcesServiceTest {

    @Mock
    private TrainingResourcesRepository repository;

    @InjectMocks
    private TrainingResourcesService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        TrainingResources tr1 = new TrainingResources();
        tr1.setResourceId(1L);
        TrainingResources tr2 = new TrainingResources();
        tr2.setResourceId(2L);
        when(repository.findAll()).thenReturn(Arrays.asList(tr1, tr2));

        List<TrainingResources> list = service.findAll();
        assertEquals(2, list.size());
    }

    @Test
    void testFindByIdFound() {
        TrainingResources tr = new TrainingResources();
        tr.setResourceId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(tr));

        Optional<TrainingResources> result = service.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getResourceId());
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Optional<TrainingResources> result = service.findById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void testCreate() {
        TrainingResources tr = new TrainingResources();
        tr.setName("New Resource");
        when(repository.save(tr)).thenReturn(tr);

        TrainingResources created = service.create(tr);
        assertEquals("New Resource", created.getName());
    }

    @Test
    void testUpdateFound() {
        TrainingResources existing = new TrainingResources();
        existing.setResourceId(1L);
        existing.setName("Old Name");

        TrainingResources updated = new TrainingResources();
        updated.setName("New Name");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        TrainingResources result = service.update(1L, updated);
        assertEquals("New Name", result.getName());
    }

    @Test
    void testUpdateNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> service.update(1L, new TrainingResources()));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void testPatchFound() {
        TrainingResources existing = new TrainingResources();
        existing.setResourceId(1L);
        existing.setName("Old Name");

        TrainingResources patch = new TrainingResources();
        patch.setName("Patched Name");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        TrainingResources result = service.patch(1L, patch);
        assertEquals("Patched Name", result.getName());
    }

    @Test
    void testDeleteFound() {
        when(repository.existsById(1L)).thenReturn(true);
        service.delete(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(repository.existsById(1L)).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> service.delete(1L));
        assertTrue(exception.getMessage().contains("not found"));
    }
}
