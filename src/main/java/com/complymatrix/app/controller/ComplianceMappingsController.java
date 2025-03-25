package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ComplianceMappings;
import com.complymatrix.app.service.ComplianceMappingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compliance-mappings")
public class ComplianceMappingsController {

    private final ComplianceMappingsService service;

    public ComplianceMappingsController(ComplianceMappingsService service) {
        this.service = service;
    }

    @GetMapping
    public List<ComplianceMappings> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<ComplianceMappings> create(@RequestBody ComplianceMappings cm) {
        ComplianceMappings created = service.create(cm);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{mappingId}")
    public ResponseEntity<ComplianceMappings> getOne(@PathVariable Long mappingId) {
        return service.findById(mappingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{mappingId}")
    public ResponseEntity<ComplianceMappings> update(@PathVariable Long mappingId,
                                                     @RequestBody ComplianceMappings cm) {
        try {
            ComplianceMappings updated = service.update(mappingId, cm);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{mappingId}")
    public ResponseEntity<ComplianceMappings> patch(@PathVariable Long mappingId,
                                                    @RequestBody ComplianceMappings partial) {
        try {
            ComplianceMappings updated = service.patch(mappingId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{mappingId}")
    public ResponseEntity<Void> delete(@PathVariable Long mappingId) {
        try {
            service.delete(mappingId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
