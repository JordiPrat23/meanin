package com.tecnocampus.project.api.dto.visit;

import java.util.List;

public record VetScheduleResponse(
        List<VisitResponse> visits,
        List<AvailableSlot> availableSlots
) {}

