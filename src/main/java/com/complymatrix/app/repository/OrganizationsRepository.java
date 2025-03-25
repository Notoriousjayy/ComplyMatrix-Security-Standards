package com.complymatrix.app.repository;

import com.complymatrix.app.entity.Organizations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationsRepository extends JpaRepository<Organizations, Long> {
    // Optionally add custom query methods here
}
