package com.complymatrix.app.controller;

import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.service.OrganizationStandardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/organization-standards
 */
@RestController
@RequestMapping("/api/v1/organization-standards")
public class OrganizationStandardsController {

    private final OrganizationStandardsService service;

    public OrganizationStandardsController(OrganizationStandardsService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrganizationStandards> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<OrganizationStandards> create(@RequestBody OrganizationStandards link) {
        OrganizationStandards created = service.create(link);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{organizationId}/{standardId}")
    public ResponseEntity<OrganizationStandards> getOne(@PathVariable Integer organizationId,
                                                        @PathVariable Integer standardId) {
        return service.findById(organizationId, standardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{organizationId}/{standardId}")
    public ResponseEntity<Void> delete(@PathVariable Integer organizationId,
                                       @PathVariable Integer standardId) {
        try {
            service.delete(organizationId, standardId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
