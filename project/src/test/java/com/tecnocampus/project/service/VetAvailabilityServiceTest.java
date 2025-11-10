package com.tecnocampus.project.service;

import com.tecnocampus.project.application.services.VetAvailabilityService;
import com.tecnocampus.project.domain.vetavailability.Availability;
import com.tecnocampus.project.domain.vetavailability.ExceptionAvailability;
import com.tecnocampus.project.domain.vetavailability.Vet;
import com.tecnocampus.project.persistance.AvailabilityRepository;
import com.tecnocampus.project.persistance.ExceptionAvailabilityRepository;
import com.tecnocampus.project.persistance.VetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VetAvailabilityServiceTest {

    private AvailabilityRepository availabilityRepository;
    private ExceptionAvailabilityRepository exceptionAvailabilityRepository;
    private VetAvailabilityService service;
    private Vet vet;

    @BeforeEach
    void setUp() {
        availabilityRepository = mock(AvailabilityRepository.class);
        exceptionAvailabilityRepository = mock(ExceptionAvailabilityRepository.class);
        VetRepository vetRepository = mock(VetRepository.class);
        service = new VetAvailabilityService(availabilityRepository, exceptionAvailabilityRepository, vetRepository);
        vet = new Vet();
        vet.setName("Dr. Test");
    }

    @Test
    void addAvailability_shouldSaveToRepo() {
        Availability availability = new Availability(DayOfWeek.MONDAY, LocalTime.of(9,0), LocalTime.of(17,0));

        when(availabilityRepository.save(any())).thenReturn(availability);

        Availability saved = service.addAvailability(vet, availability);
        assertNotNull(saved);
        verify(availabilityRepository, times(1)).save(availability);
    }

    @Test
    void deleteAvailability_shouldCallRepoDelete() {
        Availability a = new Availability(DayOfWeek.MONDAY, LocalTime.of(9,0), LocalTime.of(17,0));
        vet.addAvailability(a);

        service.deleteAvailability(vet, a);

        verify(availabilityRepository, times(1)).delete(a);
    }

    @Test
    void addException_shouldSaveException() {
        ExceptionAvailability ex = new ExceptionAvailability();
        ex.setDate(LocalDate.now().plusDays(1));
        ex.setAvailable(false);
        ex.setReason("Conference");

        when(exceptionAvailabilityRepository.save(any())).thenReturn(ex);

        ExceptionAvailability created = service.addException(vet, ex);
        assertNotNull(created);
        verify(exceptionAvailabilityRepository, times(1)).save(ex);
    }

    @Test
    void getAvailabilities_shouldReturnList() {
        when(availabilityRepository.findByVet(vet)).thenReturn(List.of());
        assertTrue(service.getAvailabilities(vet).isEmpty());
    }
}
