package com.complymatrix.app.controller;

import com.complymatrix.app.entity.OrganizationStandards;
import com.complymatrix.app.service.OrganizationStandardsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrganizationStandardsController.class)
class OrganizationStandardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationStandardsService service;

    @Test
    @DisplayName("GET /api/v1/organization-standards returns list")
    void listAllTest() throws Exception {
        when(service.findAll()).thenReturn(Collections.singletonList(new OrganizationStandards()));

        mockMvc.perform(get("/api/v1/organization-standards"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/organization-standards creates new link")
    void createTest() throws Exception {
        OrganizationStandards os = new OrganizationStandards();
        when(service.create(any(OrganizationStandards.class))).thenReturn(os);

        mockMvc.perform(post("/api/v1/organization-standards")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{}"))
               .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /api/v1/organization-standards/{orgId}/{stdId} returns one if found")
    void getOneTest() throws Exception {
        OrganizationStandards found = new OrganizationStandards();
        when(service.findById(1L, 2L)).thenReturn(Optional.of(found));

        mockMvc.perform(get("/api/v1/organization-standards/1/2"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/organization-standards/{orgId}/{stdId} returns 404 if not found")
    void getOneNotFoundTest() throws Exception {
        when(service.findById(1L, 2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/organization-standards/1/2"))
               .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/organization-standards/{orgId}/{stdId} returns 204 if successful")
    void deleteSuccessTest() throws Exception {
        doNothing().when(service).delete(1L, 2L);

        mockMvc.perform(delete("/api/v1/organization-standards/1/2"))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/organization-standards/{orgId}/{stdId} returns 404 if not found")
    void deleteNotFoundTest() throws Exception {
        doThrow(new RuntimeException("Not found")).when(service).delete(1L, 2L);

        mockMvc.perform(delete("/api/v1/organization-standards/1/2"))
               .andExpect(status().isNotFound());
    }
}
