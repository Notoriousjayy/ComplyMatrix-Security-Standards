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

    public Optional<ControlRelevantStandards> findById(Integer controlId, Integer standardId) {
        return repository.findById(new ControlRelevantStandardsId(controlId, standardId));
    }

    public ControlRelevantStandards create(ControlRelevantStandards link) {
        return repository.save(link);
    }

    public void delete(Integer controlId, Integer standardId) {
        ControlRelevantStandardsId id = new ControlRelevantStandardsId(controlId, standardId);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Link not found: control=" + controlId + ", std=" + standardId);
        }
        repository.deleteById(id);
    }
}
