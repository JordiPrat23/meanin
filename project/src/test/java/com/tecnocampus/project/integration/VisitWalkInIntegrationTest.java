package com.tecnocampus.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.project.api.dto.visit.ScheduleVisitRequest;
import com.tecnocampus.project.domain.vetavailability.Vet;
import com.tecnocampus.project.domain.visit.Visit;
import com.tecnocampus.project.domain.visit.VisitStatus;
import com.tecnocampus.project.persistance.VetRepository;
import com.tecnocampus.project.persistance.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VisitWalkInIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Vet vet;

    @BeforeEach
    void setUp() {
        visitRepository.deleteAll();
        vetRepository.deleteAll();

        vet = new Vet();
        vet.setName("Dr. Martí");
        vetRepository.save(vet);
    }

    @Test
    void createWalkInVisit_shouldCreateVisitInProgress() throws Exception {
        ScheduleVisitRequest body = new ScheduleVisitRequest(
                "Luna", "Jordi Sala", null, null, 0, "Emergency");

        mockMvc.perform(post("/vets/" + vet.getId() + "/visits/walkin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());

        Visit visit = visitRepository.findAll().get(0);
        assertThat(visit.getVet().getId()).isEqualTo(vet.getId());
        assertThat(visit.getPetName()).isEqualTo("Luna");
        assertThat(visit.getOwnerName()).isEqualTo("Jordi Sala");
        assertThat(visit.getReason()).isEqualTo("Emergency");
        assertThat(visit.getStatus()).isEqualTo(VisitStatus.IN_PROGRESS);
        assertThat(visit.getDate()).isNotNull();
        assertThat(visit.getTime()).isNotNull();
    }

    @Test
    void createWalkInVisit_withoutBody_shouldUseDefaults() throws Exception {
        mockMvc.perform(post("/vets/" + vet.getId() + "/visits/walkin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        Visit visit = visitRepository.findAll().get(0);
        assertThat(visit.getPetName()).isEqualTo("Unknown Pet");
        assertThat(visit.getOwnerName()).isEqualTo("Unknown Owner");
        assertThat(visit.getReason()).isEqualTo("Walk-in visit");
        assertThat(visit.getStatus()).isEqualTo(VisitStatus.IN_PROGRESS);
    }
}
