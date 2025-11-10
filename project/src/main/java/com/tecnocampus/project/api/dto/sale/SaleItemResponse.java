package com.tecnocampus.project.api.dto.sale;

import java.math.BigDecimal;

public record SaleItemResponse(
        Long medicationId,
        String description,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal
) {}

