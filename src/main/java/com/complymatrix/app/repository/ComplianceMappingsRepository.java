package com.complymatrix.app.repository;

import com.complymatrix.app.entity.ComplianceMappings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplianceMappingsRepository extends JpaRepository<ComplianceMappings, Long> {
    // Optionally add custom query methods here
}
