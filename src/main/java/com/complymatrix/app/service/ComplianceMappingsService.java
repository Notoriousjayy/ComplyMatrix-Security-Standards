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

    public Optional<ComplianceMappings> findById(Long id) {
        return repository.findById(id);
    }

    public ComplianceMappings create(ComplianceMappings cm) {
        return repository.save(cm);
    }

    public ComplianceMappings update(Long id, ComplianceMappings updated) {
        return repository.findById(id)
                .map(existing -> {
                    Long existingId = existing.getMappingId();
                    BeanUtils.copyProperties(updated, existing, "mappingId");
                    existing.setMappingId(existingId);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ComplianceMapping not found with ID: " + id));
    }

    public ComplianceMappings patch(Long id, ComplianceMappings partial) {
        return repository.findById(id)
                .map(existing -> {
                    if (partial.getSourceStandard() != null) {
                        existing.setSourceStandard(partial.getSourceStandard());
                    }
                    if (partial.getSourceControl() != null) {
                        existing.setSourceControl(partial.getSourceControl());
                    }
                    if (partial.getTargetStandard() != null) {
                        existing.setTargetStandard(partial.getTargetStandard());
                    }
                    if (partial.getTargetControl() != null) {
                        existing.setTargetControl(partial.getTargetControl());
                    }
                    if (partial.getMappingDescription() != null) {
                        existing.setMappingDescription(partial.getMappingDescription());
                    }
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ComplianceMapping not found with ID: " + id));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ComplianceMapping not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
