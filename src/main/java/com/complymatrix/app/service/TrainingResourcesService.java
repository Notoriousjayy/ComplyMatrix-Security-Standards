package com.complymatrix.app.service;

import com.complymatrix.app.entity.TrainingResources;
import com.complymatrix.app.repository.TrainingResourcesRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingResourcesService {

    private final TrainingResourcesRepository repository;

    public TrainingResourcesService(TrainingResourcesRepository repository) {
        this.repository = repository;
    }

    public List<TrainingResources> findAll() {
        return repository.findAll();
    }

    public Optional<TrainingResources> findById(Long id) {
        return repository.findById(id);
    }

    public TrainingResources create(TrainingResources tr) {
        return repository.save(tr);
    }

    public TrainingResources update(Long id, TrainingResources updated) {
        return repository.findById(id)
                .map(existing -> {
                    Long existingId = existing.getResourceId();
                    BeanUtils.copyProperties(updated, existing, "resourceId");
                    existing.setResourceId(existingId);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("TrainingResource not found with ID: " + id));
    }

    public TrainingResources patch(Long id, TrainingResources partial) {
        return repository.findById(id)
                .map(existing -> {
                    if (partial.getName() != null) {
                        existing.setName(partial.getName());
                    }
                    if (partial.getStandard() != null) {
                        existing.setStandard(partial.getStandard());
                    }
                    if (partial.getDescription() != null) {
                        existing.setDescription(partial.getDescription());
                    }
                    if (partial.getProvider() != null) {
                        existing.setProvider(partial.getProvider());
                    }
                    if (partial.getType() != null) {
                        existing.setType(partial.getType());
                    }
                    if (partial.getUrl() != null) {
                        existing.setUrl(partial.getUrl());
                    }
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("TrainingResource not found with ID: " + id));
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("TrainingResource not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
