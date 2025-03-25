package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ComplianceTracking;
import com.complymatrix.app.service.ComplianceTrackingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/compliance-tracking")
public class ComplianceTrackingController {

    private final ComplianceTrackingService service;

    public ComplianceTrackingController(ComplianceTrackingService service) {
        this.service = service;
    }

    @GetMapping
    public List<ComplianceTracking> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<ComplianceTracking> create(@RequestBody ComplianceTracking ct) {
        ComplianceTracking created = service.create(ct);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<ComplianceTracking> getOne(@PathVariable Long trackingId) {
        return service.findById(trackingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{trackingId}")
    public ResponseEntity<ComplianceTracking> update(@PathVariable Long trackingId,
                                                     @RequestBody ComplianceTracking ct) {
        try {
            ComplianceTracking updated = service.update(trackingId, ct);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{trackingId}")
    public ResponseEntity<ComplianceTracking> patch(@PathVariable Long trackingId,
                                                    @RequestBody ComplianceTracking partial) {
        try {
            ComplianceTracking updated = service.patch(trackingId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{trackingId}")
    public ResponseEntity<Void> delete(@PathVariable Long trackingId) {
        try {
            service.delete(trackingId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
