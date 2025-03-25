package com.complymatrix.app.repository;

import com.complymatrix.app.entity.ControlRequirements;
import com.complymatrix.app.entity.Standards;

import jakarta.transaction.Transactional;

import com.complymatrix.app.BasePostgresContainerTest;
import com.complymatrix.app.entity.ControlRelevantStandards;
import com.complymatrix.app.entity.ControlRelevantStandardsId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(LiquibaseAutoConfiguration.class)
class ControlRelevantStandardsRepositoryTest extends BasePostgresContainerTest {

    @Autowired
    private ControlRelevantStandardsRepository controlRelevantStandardsRepository;

    @Autowired
    private ControlRequirementsRepository controlRequirementsRepository;

    @Autowired
    private StandardsRepository standardsRepository;

    @Test
    void testSaveAndFindBridgingEntity() {
        // Create a Standard first
        Standards std = new Standards();
        std.setName("Test Standard");
        std = standardsRepository.save(std);

        // Create a ControlRequirements and link it to the standard
        ControlRequirements ctrl = new ControlRequirements();
        ctrl.setName("Test Control");
        ctrl.setStandard(std); // IMPORTANT: satisfies not-null "standard_id"
        ctrl = controlRequirementsRepository.save(ctrl);

        // Create bridging entity with composite ID
        ControlRelevantStandardsId crsId = new ControlRelevantStandardsId();
        crsId.setControlId(ctrl.getControlId());
        crsId.setStandardId(std.getStandardId());

        ControlRelevantStandards crs = new ControlRelevantStandards();
        crs.setId(crsId);
        crs.setControl(ctrl);
        crs.setStandard(std);

        // Save
        ControlRelevantStandards saved = controlRelevantStandardsRepository.save(crs);
        assertNotNull(saved);

        // Validate
        assertEquals(ctrl.getControlId(), saved.getId().getControlId());
        assertEquals(std.getStandardId(), saved.getId().getStandardId());

        // Retrieve
        ControlRelevantStandards found = controlRelevantStandardsRepository.findById(crsId).orElse(null);
        assertNotNull(found);
        assertEquals("Test Control", found.getControl().getName());
        assertEquals("Test Standard", found.getStandard().getName());
    }
}
