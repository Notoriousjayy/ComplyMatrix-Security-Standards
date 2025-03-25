package com.complymatrix.app.repository;

import com.complymatrix.app.BasePostgresContainerTest;
import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.entity.OrganizationStandardsId;
import com.complymatrix.app.entity.Organizations;
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
class OrganizationStandardsRepositoryTest extends BasePostgresContainerTest {
    
    @Autowired
    private OrganizationStandardsRepository organizationStandardsRepository;

    @Autowired
    private OrganizationsRepository organizationsRepository;

    @Autowired
    private StandardsRepository standardsRepository;

    @Test
    void testSaveOrganizationStandards() {
        // 1) Create an Organization
        Organizations org = new Organizations();
        org.setName("Bridging Co");
        org = organizationsRepository.save(org);

        // 2) Create a Standard
        Standards std = new Standards();
        std.setName("Bridging Standard");
        std = standardsRepository.save(std);

        // 3) Create bridging entity
        OrganizationStandardsId id = new OrganizationStandardsId();
        id.setOrganizationId(org.getOrganizationId());
        id.setStandardId(std.getStandardId());

        OrganizationStandards orgStd = new OrganizationStandards();
        orgStd.setId(id);
        orgStd.setOrganization(org);
        orgStd.setStandard(std);

        // 4) Save
        OrganizationStandards saved = organizationStandardsRepository.save(orgStd);

        // 5) Validate
        assertNotNull(saved);
        assertEquals(org.getOrganizationId(), saved.getId().getOrganizationId());
        assertEquals(std.getStandardId(), saved.getId().getStandardId());

        // 6) Retrieve and assert
        OrganizationStandards found = organizationStandardsRepository.findById(id).orElse(null);
        assertNotNull(found);
        assertEquals("Bridging Co", found.getOrganization().getName());
        assertEquals("Bridging Standard", found.getStandard().getName());
    }
}

