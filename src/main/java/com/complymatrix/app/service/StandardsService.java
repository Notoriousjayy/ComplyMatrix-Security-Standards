package com.complymatrix.app.service;

import com.complymatrix.app.entity.Standards;
import com.complymatrix.app.repository.StandardsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StandardsService {

    private final StandardsRepository standardsRepository;

    @Autowired
    public StandardsService(StandardsRepository standardsRepository) {
        this.standardsRepository = standardsRepository;
    }

    public List<Standards> findAll() {
        return standardsRepository.findAll();
    }

    public Optional<Standards> findById(Integer id) {
        return standardsRepository.findById(id);
    }

    public Standards createStandard(Standards standard) {
        // ID is auto-generated (assuming sequence or identity). Just save:
        return standardsRepository.save(standard);
    }

    public Standards updateStandard(Integer id, Standards updated) {
        // Full update (PUT) - replace all fields
        return standardsRepository.findById(id)
                .map(existing -> {
                    // Preserve the ID to avoid overwriting it
                    Integer existingId = existing.getStandardId();
                    BeanUtils.copyProperties(updated, existing, "standardId");
                    existing.setStandardId(existingId);
                    return standardsRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Standard not found with ID: " + id));
    }

    public Standards patchStandard(Integer id, Standards partial) {
        // Partial update (PATCH)
        return standardsRepository.findById(id)
                .map(existing -> {
                    if (partial.getName() != null) {
                        existing.setName(partial.getName());
                    }
                    if (partial.getCategory() != null) {
                        existing.setCategory(partial.getCategory());
                    }
                    if (partial.getDescription() != null) {
                        existing.setDescription(partial.getDescription());
                    }
                    if (partial.getUrl() != null) {
                        existing.setUrl(partial.getUrl());
                    }
                    if (partial.getPublisher() != null) {
                        existing.setPublisher(partial.getPublisher());
                    }
                    if (partial.getFocusArea() != null) {
                        existing.setFocusArea(partial.getFocusArea());
                    }
                    if (partial.getReleaseDate() != null) {
                        existing.setReleaseDate(partial.getReleaseDate());
                    }
                    if (partial.getStandardVersion() != null) {
                        existing.setStandardVersion(partial.getStandardVersion());
                    }
                    if (partial.getRegion() != null) {
                        existing.setRegion(partial.getRegion());
                    }
                    return standardsRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Standard not found with ID: " + id));
    }

    public void deleteStandard(Integer id) {
        if (!standardsRepository.existsById(id)) {
            throw new RuntimeException("Standard not found with ID: " + id);
        }
        standardsRepository.deleteById(id);
    }
}
