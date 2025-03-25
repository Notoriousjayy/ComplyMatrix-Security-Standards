package com.complymatrix.app.repository;

import com.complymatrix.app.BasePostgresContainerTest;
import com.complymatrix.app.entity.Standards;
import com.complymatrix.app.entity.TrainingResources;

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
class TrainingResourcesRepositoryTest extends BasePostgresContainerTest {

    @Autowired
    private TrainingResourcesRepository trainingResourcesRepository;

    @Autowired
    private StandardsRepository standardsRepository;

    @Test
    void testSaveAndFind() {
        // Create a Standard
        Standards standard = new Standards();
        standard.setName("PCI-DSS");
        standard = standardsRepository.save(standard);

        // Create a TrainingResources entity
        TrainingResources resource = new TrainingResources();
        resource.setName("PCI Training 101");
        resource.setDescription("Introductory course on PCI-DSS requirements.");
        resource.setProvider("SecurityCorp");
        resource.setType("Online Course");
        resource.setUrl("http://example.com/pci-course");
        resource.setStandard(standard);

        // Save
        TrainingResources savedResource = trainingResourcesRepository.save(resource);
        assertNotNull(savedResource.getResourceId());

        // Retrieve
        TrainingResources found = trainingResourcesRepository.findById(savedResource.getResourceId())
                .orElse(null);
        assertNotNull(found);
        assertEquals("PCI Training 101", found.getName());
        assertEquals("SecurityCorp", found.getProvider());
        assertEquals(standard.getStandardId(), found.getStandard().getStandardId());
    }
}
