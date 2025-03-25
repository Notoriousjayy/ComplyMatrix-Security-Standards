package com.complymatrix.app.service;

import com.complymatrix.app.entity.ComplianceTracking;
import com.complymatrix.app.repository.ComplianceTrackingRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplianceTrackingService {

    private final ComplianceTrackingRepository repository;

    public ComplianceTrackingService(ComplianceTrackingRepository repository) {
        this.repository = repository;
    }

    public List<ComplianceTracking> findAll() {
        return repository.findAll();
    }

    public Optional<ComplianceTracking> findById(Integer id) {
        return repository.findById(id);
    }

    public ComplianceTracking create(ComplianceTracking ct) {
        return repository.save(ct);
    }

    public ComplianceTracking update(Integer id, ComplianceTracking updated) {
        return repository.findById(id)
                .map(existing -> {
                    Integer existingId = existing.getTrackingId();
                    BeanUtils.copyProperties(updated, existing, "trackingId");
                    existing.setTrackingId(existingId);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ComplianceTracking not found with ID: " + id));
    }

    public ComplianceTracking patch(Integer id, ComplianceTracking partial) {
        return repository.findById(id)
                .map(existing -> {
                    if (partial.getOrganizationId() != null) {
                        existing.setOrganizationId(partial.getOrganizationId());
                    }
                    if (partial.getStandardId() != null) {
                        existing.setStandardId(partial.getStandardId());
                    }
                    if (partial.getControlId() != null) {
                        existing.setControlId(partial.getControlId());
                    }
                    if (partial.getComplianceStatus() != null) {
                        existing.setComplianceStatus(partial.getComplianceStatus());
                    }
                    if (partial.getAuditDate() != null) {
                        existing.setAuditDate(partial.getAuditDate());
                    }
                    if (partial.getEvidenceUrl() != null) {
                        existing.setEvidenceUrl(partial.getEvidenceUrl());
                    }
                    if (partial.getRiskImpact() != null) {
                        existing.setRiskImpact(partial.getRiskImpact());
                    }
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ComplianceTracking not found with ID: " + id));
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ComplianceTracking not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
