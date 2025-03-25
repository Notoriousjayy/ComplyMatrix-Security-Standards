package com.complymatrix.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.entity.Organizations;
import com.complymatrix.app.entity.Standards;
import com.complymatrix.app.repository.OrganizationStandardsRepository;
import com.complymatrix.app.repository.OrganizationsRepository;
import com.complymatrix.app.repository.StandardsRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrganizationService {

    @Autowired
    private OrganizationsRepository organizationsRepository;

    @Autowired
    private StandardsRepository standardsRepository;

    @Autowired
    private OrganizationStandardsRepository organizationStandardsRepository;

    public void linkOrganizationToStandard(Long orgId, Long stdId) {
        Organizations org = organizationsRepository.findById(orgId)
            .orElseThrow(() -> new RuntimeException("Org not found"));
        Standards std = standardsRepository.findById(stdId)
            .orElseThrow(() -> new RuntimeException("Standard not found"));

        OrganizationStandards link = new OrganizationStandards();
        link.setOrganization(org);
        link.setStandard(std);

        organizationStandardsRepository.save(link);
    }
}
