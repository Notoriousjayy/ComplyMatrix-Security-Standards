package com.complymatrix.app.repository;

import com.complymatrix.app.entity.ControlRelevantStandards;
import com.complymatrix.app.entity.ControlRelevantStandardsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ControlRelevantStandardsRepository 
        extends JpaRepository<ControlRelevantStandards, ControlRelevantStandardsId> {
    // Optionally add custom query methods here
}
