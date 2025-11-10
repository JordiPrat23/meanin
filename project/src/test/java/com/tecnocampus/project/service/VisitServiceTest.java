package com.tecnocampus.project.service;

import com.tecnocampus.project.api.dto.visit.ScheduleVisitRequest;
import com.tecnocampus.project.application.services.VisitService;
import com.tecnocampus.project.domain.visit.Visit;
import com.tecnocampus.project.domain.visit.VisitStatus;
import com.tecnocampus.project.domain.vetavailability.Vet;
import com.tecnocampus.project.persistance.VisitRepository;
import com.tecnocampus.project.persistance.VetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VisitServiceTest {

    private VisitRepository visitRepository;
    private VetRepository vetRepository;
    private VisitService visitService;
    private Vet vet;

    @BeforeEach
    void setUp() {
        visitRepository = mock(VisitRepository.class);
        vetRepository = mock(VetRepository.class);
        visitService = new VisitService(visitRepository, vetRepository);

        vet = new Vet();
        vet.setId(1L);
        vet.setName("Dr. Test");
    }

    @Test
    void scheduleVisit_shouldSaveAndReturnVisit() {
        ScheduleVisitRequest req = new ScheduleVisitRequest("Buddy", "Alice", LocalDate.now().plusDays(1), LocalTime.of(10, 0), 15, "Checkup");

        when(vetRepository.findById(1L)).thenReturn(Optional.of(vet));
        when(visitRepository.save(any())).thenAnswer(invocation -> {
            Visit v = (Visit) invocation.getArgument(0);
            v.setId(42L);
            return v;
        });

        Visit saved = visitService.scheduleVisit(1L, req);

        assertNotNull(saved);
        assertEquals(42L, saved.getId());
        assertEquals("Buddy", saved.getPetName());
        assertEquals("Alice", saved.getOwnerName());
        assertEquals(VisitStatus.SCHEDULED, saved.getStatus());
        verify(visitRepository, times(1)).save(any());
    }

    @Test
    void getScheduleForVet_shouldReturnListBetweenDates() {
        when(vetRepository.findById(1L)).thenReturn(Optional.of(vet));

        Visit v1 = new Visit();
        v1.setId(1L);
        v1.setVet(vet);
        v1.setDate(LocalDate.now());
        v1.setTime(LocalTime.of(9,0));

        when(visitRepository.findByVetAndDateBetween(eq(vet), any(), any())).thenReturn(List.of(v1));

        List<Visit> res = visitService.getScheduleForVet(1L, LocalDate.now(), LocalDate.now().plusDays(7));

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals(v1.getId(), res.get(0).getId());
        verify(visitRepository, times(1)).findByVetAndDateBetween(eq(vet), any(), any());
    }
}

