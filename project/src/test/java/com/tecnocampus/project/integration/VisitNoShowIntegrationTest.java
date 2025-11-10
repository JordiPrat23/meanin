package com.tecnocampus.project.integration;

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

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VisitNoShowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VetRepository vetRepository;

    private Visit visit;

    @BeforeEach
    void setUp() {
        visitRepository.deleteAll();
        vetRepository.deleteAll();

        var vet = new Vet();
        vet.setName("Dr. Soler");
        vetRepository.save(vet);

        visit = new Visit();
        visit.setVet(vet);
        visit.setPetName("Toby");
        visit.setOwnerName("Marc Garcia");
        visit.setDate(LocalDate.now());
        visit.setTime(LocalTime.of(10, 0));
        visit.setDurationMinutes(30);
        visit.setStatus(VisitStatus.SCHEDULED);
        visitRepository.save(visit);
    }

    @Test
    void markNoShow_shouldUpdateStatusToNoShow() throws Exception {
        mockMvc.perform(post("/visits/" + visit.getId() + "/no-show")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Visit updated = visitRepository.findById(visit.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(VisitStatus.NO_SHOW);
        assertThat(updated.getEndTimestamp()).isNotNull();
    }

    @Test
    void markNoShow_shouldFailIfNotScheduled() throws Exception {
        visit.setStatus(VisitStatus.COMPLETED);
        visitRepository.save(visit);

        mockMvc.perform(post("/visits/" + visit.getId() + "/no-show")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }
}
