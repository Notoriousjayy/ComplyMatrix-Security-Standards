package com.complymatrix.app.service;

import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.entity.OrganizationStandardsId;
import com.complymatrix.app.repository.OrganizationStandardsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationStandardsService {

    private final OrganizationStandardsRepository repository;

    public OrganizationStandardsService(OrganizationStandardsRepository repository) {
        this.repository = repository;
    }

    public List<OrganizationStandards> findAll() {
        return repository.findAll();
    }

    public Optional<OrganizationStandards> findById(Integer organizationId, Integer standardId) {
        return repository.findById(new OrganizationStandardsId(organizationId, standardId));
    }

    public OrganizationStandards create(OrganizationStandards link) {
        return repository.save(link);
    }

    public void delete(Integer organizationId, Integer standardId) {
        OrganizationStandardsId id = new OrganizationStandardsId(organizationId, standardId);
        if (!repository.existsById(id)) {
            throw new RuntimeException("Link not found: org=" + organizationId + ", std=" + standardId);
        }
        repository.deleteById(id);
    }
}
