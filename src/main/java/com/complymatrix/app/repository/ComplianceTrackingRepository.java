package com.complymatrix.app.repository;

import com.complymatrix.app.entity.ComplianceTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplianceTrackingRepository extends JpaRepository<ComplianceTracking, Long> {
    // Optionally add custom query methods here
}
