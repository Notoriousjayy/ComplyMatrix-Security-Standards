package com.complymatrix.app.service;

import com.complymatrix.app.entity.ControlRelevantStandards;
import com.complymatrix.app.entity.ControlRelevantStandardsId;
import com.complymatrix.app.repository.ControlRelevantStandardsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ControlRelevantStandardsService {

    private final ControlRelevantStandardsRepository repository;

    public ControlRelevantStandardsService(ControlRelevantStandardsRepository repository) {
        this.repository = repository;
    }

    public List<ControlRelevantStandards> findAll() {
        return repository.findAll();
    }

    public Optional<ControlRelevantStandards> findById(Long controlId, Long standardId) {
        // Create composite key using the no-arg constructor and setters.
        ControlRelevantStandardsId id = new ControlRelevantStandardsId();
        id.setControlId(controlId);
        id.setStandardId(standardId);
        return repository.findById(id);
    }

    public ControlRelevantStandards create(ControlRelevantStandards link) {
        return repository.save(link);
    }

    public void delete(Long controlId, Long standardId) {
        ControlRelevantStandardsId id = new ControlRelevantStandardsId();
        id.setControlId(controlId);
        id.setStandardId(standardId);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Link not found: control=" + controlId + ", std=" + standardId);
        }
        repository.deleteById(id);
    }
}
