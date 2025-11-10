package com.tecnocampus.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.project.api.dto.visit.CancelVisitRequest;
import com.tecnocampus.project.domain.visit.Visit;
import com.tecnocampus.project.domain.visit.VisitStatus;
import com.tecnocampus.project.persistance.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VisitRestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Visit visit;

    @BeforeEach
    void setUp() {
        visitRepository.deleteAll();

        visit = new Visit();
        visit.setPetName("Rex");
        visit.setOwnerName("Anna Puig");
        visit.setDate(LocalDate.now().plusDays(1));
        visit.setTime(LocalTime.of(10, 0));
        visit.setDurationMinutes(30);
        visit.setStatus(VisitStatus.SCHEDULED);
        visit.setReason("Revisió general");
        visitRepository.save(visit);
    }

    @Test
    void cancelVisitWithReason_shouldSetCancelledStatusAndSaveReason() throws Exception {
        var request = new CancelVisitRequest("El propietari no pot assistir");

        mockMvc.perform(put("/visits/" + visit.getId() + "/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNoContent());

        Visit updated = visitRepository.findById(visit.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(VisitStatus.CANCELLED);
        assertThat(updated.getReason()).isEqualTo("El propietari no pot assistir");
    }

    @Test
    void cancelVisitWithoutBody_shouldCancelWithoutReason() throws Exception {
        mockMvc.perform(put("/visits/" + visit.getId() + "/cancel"))
                .andExpect(status().isNoContent());

        Visit updated = visitRepository.findById(visit.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(VisitStatus.CANCELLED);
        assertThat(updated.getReason()).isEqualTo("Revisió general"); // no canvia
    }
}
