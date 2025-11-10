package com.tecnocampus.project.api.dto.visit;

import java.time.LocalDate;
import java.time.LocalTime;

public record RescheduleVisitRequest(
        LocalDate newDate,
        LocalTime newTime,
        Integer newDurationMinutes // si és null, es manté la durada actual
) {}
