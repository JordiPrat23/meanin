package com.tecnocampus.project.api.dto.sale;

public record SaleItemRequest(
        Long medicationId,
        int quantity
) {}
