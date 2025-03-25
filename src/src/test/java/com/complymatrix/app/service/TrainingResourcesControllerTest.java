package com.complymatrix.app.controller;

import com.complymatrix.app.entity.TrainingResources;
import com.complymatrix.app.service.TrainingResourcesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TrainingResourcesController.class)
class TrainingResourcesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingResourcesService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetOneFound() throws Exception {
        TrainingResources tr = new TrainingResources();
        tr.setResourceId(1L);
        tr.setName("Test Resource");
        when(service.findById(1L)).thenReturn(Optional.of(tr));

        mockMvc.perform(get("/api/v1/training-resources/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resourceId").value(1))
                .andExpect(jsonPath("$.name").value("Test Resource"));
    }

    @Test
    void testGetOneNotFound() throws Exception {
        when(service.findById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/training-resources/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        TrainingResources tr = new TrainingResources();
        tr.setName("New Resource");

        TrainingResources created = new TrainingResources();
        created.setResourceId(1L);
        created.setName("New Resource");

        when(service.create(Mockito.any(TrainingResources.class))).thenReturn(created);

        mockMvc.perform(post("/api/v1/training-resources")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tr)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resourceId").value(1))
                .andExpect(jsonPath("$.name").value("New Resource"));
    }

    @Test
    void testDeleteNotFound() throws Exception {
        Mockito.doThrow(new RuntimeException("TrainingResource not found")).when(service).delete(1L);
        mockMvc.perform(delete("/api/v1/training-resources/1"))
                .andExpect(status().isNotFound());
    }
}
