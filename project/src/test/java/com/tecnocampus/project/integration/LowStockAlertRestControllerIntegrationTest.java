package com.tecnocampus.project.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LowStockAlertRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllAlerts() throws Exception {
        mockMvc.perform(get("/alerts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("OPEN"));
    }

    @Test
    void testUpdateAlertStatus() throws Exception {
        String updateJson = "{" +
                "\"status\": \"ACKNOWLEDGED\"," +
                "\"note\": \"Pedido realizado\"}";
        mockMvc.perform(patch("/alerts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACKNOWLEDGED"))
                .andExpect(jsonPath("$.note").value("Pedido realizado"));
    }
}

