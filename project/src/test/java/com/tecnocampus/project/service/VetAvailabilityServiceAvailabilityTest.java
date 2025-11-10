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

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VetAvailabilityServiceAvailabilityTest {

    private AvailabilityRepository availabilityRepository;
    private ExceptionAvailabilityRepository exceptionAvailabilityRepository;
    private VetRepository vetRepository;
    private VetAvailabilityService service;
    private Vet vet;

    @BeforeEach
    void setUp() {
        availabilityRepository = mock(AvailabilityRepository.class);
        exceptionAvailabilityRepository = mock(ExceptionAvailabilityRepository.class);
        vetRepository = mock(VetRepository.class);
        service = new VetAvailabilityService(availabilityRepository, exceptionAvailabilityRepository, vetRepository);

        vet = new Vet();
        vet.setId(1L);
        vet.setName("Dr. Test");

        when(vetRepository.findById(1L)).thenReturn(Optional.of(vet));
    }

    @Test
    void wholeDayBlockedByException_shouldReturnUnavailable() {
        Availability base = new Availability(DayOfWeek.MONDAY, LocalTime.of(9,0), LocalTime.of(17,0));
        base.setVet(vet);
        when(availabilityRepository.findByVet(vet)).thenReturn(List.of(base));

        LocalDate targetDate = LocalDate.of(2025,10,20); // Monday
        ExceptionAvailability ex = new ExceptionAvailability();
        ex.setDate(targetDate);
        ex.setAvailable(false);
        ex.setVet(vet);
        when(exceptionAvailabilityRepository.findByVet(vet)).thenReturn(List.of(ex));

        boolean available = service.isVetAvailable(1L, targetDate, LocalTime.of(10,0));
        assertFalse(available, "Vet should be unavailable due to whole-day exception");
    }

    @Test
    void partialExceptionBlocksOnlyWindow() {
        Availability base = new Availability(DayOfWeek.MONDAY, LocalTime.of(9,0), LocalTime.of(17,0));
        base.setVet(vet);
        when(availabilityRepository.findByVet(vet)).thenReturn(List.of(base));

        LocalDate targetDate = LocalDate.of(2025,10,20); // Monday
        ExceptionAvailability ex = new ExceptionAvailability();
        ex.setDate(targetDate);
        ex.setAvailable(false);
        ex.setStartTime(LocalTime.of(12,0));
        ex.setEndTime(LocalTime.of(14,0));
        ex.setVet(vet);
        when(exceptionAvailabilityRepository.findByVet(vet)).thenReturn(List.of(ex));

        assertFalse(service.isVetAvailable(1L, targetDate, LocalTime.of(13,0)), "Should be blocked during exception window");
        assertTrue(service.isVetAvailable(1L, targetDate, LocalTime.of(11,0)), "Should be available before exception window");
        assertTrue(service.isVetAvailable(1L, targetDate, LocalTime.of(15,0)), "Should be available after exception window");
    }

    @Test
    void exceptionAllowsWhenBaseNotAvailable() {
        when(availabilityRepository.findByVet(vet)).thenReturn(List.of());

        LocalDate sunday = LocalDate.of(2025,10,19); // Sunday
        ExceptionAvailability ex = new ExceptionAvailability();
        ex.setDate(sunday);
        ex.setAvailable(true); // available whole day
        ex.setVet(vet);
        when(exceptionAvailabilityRepository.findByVet(vet)).thenReturn(List.of(ex));

        assertTrue(service.isVetAvailable(1L, sunday, LocalTime.of(10,0)), "Exception should allow availability even if no base availability");
    }
}

