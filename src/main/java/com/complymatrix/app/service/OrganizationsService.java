package com.complymatrix.app.service;

import com.complymatrix.app.entity.Organizations;
import com.complymatrix.app.repository.OrganizationsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationsService {

    private final OrganizationsRepository repository;

    public OrganizationsService(OrganizationsRepository repository) {
        this.repository = repository;
    }

    public List<Organizations> findAll() {
        return repository.findAll();
    }

    public Optional<Organizations> findById(Long id) {
        return repository.findById(id);
    }

    public Organizations create(Organizations org) {
        return repository.save(org);
    }

    public Organizations update(Long id, Organizations updated) {
        return repository.findById(id)
                .map(existing -> {
                    Long existingId = existing.getOrganizationId();
                    BeanUtils.copyProperties(updated, existing, "organizationId");
                    existing.setOrganizationId(existingId);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Organization not found with ID: " + id));
    }

    public Organizations patch(Long id, Organizations partial) {
        return repository.findById(id)
                .map(existing -> {
                    if (partial.getName() != null) {
                        existing.setName(partial.getName());
                    }
                    if (partial.getIndustry() != null) {
                        existing.setIndustry(partial.getIndustry());
                    }
                    if (partial.getRegion() != null) {
                        existing.setRegion(partial.getRegion());
                    }
                    if (partial.getComplianceScore() != null) {
                        existing.setComplianceScore(partial.getComplianceScore());
                    }
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Organization not found with ID: " + id));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Organization not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
