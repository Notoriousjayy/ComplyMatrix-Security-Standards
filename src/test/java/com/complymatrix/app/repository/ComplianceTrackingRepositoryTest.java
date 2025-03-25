package com.complymatrix.app.repository;

import com.complymatrix.app.BasePostgresContainerTest;
import com.complymatrix.app.entity.ComplianceTracking;
import com.complymatrix.app.entity.Organizations;
import com.complymatrix.app.entity.Standards;

import jakarta.transaction.Transactional;

import com.complymatrix.app.entity.ControlRequirements;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(LiquibaseAutoConfiguration.class)
class ComplianceTrackingRepositoryTest extends BasePostgresContainerTest {

    @Autowired
    private ComplianceTrackingRepository complianceTrackingRepository;

    @Autowired
    private OrganizationsRepository organizationsRepository;

    @Autowired
    private StandardsRepository standardsRepository;

    @Autowired
    private ControlRequirementsRepository controlRequirementsRepository;

    @Test
    void testSaveAndFind() {
        // Create an Organization
        Organizations org = new Organizations();
        org.setName("Test Org");
        org = organizationsRepository.save(org);

        // Create a Standard
        Standards std = new Standards();
        std.setName("Test Standard");
        std = standardsRepository.save(std);

        // Create a Control
        ControlRequirements ctrl = new ControlRequirements();
        ctrl.setName("Test Control");
        ctrl.setStandard(std);
        ctrl = controlRequirementsRepository.save(ctrl);

        // Create a ComplianceTracking
        ComplianceTracking tracking = new ComplianceTracking();
        tracking.setOrganization(org);
        tracking.setStandard(std);
        tracking.setControl(ctrl);
        tracking.setComplianceStatus("Compliant");
        tracking.setAuditDate(LocalDate.now());
        tracking.setEvidenceUrl("http://example.com/proof");
        tracking.setRiskImpact("Low");

        // Save
        ComplianceTracking saved = complianceTrackingRepository.save(tracking);
        assertNotNull(saved.getTrackingId());

        // Retrieve
        ComplianceTracking found = complianceTrackingRepository.findById(saved.getTrackingId()).orElse(null);
        assertNotNull(found);
        assertEquals("Compliant", found.getComplianceStatus());
        assertEquals("Low", found.getRiskImpact());
        assertEquals(org.getOrganizationId(), found.getOrganization().getOrganizationId());
    }
}
