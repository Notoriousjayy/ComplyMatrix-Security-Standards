package com.complymatrix.app.controller;

import com.complymatrix.app.entity.Organizations;
import com.complymatrix.app.service.OrganizationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationsController {

    private final OrganizationsService service;

    public OrganizationsController(OrganizationsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Organizations> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Organizations> create(@RequestBody Organizations org) {
        Organizations created = service.create(org);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organizations> getOne(@PathVariable Long organizationId) {
        return service.findById(organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<Organizations> update(@PathVariable Long organizationId,
                                                @RequestBody Organizations org) {
        try {
            Organizations updated = service.update(organizationId, org);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{organizationId}")
    public ResponseEntity<Organizations> patch(@PathVariable Long organizationId,
                                               @RequestBody Organizations partial) {
        try {
            Organizations updated = service.patch(organizationId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Void> delete(@PathVariable Long organizationId) {
        try {
            service.delete(organizationId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
