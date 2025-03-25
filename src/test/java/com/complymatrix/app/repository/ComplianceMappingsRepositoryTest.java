package com.complymatrix.app.repository;

import com.complymatrix.app.BasePostgresContainerTest;
import com.complymatrix.app.entity.ComplianceMappings;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(LiquibaseAutoConfiguration.class)
class ComplianceMappingsRepositoryTest extends BasePostgresContainerTest {

    @Autowired
    private ComplianceMappingsRepository complianceMappingsRepository;
    @Autowired
    private StandardsRepository standardsRepository;
    @Autowired
    private ControlRequirementsRepository controlRequirementsRepository;

    @Test
    void testCreateMapping() {
        // 1) Create & save Standards
        Standards sourceStd = new Standards();
        sourceStd.setName("Source Standard");
        sourceStd = standardsRepository.save(sourceStd);

        Standards targetStd = new Standards();
        targetStd.setName("Target Standard");
        targetStd = standardsRepository.save(targetStd);

        // 2) Create & save ControlRequirements
        ControlRequirements sourceControl = new ControlRequirements();
        sourceControl.setName("Source Control");
        sourceControl.setStandard(sourceStd);
        sourceControl = controlRequirementsRepository.save(sourceControl);

        ControlRequirements targetControl = new ControlRequirements();
        targetControl.setName("Target Control");
        targetControl.setStandard(targetStd);
        targetControl = controlRequirementsRepository.save(targetControl);

        // 3) Create & save a ComplianceMappings entity
        ComplianceMappings mapping = new ComplianceMappings();
        mapping.setSourceStandard(sourceStd);
        mapping.setSourceControl(sourceControl);
        mapping.setTargetStandard(targetStd);
        mapping.setTargetControl(targetControl);
        mapping.setMappingDescription("Example mapping from source to target.");

        // 4) Verify the mapping got persisted
        ComplianceMappings savedMapping = complianceMappingsRepository.save(mapping);
        assertNotNull(savedMapping.getMappingId());

        // 5) Retrieve it back & verify
        ComplianceMappings found =
                complianceMappingsRepository.findById(savedMapping.getMappingId())
                        .orElse(null);

        assertNotNull(found);
        assertEquals("Example mapping from source to target.", found.getMappingDescription());
        assertEquals("Source Control", found.getSourceControl().getName());
        assertEquals("Target Control", found.getTargetControl().getName());
    }
}
