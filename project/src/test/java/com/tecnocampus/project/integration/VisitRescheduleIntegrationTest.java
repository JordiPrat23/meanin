package com.tecnocampus.project.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecnocampus.project.api.dto.visit.RescheduleVisitRequest;
import com.tecnocampus.project.domain.vetavailability.Availability;
import com.tecnocampus.project.domain.vetavailability.Vet;
import com.tecnocampus.project.domain.visit.Visit;
import com.tecnocampus.project.domain.visit.VisitStatus;
import com.tecnocampus.project.persistance.AvailabilityRepository;
import com.tecnocampus.project.persistance.VisitRepository;
import com.tecnocampus.project.persistance.VetRepository;
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
class    VisitRescheduleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitRepository visitRepository;

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Visit visit;

    @BeforeEach
    void setUp() {

        visitRepository.deleteAll();
        vetRepository.deleteAll();
        availabilityRepository.deleteAll();


        var vet = new Vet();
        vet.setName("Dr. Ferrer");
        vetRepository.save(vet);


        var availability1 = new Availability();
        availability1.setVet(vet);
        availability1.setDayOfWeek(LocalDate.now().plusDays(2).getDayOfWeek());
        availability1.setStartTime(LocalTime.of(8, 0));
        availability1.setEndTime(LocalTime.of(18, 0));
        availabilityRepository.save(availability1);


        var availability2 = new Availability();
        availability2.setVet(vet);
        availability2.setDayOfWeek(LocalDate.now().plusDays(3).getDayOfWeek());
        availability2.setStartTime(LocalTime.of(8, 0));
        availability2.setEndTime(LocalTime.of(18, 0));
        availabilityRepository.save(availability2);


        visit = new Visit();
        visit.setVet(vet);
        visit.setPetName("Rocky");
        visit.setOwnerName("Laura Riera");
        visit.setDate(LocalDate.now().plusDays(1));
        visit.setTime(LocalTime.of(9, 0));
        visit.setDurationMinutes(30);
        visit.setStatus(VisitStatus.SCHEDULED);
        visit.setReason("Vacunes anuals");
        visitRepository.save(visit);
    }

    @Test
    void rescheduleVisit_shouldUpdateDateTimeAndDuration() throws Exception {
        var request = new RescheduleVisitRequest(
                LocalDate.now().plusDays(2),
                LocalTime.of(11, 0),
                45
        );

        mockMvc.perform(put("/visits/" + visit.getId() + "/reschedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Visit updated = visitRepository.findById(visit.getId()).orElseThrow();
        assertThat(updated.getDate()).isEqualTo(LocalDate.now().plusDays(2));
        assertThat(updated.getTime()).isEqualTo(LocalTime.of(11, 0));
        assertThat(updated.getDurationMinutes()).isEqualTo(45);
        assertThat(updated.getStatus()).isEqualTo(VisitStatus.SCHEDULED);
    }

    @Test
    void rescheduleVisit_shouldKeepOriginalDurationIfNotProvided() throws Exception {
        var request = new RescheduleVisitRequest(
                LocalDate.now().plusDays(3),
                LocalTime.of(15, 30),
                null
        );

        mockMvc.perform(put("/visits/" + visit.getId() + "/reschedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        Visit updated = visitRepository.findById(visit.getId()).orElseThrow();
        assertThat(updated.getDate()).isEqualTo(LocalDate.now().plusDays(3));
        assertThat(updated.getTime()).isEqualTo(LocalTime.of(15, 30));
        assertThat(updated.getDurationMinutes()).isEqualTo(30); // manté la durada original
    }

    @Test
    void rescheduleVisit_shouldFailIfVisitNotScheduled() throws Exception {
        visit.setStatus(VisitStatus.COMPLETED);
        visitRepository.save(visit);

        var request = new RescheduleVisitRequest(
                LocalDate.now().plusDays(2),
                LocalTime.of(14, 0),
                60
        );

        mockMvc.perform(put("/visits/" + visit.getId() + "/reschedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // 400 esperat
    }
}
