package com.complymatrix.app.repository;

import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.entity.OrganizationStandardsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationStandardsRepository 
        extends JpaRepository<OrganizationStandards, OrganizationStandardsId> {
    // Optionally add custom query methods here
}
