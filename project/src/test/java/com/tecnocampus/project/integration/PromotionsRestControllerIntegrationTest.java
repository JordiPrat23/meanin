package com.tecnocampus.project.integration;

import com.tecnocampus.project.domain.promotions.Promotion;
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
public class PromotionsRestControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetAllPromotions() throws Exception {
        mockMvc.perform(get("/promotions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Autumn Vaccination Drive"));
    }

    @Test
    void testCreatePromotion() throws Exception {
        String promoJson = "{" +
                "\"name\": \"Spring Sale\"," +
                "\"description\": \"15% off on all services\"," +
                "\"startDate\": \"2025-03-01\"," +
                "\"endDate\": \"2025-03-31\"," +
                "\"maxUses\": 100," +
                "\"promoCode\": \"SPRING15\"}";
        mockMvc.perform(post("/promotions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(promoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Spring Sale"));
    }

    @Test
    void testUpdatePromotion() throws Exception {
        String updateJson = "{" +
                "\"name\": \"Autumn Drive Updated\"," +
                "\"description\": \"25% off\"," +
                "\"startDate\": \"2025-10-01\"," +
                "\"endDate\": \"2025-11-30\"," +
                "\"maxUses\": 200," +
                "\"promoCode\": \"AUTUMN25\"}";
        mockMvc.perform(put("/promotions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Autumn Drive Updated"));
    }

    @Test
    void testDeletePromotion() throws Exception {
        mockMvc.perform(delete("/promotions/1"))
                .andExpect(status().isNoContent());
    }
}

