package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ComplianceTracking;
import com.complymatrix.app.service.ComplianceTrackingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ComplianceTrackingController.class)
class ComplianceTrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComplianceTrackingService service;

    @Test
    @DisplayName("GET /api/v1/compliance-tracking should return a list of ComplianceTracking")
    void listAllTest() throws Exception {
        ComplianceTracking tracking = new ComplianceTracking();
        tracking.setTrackingId(10L);

        when(service.findAll()).thenReturn(Collections.singletonList(tracking));

        mockMvc.perform(get("/api/v1/compliance-tracking"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].trackingId").value(10L));
    }

    @Test
    @DisplayName("GET /api/v1/compliance-tracking/{id} should return one if found")
    void getOneTest() throws Exception {
        ComplianceTracking tracking = new ComplianceTracking();
        tracking.setTrackingId(20L);

        when(service.findById(20L)).thenReturn(Optional.of(tracking));

        mockMvc.perform(get("/api/v1/compliance-tracking/20"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.trackingId").value(20L));
    }

    @Test
    @DisplayName("POST /api/v1/compliance-tracking should create a new ComplianceTracking")
    void createTest() throws Exception {
        ComplianceTracking toSave = new ComplianceTracking();
        toSave.setComplianceStatus("PASS");

        ComplianceTracking saved = new ComplianceTracking();
        saved.setTrackingId(99L);
        saved.setComplianceStatus("PASS");

        when(service.create(any(ComplianceTracking.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/compliance-tracking")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"complianceStatus\":\"PASS\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.trackingId").value(99L))
               .andExpect(jsonPath("$.complianceStatus").value("PASS"));
    }

    @Test
    @DisplayName("DELETE /api/v1/compliance-tracking/{id} should return 204 if delete successful")
    void deleteTest() throws Exception {
        doNothing().when(service).delete(30L);

        mockMvc.perform(delete("/api/v1/compliance-tracking/30"))
               .andExpect(status().isNoContent());

        verify(service, times(1)).delete(30L);
    }

    @Test
    @DisplayName("DELETE /api/v1/compliance-tracking/{id} should return 404 if not found")
    void deleteNotFoundTest() throws Exception {
        doThrow(new RuntimeException("Not found")).when(service).delete(300L);

        mockMvc.perform(delete("/api/v1/compliance-tracking/300"))
               .andExpect(status().isNotFound());
    }
}
