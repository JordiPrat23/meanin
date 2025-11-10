package com.tecnocampus.project.api.dto.sale;

import java.math.BigDecimal;
import com.tecnocampus.project.domain.invoice.Invoice;

public record SaleResponse(
        Long invoiceId,
        BigDecimal subtotal,
        BigDecimal discountAmount,
        BigDecimal finalAmount,
        Invoice.Status status
) {}

