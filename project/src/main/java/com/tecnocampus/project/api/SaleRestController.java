package com.tecnocampus.project.api;

import com.tecnocampus.project.api.dto.sale.SaleRequest;
import com.tecnocampus.project.api.dto.sale.SaleResponse;
import com.tecnocampus.project.application.services.SaleService;
import com.tecnocampus.project.domain.invoice.Invoice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SaleRestController {

    private final SaleService saleService;

    public SaleRestController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/sales")
    public ResponseEntity<SaleResponse> createSale(@RequestBody SaleRequest request) {
        Invoice invoice = saleService.createSale(request);
        SaleResponse resp = new SaleResponse(invoice.getId(), invoice.getSubtotal(), invoice.getDiscountAmount(), invoice.getFinalAmount(), invoice.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/invoices/{id}/pay")
    public ResponseEntity<SaleResponse> payInvoice(@PathVariable Long id) {
        Invoice invoice = saleService.payInvoice(id);
        SaleResponse resp = new SaleResponse(invoice.getId(), invoice.getSubtotal(), invoice.getDiscountAmount(), invoice.getFinalAmount(), invoice.getStatus());
        return ResponseEntity.ok(resp);
    }
}
