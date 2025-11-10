package com.tecnocampus.project.api.dto.vetavailability;

import java.util.List;

public record VetAvailabilityResponse(
        Long vetId,
        List<AvailabilityRequest> availabilities,
        List<AvailabilityExceptionRequest> exceptions
) {}
