#!/bin/bash

# ---------------------------------------------------------------------------
# 1) Create necessary directories for controllers and services
# ---------------------------------------------------------------------------
mkdir -p src/main/java/com/complymatrix/app/controller
mkdir -p src/main/java/com/complymatrix/app/service

# ---------------------------------------------------------------------------
# 2) Standards
# ---------------------------------------------------------------------------
cat << 'EOF' > src/main/java/com/complymatrix/app/service/StandardsService.java
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
EOF

cat << 'EOF' > src/main/java/com/complymatrix/app/controller/StandardsController.java
package com.complymatrix.app.controller;

import com.complymatrix.app.entity.Standards;
import com.complymatrix.app.service.StandardsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/standards
 */
@RestController
@RequestMapping("/api/v1/standards")
public class StandardsController {

    private final StandardsService standardsService;

    public StandardsController(StandardsService standardsService) {
        this.standardsService = standardsService;
    }

    @GetMapping
    public List<Standards> listStandards() {
        return standardsService.findAll();
    }

    @PostMapping
    public ResponseEntity<Standards> createStandard(@RequestBody Standards standard) {
        Standards created = standardsService.createStandard(standard);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{standardId}")
    public ResponseEntity<Standards> getStandard(@PathVariable Integer standardId) {
        return standardsService.findById(standardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{standardId}")
    public ResponseEntity<Standards> updateStandard(@PathVariable Integer standardId,
                                                    @RequestBody Standards standard) {
        try {
            Standards updated = standardsService.updateStandard(standardId, standard);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{standardId}")
    public ResponseEntity<Standards> patchStandard(@PathVariable Integer standardId,
                                                   @RequestBody Standards partial) {
        try {
            Standards updated = standardsService.patchStandard(standardId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{standardId}")
    public ResponseEntity<Void> deleteStandard(@PathVariable Integer standardId) {
        try {
            standardsService.deleteStandard(standardId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
EOF

# ---------------------------------------------------------------------------
# 3) Control Requirements
# ---------------------------------------------------------------------------
cat << 'EOF' > src/main/java/com/complymatrix/app/service/ControlRequirementsService.java
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

    public Optional<ControlRequirements> findById(Integer id) {
        return controlRequirementsRepository.findById(id);
    }

    public ControlRequirements create(ControlRequirements cr) {
        return controlRequirementsRepository.save(cr);
    }

    public ControlRequirements update(Integer id, ControlRequirements updated) {
        return controlRequirementsRepository.findById(id)
                .map(existing -> {
                    Integer existingId = existing.getControlId();
                    BeanUtils.copyProperties(updated, existing, "controlId");
                    existing.setControlId(existingId);
                    return controlRequirementsRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ControlRequirement not found with ID: " + id));
    }

    public ControlRequirements patch(Integer id, ControlRequirements partial) {
        return controlRequirementsRepository.findById(id)
                .map(existing -> {
                    if (partial.getStandardId() != null) {
                        existing.setStandardId(partial.getStandardId());
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

    public void delete(Integer id) {
        if (!controlRequirementsRepository.existsById(id)) {
            throw new RuntimeException("ControlRequirement not found with ID: " + id);
        }
        controlRequirementsRepository.deleteById(id);
    }
}
EOF

cat << 'EOF' > src/main/java/com/complymatrix/app/controller/ControlRequirementsController.java
package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ControlRequirements;
import com.complymatrix.app.service.ControlRequirementsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/control-requirements
 */
@RestController
@RequestMapping("/api/v1/control-requirements")
public class ControlRequirementsController {

    private final ControlRequirementsService service;

    public ControlRequirementsController(ControlRequirementsService service) {
        this.service = service;
    }

    @GetMapping
    public List<ControlRequirements> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<ControlRequirements> create(@RequestBody ControlRequirements cr) {
        ControlRequirements created = service.create(cr);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{controlId}")
    public ResponseEntity<ControlRequirements> getOne(@PathVariable Integer controlId) {
        return service.findById(controlId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{controlId}")
    public ResponseEntity<ControlRequirements> update(@PathVariable Integer controlId,
                                                      @RequestBody ControlRequirements cr) {
        try {
            ControlRequirements updated = service.update(controlId, cr);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{controlId}")
    public ResponseEntity<ControlRequirements> patch(@PathVariable Integer controlId,
                                                     @RequestBody ControlRequirements partial) {
        try {
            ControlRequirements updated = service.patch(controlId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{controlId}")
    public ResponseEntity<Void> delete(@PathVariable Integer controlId) {
        try {
            service.delete(controlId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
EOF

# ---------------------------------------------------------------------------
# 4) Compliance Mappings
# ---------------------------------------------------------------------------
cat << 'EOF' > src/main/java/com/complymatrix/app/service/ComplianceMappingsService.java
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
EOF

cat << 'EOF' > src/main/java/com/complymatrix/app/controller/ComplianceMappingsController.java
package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ComplianceMappings;
import com.complymatrix.app.service.ComplianceMappingsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/compliance-mappings
 */
@RestController
@RequestMapping("/api/v1/compliance-mappings")
public class ComplianceMappingsController {

    private final ComplianceMappingsService service;

    public ComplianceMappingsController(ComplianceMappingsService service) {
        this.service = service;
    }

    @GetMapping
    public List<ComplianceMappings> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<ComplianceMappings> create(@RequestBody ComplianceMappings cm) {
        ComplianceMappings created = service.create(cm);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{mappingId}")
    public ResponseEntity<ComplianceMappings> getOne(@PathVariable Integer mappingId) {
        return service.findById(mappingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{mappingId}")
    public ResponseEntity<ComplianceMappings> update(@PathVariable Integer mappingId,
                                                     @RequestBody ComplianceMappings cm) {
        try {
            ComplianceMappings updated = service.update(mappingId, cm);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{mappingId}")
    public ResponseEntity<ComplianceMappings> patch(@PathVariable Integer mappingId,
                                                    @RequestBody ComplianceMappings partial) {
        try {
            ComplianceMappings updated = service.patch(mappingId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{mappingId}")
    public ResponseEntity<Void> delete(@PathVariable Integer mappingId) {
        try {
            service.delete(mappingId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
EOF

# ---------------------------------------------------------------------------
# 5) Organizations
# ---------------------------------------------------------------------------
cat << 'EOF' > src/main/java/com/complymatrix/app/service/OrganizationsService.java
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

    public Optional<Organizations> findById(Integer id) {
        return repository.findById(id);
    }

    public Organizations create(Organizations org) {
        return repository.save(org);
    }

    public Organizations update(Integer id, Organizations updated) {
        return repository.findById(id)
                .map(existing -> {
                    Integer existingId = existing.getOrganizationId();
                    BeanUtils.copyProperties(updated, existing, "organizationId");
                    existing.setOrganizationId(existingId);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Organization not found with ID: " + id));
    }

    public Organizations patch(Integer id, Organizations partial) {
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

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Organization not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
EOF

cat << 'EOF' > src/main/java/com/complymatrix/app/controller/OrganizationsController.java
package com.complymatrix.app.controller;

import com.complymatrix.app.entity.Organizations;
import com.complymatrix.app.service.OrganizationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/organizations
 */
@RestController
@RequestMapping("/api/v1/organizations")
public class OrganizationsController {

    private final OrganizationsService service;

    public OrganizationsController(OrganizationsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Organizations> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<Organizations> create(@RequestBody Organizations org) {
        Organizations created = service.create(org);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<Organizations> getOne(@PathVariable Integer organizationId) {
        return service.findById(organizationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{organizationId}")
    public ResponseEntity<Organizations> update(@PathVariable Integer organizationId,
                                                @RequestBody Organizations org) {
        try {
            Organizations updated = service.update(organizationId, org);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{organizationId}")
    public ResponseEntity<Organizations> patch(@PathVariable Integer organizationId,
                                               @RequestBody Organizations partial) {
        try {
            Organizations updated = service.patch(organizationId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<Void> delete(@PathVariable Integer organizationId) {
        try {
            service.delete(organizationId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
EOF

# ---------------------------------------------------------------------------
# 6) Organization Standards (Many-to-Many bridging)
#    Typically we only GET (all), POST (link), GET by ID, and DELETE (unlink).
#    No PUT/PATCH in the openapi spec for these bridging endpoints.
# ---------------------------------------------------------------------------
cat << 'EOF' > src/main/java/com/complymatrix/app/service/OrganizationStandardsService.java
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
EOF

cat << 'EOF' > src/main/java/com/complymatrix/app/controller/OrganizationStandardsController.java
package com.complymatrix.app.controller;

import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.service.OrganizationStandardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/organization-standards
 */
@RestController
@RequestMapping("/api/v1/organization-standards")
public class OrganizationStandardsController {

    private final OrganizationStandardsService service;

    public OrganizationStandardsController(OrganizationStandardsService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrganizationStandards> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<OrganizationStandards> create(@RequestBody OrganizationStandards link) {
        OrganizationStandards created = service.create(link);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{organizationId}/{standardId}")
    public ResponseEntity<OrganizationStandards> getOne(@PathVariable Integer organizationId,
                                                        @PathVariable Integer standardId) {
        return service.findById(organizationId, standardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{organizationId}/{standardId}")
    public ResponseEntity<Void> delete(@PathVariable Integer organizationId,
                                       @PathVariable Integer standardId) {
        try {
            service.delete(organizationId, standardId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
EOF

# ---------------------------------------------------------------------------
# 7) Control Relevant Standards (Many-to-Many bridging)
#    Similar to OrganizationStandards
# ---------------------------------------------------------------------------
cat << 'EOF' > src/main/java/com/complymatrix/app/service/ControlRelevantStandardsService.java
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
EOF

cat << 'EOF' > src/main/java/com/complymatrix/app/controller/ControlRelevantStandardsController.java
package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ControlRelevantStandards;
import com.complymatrix.app.service.ControlRelevantStandardsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/control-relevant-standards
 */
@RestController
@RequestMapping("/api/v1/control-relevant-standards")
public class ControlRelevantStandardsController {

    private final ControlRelevantStandardsService service;

    public ControlRelevantStandardsController(ControlRelevantStandardsService service) {
        this.service = service;
    }

    @GetMapping
    public List<ControlRelevantStandards> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<ControlRelevantStandards> create(@RequestBody ControlRelevantStandards link) {
        ControlRelevantStandards created = service.create(link);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/{controlId}/{standardId}")
    public ResponseEntity<ControlRelevantStandards> getOne(@PathVariable Integer controlId,
                                                           @PathVariable Integer standardId) {
        return service.findById(controlId, standardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{controlId}/{standardId}")
    public ResponseEntity<Void> delete(@PathVariable Integer controlId,
                                       @PathVariable Integer standardId) {
        try {
            service.delete(controlId, standardId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
EOF

# ---------------------------------------------------------------------------
# 8) Compliance Tracking
# ---------------------------------------------------------------------------
cat << 'EOF' > src/main/java/com/complymatrix/app/service/ComplianceTrackingService.java
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

    public Optional<ComplianceTracking> findById(Integer id) {
        return repository.findById(id);
    }

    public ComplianceTracking create(ComplianceTracking ct) {
        return repository.save(ct);
    }

    public ComplianceTracking update(Integer id, ComplianceTracking updated) {
        return repository.findById(id)
                .map(existing -> {
                    Integer existingId = existing.getTrackingId();
                    BeanUtils.copyProperties(updated, existing, "trackingId");
                    existing.setTrackingId(existingId);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("ComplianceTracking not found with ID: " + id));
    }

    public ComplianceTracking patch(Integer id, ComplianceTracking partial) {
        return repository.findById(id)
                .map(existing -> {
                    if (partial.getOrganizationId() != null) {
                        existing.setOrganizationId(partial.getOrganizationId());
                    }
                    if (partial.getStandardId() != null) {
                        existing.setStandardId(partial.getStandardId());
                    }
                    if (partial.getControlId() != null) {
                        existing.setControlId(partial.getControlId());
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

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("ComplianceTracking not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
EOF

cat << 'EOF' > src/main/java/com/complymatrix/app/controller/ComplianceTrackingController.java
package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ComplianceTracking;
import com.complymatrix.app.service.ComplianceTrackingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/compliance-tracking
 */
@RestController
@RequestMapping("/api/v1/compliance-tracking")
public class ComplianceTrackingController {

    private final ComplianceTrackingService service;

    public ComplianceTrackingController(ComplianceTrackingService service) {
        this.service = service;
    }

    @GetMapping
    public List<ComplianceTracking> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<ComplianceTracking> create(@RequestBody ComplianceTracking ct) {
        ComplianceTracking created = service.create(ct);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<ComplianceTracking> getOne(@PathVariable Integer trackingId) {
        return service.findById(trackingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{trackingId}")
    public ResponseEntity<ComplianceTracking> update(@PathVariable Integer trackingId,
                                                     @RequestBody ComplianceTracking ct) {
        try {
            ComplianceTracking updated = service.update(trackingId, ct);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{trackingId}")
    public ResponseEntity<ComplianceTracking> patch(@PathVariable Integer trackingId,
                                                    @RequestBody ComplianceTracking partial) {
        try {
            ComplianceTracking updated = service.patch(trackingId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{trackingId}")
    public ResponseEntity<Void> delete(@PathVariable Integer trackingId) {
        try {
            service.delete(trackingId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
EOF

# ---------------------------------------------------------------------------
# 9) Training Resources
# ---------------------------------------------------------------------------
cat << 'EOF' > src/main/java/com/complymatrix/app/service/TrainingResourcesService.java
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

    public Optional<TrainingResources> findById(Integer id) {
        return repository.findById(id);
    }

    public TrainingResources create(TrainingResources tr) {
        return repository.save(tr);
    }

    public TrainingResources update(Integer id, TrainingResources updated) {
        return repository.findById(id)
                .map(existing -> {
                    Integer existingId = existing.getResourceId();
                    BeanUtils.copyProperties(updated, existing, "resourceId");
                    existing.setResourceId(existingId);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("TrainingResource not found with ID: " + id));
    }

    public TrainingResources patch(Integer id, TrainingResources partial) {
        return repository.findById(id)
                .map(existing -> {
                    if (partial.getName() != null) {
                        existing.setName(partial.getName());
                    }
                    if (partial.getStandardId() != null) {
                        existing.setStandardId(partial.getStandardId());
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

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("TrainingResource not found with ID: " + id);
        }
        repository.deleteById(id);
    }
}
EOF

cat << 'EOF' > src/main/java/com/complymatrix/app/controller/TrainingResourcesController.java
package com.complymatrix.app.controller;

import com.complymatrix.app.entity.TrainingResources;
import com.complymatrix.app.service.TrainingResourcesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for /api/v1/training-resources
 */
@RestController
@RequestMapping("/api/v1/training-resources")
public class TrainingResourcesController {

    private final TrainingResourcesService service;

    public TrainingResourcesController(TrainingResourcesService service) {
        this.service = service;
    }

    @GetMapping
    public List<TrainingResources> listAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<TrainingResources> create(@RequestBody TrainingResources tr) {
        TrainingResources created = service.create(tr);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<TrainingResources> getOne(@PathVariable Integer resourceId) {
        return service.findById(resourceId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{resourceId}")
    public ResponseEntity<TrainingResources> update(@PathVariable Integer resourceId,
                                                    @RequestBody TrainingResources tr) {
        try {
            TrainingResources updated = service.update(resourceId, tr);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{resourceId}")
    public ResponseEntity<TrainingResources> patch(@PathVariable Integer resourceId,
                                                   @RequestBody TrainingResources partial) {
        try {
            TrainingResources updated = service.patch(resourceId, partial);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{resourceId}")
    public ResponseEntity<Void> delete(@PathVariable Integer resourceId) {
        try {
            service.delete(resourceId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
EOF

echo "All controller and service classes have been created successfully."
