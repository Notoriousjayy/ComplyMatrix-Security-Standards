package com.complymatrix.app.controller;

import com.complymatrix.app.entity.TrainingResources;
import com.complymatrix.app.service.TrainingResourcesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/training-resources
 */
@RestController
@RequestMapping("/api/v1/training-resources")
public class TrainingResourcesController {

    private final TrainingResourcesService service;

    public TrainingResourcesController(TrainingResourcesService service) {
        this.service = service;
    }

    @GetMapping
    public List<TrainingResources> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<TrainingResources> create(@RequestBody TrainingResources tr) {
        TrainingResources created = service.create(tr);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<TrainingResources> getOne(@PathVariable Integer resourceId) {
        return service.findById(resourceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<TrainingResources> update(@PathVariable Integer resourceId,
                                                    @RequestBody TrainingResources tr) {
        try {
            TrainingResources updated = service.update(resourceId, tr);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{resourceId}")
    public ResponseEntity<TrainingResources> patch(@PathVariable Integer resourceId,
                                                   @RequestBody TrainingResources partial) {
        try {
            TrainingResources updated = service.patch(resourceId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Void> delete(@PathVariable Integer resourceId) {
        try {
            service.delete(resourceId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
