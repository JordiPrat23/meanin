package com.tecnocampus.project.api.dto.visit;

import java.time.LocalDate;
import java.time.LocalTime;

public record AvailableSlot(
        LocalDate date,
        LocalTime time,
        int durationMinutes
) {}

