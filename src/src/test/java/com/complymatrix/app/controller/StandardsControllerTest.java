package com.complymatrix.app.controller;

import com.complymatrix.app.entity.Standards;
import com.complymatrix.app.service.StandardsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StandardsController.class)
class StandardsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StandardsService standardsService;

    @Test
    @DisplayName("GET /api/v1/standards returns list")
    void listStandardsTest() throws Exception {
        Standards std = new Standards();
        std.setStandardId(10L);
        std.setName("ISO27001");

        when(standardsService.findAll()).thenReturn(Collections.singletonList(std));

        mockMvc.perform(get("/api/v1/standards"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].standardId").value(10L))
               .andExpect(jsonPath("$[0].name").value("ISO27001"));
    }

    @Test
    @DisplayName("GET /api/v1/standards/{id} returns one if found")
    void getStandardTest() throws Exception {
        Standards std = new Standards();
        std.setStandardId(20L);
        std.setName("NIST CSF");

        when(standardsService.findById(20L)).thenReturn(Optional.of(std));

        mockMvc.perform(get("/api/v1/standards/20"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.standardId").value(20L));
    }

    @Test
    @DisplayName("POST /api/v1/standards creates standard")
    void createStandardTest() throws Exception {
        Standards toSave = new Standards();
        toSave.setName("PCI DSS");

        Standards saved = new Standards();
        saved.setStandardId(1L);
        saved.setName("PCI DSS");

        when(standardsService.createStandard(any(Standards.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/standards")
               .contentType(MediaType.APPLICATION_JSON)
               .content("{\"name\":\"PCI DSS\"}"))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.standardId").value(1L))
               .andExpect(jsonPath("$.name").value("PCI DSS"));
    }

    @Test
    @DisplayName("DELETE /api/v1/standards/{id} returns 204 if successful")
    void deleteStandardTest() throws Exception {
        doNothing().when(standardsService).deleteStandard(30L);

        mockMvc.perform(delete("/api/v1/standards/30"))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/v1/standards/{id} returns 404 if not found")
    void deleteStandardNotFoundTest() throws Exception {
        doThrow(new RuntimeException("Not found")).when(standardsService).deleteStandard(300L);

        mockMvc.perform(delete("/api/v1/standards/300"))
               .andExpect(status().isNotFound());
    }
}
