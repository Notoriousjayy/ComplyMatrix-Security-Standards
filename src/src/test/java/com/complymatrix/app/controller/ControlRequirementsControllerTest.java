package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ControlRequirements;
import com.complymatrix.app.service.ControlRequirementsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControlRequirementsController.class)
class ControlRequirementsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ControlRequirementsService service;

    @Test
    @DisplayName("GET /api/v1/control-requirements returns list")
    void listAllTest() throws Exception {
        ControlRequirements cr = new ControlRequirements();
        cr.setControlId(10L);

        when(service.findAll()).thenReturn(Collections.singletonList(cr));

        mockMvc.perform(get("/api/v1/control-requirements"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].controlId").value(10L));
    }

    @Test
    @DisplayName("GET /api/v1/control-requirements/{id} returns one if found")
    void getOneTest() throws Exception {
        ControlRequirements cr = new ControlRequirements();
        cr.setControlId(20L);

        when(service.findById(20L)).thenReturn(Optional.of(cr));

        mockMvc.perform(get("/api/v1/control-requirements/20"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.controlId").value(20L));
    }

    @Test
    @DisplayName("POST /api/v1/control-requirements creates new CR")
    void createTest() throws Exception {
        ControlRequirements toSave = new ControlRequirements();
        toSave.setName("Test CR");

        ControlRequirements saved = new ControlRequirements();
        saved.setControlId(1L);
        saved.setName("Test CR");

        when(service.create(any(ControlRequirements.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/control-requirements")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"Test CR\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.controlId").value(1L));
    }

    @Test
    @DisplayName("DELETE /api/v1/control-requirements/{id} returns 204 if successful")
    void deleteTest() throws Exception {
        doNothing().when(service).delete(30L);

        mockMvc.perform(delete("/api/v1/control-requirements/30"))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/control-requirements/{id} returns 404 if not found")
    void deleteNotFoundTest() throws Exception {
        doThrow(new RuntimeException("Not found")).when(service).delete(300L);

        mockMvc.perform(delete("/api/v1/control-requirements/300"))
               .andExpect(status().isNotFound());
    }
}
