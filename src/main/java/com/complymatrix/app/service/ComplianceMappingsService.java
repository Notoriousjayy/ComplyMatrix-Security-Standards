package com.complymatrix.app.service;

import com.complymatrix.app.entity.ComplianceMappings;
import com.complymatrix.app.repository.ComplianceMappingsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComplianceMappingsService {

    private final ComplianceMappingsRepository repository;

    public ComplianceMappingsService(ComplianceMappingsRepository repository) {
        this.repository = repository;
    }

    public List<ComplianceMappings> findAll() {
        return repository.findAll();
    }

    public Optional<ComplianceMappings> findById(Integer id) {
        return repository.findById(id);
    }

    public ComplianceMappings create(ComplianceMappings cm) {
        return repository.save(cm);
    }

    public ComplianceMappings update(Integer id, ComplianceMappings updated) {
        return repository.findById(id)
                .map(existing -> {
                    Integer existingId = existing.getMappingId();
                    BeanUtils.copyProperties(updated, existing, "mappingId");
                    existing.setMappingId(existingId);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ComplianceMapping not found with ID: " + id));
    }

    public ComplianceMappings patch(Integer id, ComplianceMappings partial) {
        return repository.findById(id)
                .map(existing -> {
                    if (partial.getSourceStandardId() != null) {
                        existing.setSourceStandardId(partial.getSourceStandardId());
                    }
                    if (partial.getSourceControlId() != null) {
                        existing.setSourceControlId(partial.getSourceControlId());
                    }
                    if (partial.getTargetStandardId() != null) {
                        existing.setTargetStandardId(partial.getTargetStandardId());
                    }
                    if (partial.getTargetControlId() != null) {
                        existing.setTargetControlId(partial.getTargetControlId());
                    }
                    if (partial.getMappingDescription() != null) {
                        existing.setMappingDescription(partial.getMappingDescription());
                    }
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ComplianceMapping not found with ID: " + id));
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ComplianceMapping not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
