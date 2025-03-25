package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ControlRequirements;
import com.complymatrix.app.service.ControlRequirementsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/control-requirements")
public class ControlRequirementsController {

    private final ControlRequirementsService service;

    public ControlRequirementsController(ControlRequirementsService service) {
        this.service = service;
    }

    @GetMapping
    public List<ControlRequirements> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<ControlRequirements> create(@RequestBody ControlRequirements cr) {
        ControlRequirements created = service.create(cr);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{controlId}")
    public ResponseEntity<ControlRequirements> getOne(@PathVariable Long controlId) {
        return service.findById(controlId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{controlId}")
    public ResponseEntity<ControlRequirements> update(@PathVariable Long controlId,
                                                      @RequestBody ControlRequirements cr) {
        try {
            ControlRequirements updated = service.update(controlId, cr);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{controlId}")
    public ResponseEntity<ControlRequirements> patch(@PathVariable Long controlId,
                                                     @RequestBody ControlRequirements partial) {
        try {
            ControlRequirements updated = service.patch(controlId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{controlId}")
    public ResponseEntity<Void> delete(@PathVariable Long controlId) {
        try {
            service.delete(controlId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
