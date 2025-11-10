package com.tecnocampus.project.domain;

import com.tecnocampus.project.domain.vetavailability.Availability;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VetAvailabilityDomainTest {

    @Test
    void testAvailabilityCreation() {
        Availability availability = new Availability(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        assertEquals(DayOfWeek.MONDAY, availability.getDayOfWeek());
        assertEquals(LocalTime.of(9, 0), availability.getStartTime());
        assertEquals(LocalTime.of(17, 0), availability.getEndTime());
    }

    @Test
    void testAvailabilityUpdate() {
        Availability availability = new Availability(DayOfWeek.TUESDAY, LocalTime.of(10, 0), LocalTime.of(18, 0));
        availability.setStartTime(LocalTime.of(8, 0));
        assertEquals(LocalTime.of(8, 0), availability.getStartTime());
    }
    // ...más tests según la lógica de Availability...
}
