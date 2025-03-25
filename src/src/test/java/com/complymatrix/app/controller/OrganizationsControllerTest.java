package com.complymatrix.app.controller;

import com.complymatrix.app.entity.Organizations;
import com.complymatrix.app.service.OrganizationsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrganizationsController.class)
class OrganizationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationsService service;

    @Test
    @DisplayName("GET /api/v1/organizations returns a list")
    void listAllTest() throws Exception {
        Organizations org = new Organizations();
        org.setOrganizationId(1L);
        org.setName("Acme Corp");

        when(service.findAll()).thenReturn(Collections.singletonList(org));

        mockMvc.perform(get("/api/v1/organizations"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].organizationId").value(1L))
               .andExpect(jsonPath("$[0].name").value("Acme Corp"));
    }

    @Test
    @DisplayName("GET /api/v1/organizations/{id} returns one if found")
    void getOneTest() throws Exception {
        Organizations org = new Organizations();
        org.setOrganizationId(2L);
        org.setName("Beta LLC");

        when(service.findById(2L)).thenReturn(Optional.of(org));

        mockMvc.perform(get("/api/v1/organizations/2"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.organizationId").value(2L));
    }

    @Test
    @DisplayName("POST /api/v1/organizations creates new organization")
    void createTest() throws Exception {
        Organizations toSave = new Organizations();
        toSave.setName("Gamma Inc");

        Organizations saved = new Organizations();
        saved.setOrganizationId(3L);
        saved.setName("Gamma Inc");

        when(service.create(any(Organizations.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/organizations")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"Gamma Inc\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.organizationId").value(3L))
               .andExpect(jsonPath("$.name").value("Gamma Inc"));
    }

    @Test
    @DisplayName("DELETE /api/v1/organizations/{id} returns 204 if successful")
    void deleteTest() throws Exception {
        doNothing().when(service).delete(4L);

        mockMvc.perform(delete("/api/v1/organizations/4"))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/organizations/{id} returns 404 if not found")
    void deleteNotFoundTest() throws Exception {
        doThrow(new RuntimeException("Not found")).when(service).delete(400L);

        mockMvc.perform(delete("/api/v1/organizations/400"))
               .andExpect(status().isNotFound());
    }
}
