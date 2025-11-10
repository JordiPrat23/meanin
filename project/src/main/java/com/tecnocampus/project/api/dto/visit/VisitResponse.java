package com.tecnocampus.project.api.dto.visit;

import com.tecnocampus.project.domain.visit.VisitStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record VisitResponse(
        Long id,
        String petName,
        String ownerName,
        LocalDate date,
        LocalTime time,
        int durationMinutes,
        VisitStatus status,
        String reason
) {}

