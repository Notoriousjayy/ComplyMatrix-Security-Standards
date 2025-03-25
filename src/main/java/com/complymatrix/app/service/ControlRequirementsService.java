package com.complymatrix.app.service;

import com.complymatrix.app.entity.ControlRequirements;
import com.complymatrix.app.repository.ControlRequirementsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ControlRequirementsService {

    private final ControlRequirementsRepository controlRequirementsRepository;

    @Autowired
    public ControlRequirementsService(ControlRequirementsRepository controlRequirementsRepository) {
        this.controlRequirementsRepository = controlRequirementsRepository;
    }

    public List<ControlRequirements> findAll() {
        return controlRequirementsRepository.findAll();
    }

    public Optional<ControlRequirements> findById(Long id) {
        return controlRequirementsRepository.findById(id);
    }

    public ControlRequirements create(ControlRequirements cr) {
        return controlRequirementsRepository.save(cr);
    }

    public ControlRequirements update(Long id, ControlRequirements updated) {
        return controlRequirementsRepository.findById(id)
                .map(existing -> {
                    Long existingId = existing.getControlId();
                    BeanUtils.copyProperties(updated, existing, "controlId");
                    existing.setControlId(existingId);
                    return controlRequirementsRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ControlRequirement not found with ID: " + id));
    }

    public ControlRequirements patch(Long id, ControlRequirements partial) {
        return controlRequirementsRepository.findById(id)
                .map(existing -> {
                    if (partial.getStandard() != null) {
                        existing.setStandard(partial.getStandard());
                    }
                    if (partial.getName() != null) {
                        existing.setName(partial.getName());
                    }
                    if (partial.getDescription() != null) {
                        existing.setDescription(partial.getDescription());
                    }
                    if (partial.getCategory() != null) {
                        existing.setCategory(partial.getCategory());
                    }
                    if (partial.getIsMandatory() != null) {
                        existing.setIsMandatory(partial.getIsMandatory());
                    }
                    if (partial.getRiskLevel() != null) {
                        existing.setRiskLevel(partial.getRiskLevel());
                    }
                    if (partial.getComplianceType() != null) {
                        existing.setComplianceType(partial.getComplianceType());
                    }
                    if (partial.getApplicableIndustry() != null) {
                        existing.setApplicableIndustry(partial.getApplicableIndustry());
                    }
                    return controlRequirementsRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ControlRequirement not found with ID: " + id));
    }

    public void delete(Long id) {
        if (!controlRequirementsRepository.existsById(id)) {
            throw new RuntimeException("ControlRequirement not found with ID: " + id);
        }
        controlRequirementsRepository.deleteById(id);
    }
}
