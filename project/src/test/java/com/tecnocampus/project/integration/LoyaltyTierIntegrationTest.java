package com.tecnocampus.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.project.domain.loyalty.LoyaltyTier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoyaltyTierIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllTiers_shouldReturnList() throws Exception {
        mockMvc.perform(get("/loyalty-tiers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void createTier_shouldAddNewTier() throws Exception {
        LoyaltyTier tier = new LoyaltyTier();
        tier.setTierName("Platinum");
        tier.setRequiredPoints(1000);
        tier.setDiscountPercentage(BigDecimal.valueOf(15));
        tier.setBenefitsDescription("15% discount + free grooming");

        mockMvc.perform(post("/loyalty-tiers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tier)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tierName").value("Platinum"));
    }

    @Test
    void updateTier_shouldModifyTier() throws Exception {
        LoyaltyTier updated = new LoyaltyTier();
        updated.setRequiredPoints(300);
        updated.setDiscountPercentage(BigDecimal.valueOf(7));
        updated.setBenefitsDescription("Updated benefits");

        mockMvc.perform(put("/loyalty-tiers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.requiredPoints").value(300));
    }

    @Test
    void deleteTier_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/loyalty-tiers/1"))
                .andExpect(status().isNoContent());
    }
}
