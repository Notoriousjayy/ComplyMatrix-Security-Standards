package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ComplianceMappings;
import com.complymatrix.app.service.ComplianceMappingsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComplianceMappingsController.class)
class ComplianceMappingsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComplianceMappingsService service;

    @Test
    @DisplayName("GET /api/v1/compliance-mappings should return a list of ComplianceMappings")
    void listAllTest() throws Exception {
        ComplianceMappings cm = new ComplianceMappings();
        cm.setMappingId(100L);
        cm.setSourceStandard("Source");
        cm.setTargetStandard("Target");

        when(service.findAll()).thenReturn(Collections.singletonList(cm));

        mockMvc.perform(get("/api/v1/compliance-mappings"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].mappingId").value(100L))
               .andExpect(jsonPath("$[0].sourceStandard").value("Source"));
    }

    @Test
    @DisplayName("GET /api/v1/compliance-mappings/{id} should return one ComplianceMappings if found")
    void getOneFoundTest() throws Exception {
        ComplianceMappings cm = new ComplianceMappings();
        cm.setMappingId(200L);
        when(service.findById(200L)).thenReturn(Optional.of(cm));

        mockMvc.perform(get("/api/v1/compliance-mappings/200"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.mappingId").value(200L));
    }

    @Test
    @DisplayName("GET /api/v1/compliance-mappings/{id} should return 404 if not found")
    void getOneNotFoundTest() throws Exception {
        when(service.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/compliance-mappings/999"))
               .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/v1/compliance-mappings should create a new ComplianceMappings")
    void createTest() throws Exception {
        ComplianceMappings input = new ComplianceMappings();
        input.setSourceStandard("NewSource");

        ComplianceMappings saved = new ComplianceMappings();
        saved.setMappingId(1L);
        saved.setSourceStandard("NewSource");

        when(service.create(any(ComplianceMappings.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/compliance-mappings")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"sourceStandard\":\"NewSource\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.mappingId").value(1L))
               .andExpect(jsonPath("$.sourceStandard").value("NewSource"));
    }

    @Test
    @DisplayName("DELETE /api/v1/compliance-mappings/{id} should return 204 if delete successful")
    void deleteTest() throws Exception {
        doNothing().when(service).delete(300L);

        mockMvc.perform(delete("/api/v1/compliance-mappings/300"))
               .andExpect(status().isNoContent());

        verify(service, times(1)).delete(300L);
    }

    @Test
    @DisplayName("DELETE /api/v1/compliance-mappings/{id} should return 404 if not found")
    void deleteNotFoundTest() throws Exception {
        doThrow(new RuntimeException("Not found")).when(service).delete(999L);

        mockMvc.perform(delete("/api/v1/compliance-mappings/999"))
               .andExpect(status().isNotFound());
    }
}
