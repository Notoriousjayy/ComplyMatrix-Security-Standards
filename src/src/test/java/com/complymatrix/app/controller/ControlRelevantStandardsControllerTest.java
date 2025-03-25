package com.complymatrix.app.controller;

import com.complymatrix.app.entity.ControlRelevantStandards;
import com.complymatrix.app.service.ControlRelevantStandardsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControlRelevantStandardsController.class)
class ControlRelevantStandardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ControlRelevantStandardsService service;

    @Test
    @DisplayName("GET /api/v1/control-relevant-standards returns list")
    void listAllTest() throws Exception {
        ControlRelevantStandards crs = new ControlRelevantStandards();
        when(service.findAll()).thenReturn(Collections.singletonList(crs));

        mockMvc.perform(get("/api/v1/control-relevant-standards"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/v1/control-relevant-standards creates link")
    void createTest() throws Exception {
        ControlRelevantStandards link = new ControlRelevantStandards();
        when(service.create(any(ControlRelevantStandards.class))).thenReturn(link);

        mockMvc.perform(post("/api/v1/control-relevant-standards")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{}"))
               .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /api/v1/control-relevant-standards/{controlId}/{standardId} returns one if found")
    void getOneTest() throws Exception {
        ControlRelevantStandards found = new ControlRelevantStandards();
        when(service.findById(1L, 2L)).thenReturn(Optional.of(found));

        mockMvc.perform(get("/api/v1/control-relevant-standards/1/2"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/v1/control-relevant-standards/{controlId}/{standardId} returns 404 if not found")
    void getOneNotFoundTest() throws Exception {
        when(service.findById(1L, 2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/control-relevant-standards/1/2"))
               .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/control-relevant-standards/{controlId}/{standardId} returns 204 if successful")
    void deleteSuccessTest() throws Exception {
        doNothing().when(service).delete(1L, 2L);

        mockMvc.perform(delete("/api/v1/control-relevant-standards/1/2"))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/control-relevant-standards/{controlId}/{standardId} returns 404 if not found")
    void deleteNotFoundTest() throws Exception {
        doThrow(new RuntimeException("Not found")).when(service).delete(1L, 2L);

        mockMvc.perform(delete("/api/v1/control-relevant-standards/1/2"))
               .andExpect(status().isNotFound());
    }
}
