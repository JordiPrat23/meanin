package com.tecnocampus.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.project.domain.medication.MedicationBatch;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MedicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getMedicationBatches_shouldReturnList() throws Exception {
        mockMvc.perform(get("/medications/1/batches"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void addMedicationBatch_shouldCreateNewBatch() throws Exception {
        MedicationBatch batch = new MedicationBatch();
        batch.setLotNumber("TEST-001");
        batch.setExpiryDate(LocalDate.now().plusMonths(12));
        batch.setInitialQuantity(100);
        batch.setPurchasePricePerUnit(java.math.BigDecimal.valueOf(0.4));
        batch.setReceivedDate(LocalDate.now());
        batch.setStorageLocation("Test Shelf");

        mockMvc.perform(post("/medications/1/batches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(batch)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lotNumber").value("TEST-001"));
    }

    @Test
    void updateMedicationBatch_shouldModifyBatch() throws Exception {
        MedicationBatch updated = new MedicationBatch();
        updated.setExpiryDate(LocalDate.now().plusMonths(18));
        updated.setCurrentQuantity(90);
        updated.setStorageLocation("New Location");

        mockMvc.perform(put("/medications/batches/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storageLocation").value("New Location"));
    }

    @Test
    void deleteMedicationBatch_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/medications/batches/1"))
                .andExpect(status().isNoContent());
    }
}
