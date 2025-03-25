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

    public Optional<ComplianceTracking> findById(Long id) {
        return repository.findById(id);
    }

    public ComplianceTracking create(ComplianceTracking ct) {
        return repository.save(ct);
    }

    public ComplianceTracking update(Long id, ComplianceTracking updated) {
        return repository.findById(id)
                .map(existing -> {
                    Long existingId = existing.getTrackingId();
                    BeanUtils.copyProperties(updated, existing, "trackingId");
                    existing.setTrackingId(existingId);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ComplianceTracking not found with ID: " + id));
    }

    public ComplianceTracking patch(Long id, ComplianceTracking partial) {
        return repository.findById(id)
                .map(existing -> {
                    if (partial.getOrganization() != null) {
                        existing.setOrganization(partial.getOrganization());
                    }
                    if (partial.getStandard() != null) {
                        existing.setStandard(partial.getStandard());
                    }
                    if (partial.getControl() != null) {
                        existing.setControl(partial.getControl());
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

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ComplianceTracking not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
