package com.tecnocampus.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VetAvailabilityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    static class AvailabilityRequest {
        public LocalTime startTime;
        public LocalTime endTime;
    }

    @Test
    void getVetAvailability_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/vets/1/availability"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void putAvailability_shouldUpdateOrCreate() throws Exception {
        AvailabilityRequest req = new AvailabilityRequest();
        req.startTime = LocalTime.of(8, 0);
        req.endTime = LocalTime.of(16, 0);

        mockMvc.perform(put("/vets/1/availability/MONDAY")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void addException_shouldReturnCreated() throws Exception {
        String body = """
            {
              "date": "2025-10-15",
              "isAvailable": false,
              "reason": "Vacation"
            }
        """;

        mockMvc.perform(post("/vets/1/exceptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}
