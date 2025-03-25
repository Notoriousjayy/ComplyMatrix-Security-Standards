package com.complymatrix.app.repository;

import com.complymatrix.app.entity.TrainingResources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingResourcesRepository extends JpaRepository<TrainingResources, Long> {
    // Optionally add custom query methods here
}
