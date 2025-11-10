package com.tecnocampus.project.api.dto.sale;

import java.util.List;

public record SaleRequest(
        Long petOwnerId, // optional
        List<SaleItemRequest> items
) {}
