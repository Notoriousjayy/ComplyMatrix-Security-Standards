package com.complymatrix.app.repository;

import com.complymatrix.app.entity.ControlRequirements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlRequirementsRepository extends JpaRepository<ControlRequirements, Long> {
    // Optionally add custom query methods here
}
