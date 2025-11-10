package com.tecnocampus.project.api.dto.visit;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScheduleVisitRequest(
        String petName,
        String ownerName,
        LocalDate date,
        LocalTime time,
        int durationMinutes,
        String reason
) {}

