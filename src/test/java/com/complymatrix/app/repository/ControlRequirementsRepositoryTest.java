package com.complymatrix.app.repository;

import com.complymatrix.app.BasePostgresContainerTest;
import com.complymatrix.app.entity.ControlRequirements;
import com.complymatrix.app.entity.Standards;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(LiquibaseAutoConfiguration.class)
class ControlRequirementsRepositoryTest extends BasePostgresContainerTest {

    @Autowired
    private ControlRequirementsRepository controlRequirementsRepository;

    @Autowired
    private StandardsRepository standardsRepository;

    @Test
    void testSaveAndFind() {
        // First create and save a related Standard
        Standards standard = new Standards();
        standard.setName("NIST CSF");
        standard.setCategory("Cybersecurity");
        Standards savedStandard = standardsRepository.save(standard);

        // Create a new ControlRequirement
        ControlRequirements control = new ControlRequirements();
        control.setStandard(savedStandard);
        control.setName("Access Control Policy");
        control.setDescription("Controls related to account authorization and access policy.");
        control.setCategory("AC");
        control.setIsMandatory(true);
        control.setRiskLevel("High");
        control.setComplianceType("Regulatory");
        control.setApplicableIndustry("General");

        // Save
        ControlRequirements savedControl = controlRequirementsRepository.save(control);
        assertNotNull(savedControl.getControlId());

        // Retrieve
        ControlRequirements found = controlRequirementsRepository.findById(savedControl.getControlId())
                .orElse(null);
        assertNotNull(found);
        assertEquals("Access Control Policy", found.getName());
        assertEquals(savedStandard.getStandardId(), found.getStandard().getStandardId());
    }
}
