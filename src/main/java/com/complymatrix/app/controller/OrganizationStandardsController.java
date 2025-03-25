package com.complymatrix.app.controller;

import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.service.OrganizationStandardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<OrganizationStandards> getOne(@PathVariable Long organizationId,
                                                        @PathVariable Long standardId) {
        return service.findById(organizationId, standardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{organizationId}/{standardId}")
    public ResponseEntity<Void> delete(@PathVariable Long organizationId,
                                       @PathVariable Long standardId) {
        try {
            service.delete(organizationId, standardId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
