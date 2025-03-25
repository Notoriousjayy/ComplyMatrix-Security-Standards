package com.complymatrix.app.controller;

import com.complymatrix.app.entity.Standards;
import com.complymatrix.app.service.StandardsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/standards
 */
@RestController
@RequestMapping("/api/v1/standards")
public class StandardsController {

    private final StandardsService standardsService;

    public StandardsController(StandardsService standardsService) {
        this.standardsService = standardsService;
    }

    @GetMapping
    public List<Standards> listStandards() {
        return standardsService.findAll();
    }

    @PostMapping
    public ResponseEntity<Standards> createStandard(@RequestBody Standards standard) {
        Standards created = standardsService.createStandard(standard);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{standardId}")
    public ResponseEntity<Standards> getStandard(@PathVariable Integer standardId) {
        return standardsService.findById(standardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{standardId}")
    public ResponseEntity<Standards> updateStandard(@PathVariable Integer standardId,
                                                    @RequestBody Standards standard) {
        try {
            Standards updated = standardsService.updateStandard(standardId, standard);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{standardId}")
    public ResponseEntity<Standards> patchStandard(@PathVariable Integer standardId,
                                                   @RequestBody Standards partial) {
        try {
            Standards updated = standardsService.patchStandard(standardId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{standardId}")
    public ResponseEntity<Void> deleteStandard(@PathVariable Integer standardId) {
        try {
            standardsService.deleteStandard(standardId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
