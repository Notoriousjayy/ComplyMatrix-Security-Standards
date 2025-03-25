package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ControlRelevantStandards;
import com.complymatrix.app.service.ControlRelevantStandardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/control-relevant-standards
 */
@RestController
@RequestMapping("/api/v1/control-relevant-standards")
public class ControlRelevantStandardsController {

    private final ControlRelevantStandardsService service;

    public ControlRelevantStandardsController(ControlRelevantStandardsService service) {
        this.service = service;
    }

    @GetMapping
    public List<ControlRelevantStandards> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<ControlRelevantStandards> create(@RequestBody ControlRelevantStandards link) {
        ControlRelevantStandards created = service.create(link);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{controlId}/{standardId}")
    public ResponseEntity<ControlRelevantStandards> getOne(@PathVariable Integer controlId,
                                                           @PathVariable Integer standardId) {
        return service.findById(controlId, standardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{controlId}/{standardId}")
    public ResponseEntity<Void> delete(@PathVariable Integer controlId,
                                       @PathVariable Integer standardId) {
        try {
            service.delete(controlId, standardId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
