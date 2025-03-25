package com.complymatrix.app.repository;

import com.complymatrix.app.BasePostgresContainerTest;
import com.complymatrix.app.entity.Standards;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ImportAutoConfiguration(LiquibaseAutoConfiguration.class)
class StandardsRepositoryTest extends BasePostgresContainerTest {

    @Autowired
    private StandardsRepository standardsRepository;

    @Test
    void testSaveAndFind() {
        Standards s = new Standards();
        s.setName("ISO 27001");
        s.setCategory("Information Security");
        s.setDescription("International standard for info sec management.");
        s.setUrl("https://example.com/iso27001");
        s.setPublisher("ISO");
        s.setFocusArea("Risk Management");
        s.setRegion("Global");

        // Save a new Standards record
        Standards saved = standardsRepository.save(s);
        assertThat(saved.getStandardId()).isNotNull();

        // Verify the record was stored
        List<Standards> all = standardsRepository.findAll();
        assertThat(all).hasSize(1);
        Standards std = all.get(0);
        assertThat(std.getName()).isEqualTo("ISO 27001");
        // Additional assertions as needed
    }

    @BeforeEach
    void cleanUpStandardsTable() {
        standardsRepository.deleteAll(); 
    }

}
