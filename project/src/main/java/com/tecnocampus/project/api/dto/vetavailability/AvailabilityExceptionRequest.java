package com.tecnocampus.project.api.dto.vetavailability;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record AvailabilityExceptionRequest(
        @NotNull LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        boolean isAvailable,
        String reason
) {}
