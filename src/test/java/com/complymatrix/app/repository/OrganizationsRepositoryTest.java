package com.complymatrix.app.repository;

import com.complymatrix.app.BasePostgresContainerTest;
import com.complymatrix.app.entity.Organizations;
import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.entity.OrganizationStandardsId;
import com.complymatrix.app.entity.Standards;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(LiquibaseAutoConfiguration.class)
class OrganizationsRepositoryTest extends BasePostgresContainerTest {

    @Autowired
    private OrganizationsRepository organizationsRepository;

    @Autowired
    private StandardsRepository standardsRepository;

    @Autowired
    private OrganizationStandardsRepository organizationStandardsRepository;

    @Test
    void testSaveAndFindById() {
        // Create
        Organizations org = new Organizations();
        org.setName("ACME Corp");
        org.setIndustry("Manufacturing");
        org.setRegion("North America");
        org.setComplianceScore(new BigDecimal("85.50"));

        // Save
        Organizations saved = organizationsRepository.save(org);
        assertNotNull(saved.getOrganizationId());

        // Retrieve
        Organizations found = organizationsRepository.findById(saved.getOrganizationId()).orElse(null);
        assertNotNull(found);
        assertEquals("ACME Corp", found.getName());
        assertEquals("North America", found.getRegion());
    }

    @Test
public void testLinkOrganizationToStandard() {
    // 1) Create and save an Organization
    Organizations org = new Organizations();
    org.setName("ACME Corp");
    org = organizationsRepository.save(org);  // now org has a generated organizationId

    // 2) Create and save a Standard
    Standards std = new Standards();
    std.setName("ISO-27001");
    std = standardsRepository.save(std);      // now std has a generated standardId

    // 3) Create the composite key using the saved IDs
    OrganizationStandardsId orgStdId = new OrganizationStandardsId();
    orgStdId.setOrganizationId(org.getOrganizationId());
    orgStdId.setStandardId(std.getStandardId());

    // 4) Create the link entity and set both the id and associations
    OrganizationStandards orgStd = new OrganizationStandards();
    orgStd.setId(orgStdId);
    orgStd.setOrganization(org); // <== ensure the organization is set
    // If there's an association to Standards as well, set it:
    orgStd.setStandard(std);

    // 5) Save the link entity
    organizationStandardsRepository.save(orgStd);

    // 6) Retrieve and assert
    Optional<OrganizationStandards> fetched = organizationStandardsRepository.findById(orgStdId);
    assertTrue("The OrganizationStandards link should exist", fetched.isPresent());
}


}
