package com.tecnocampus.project.api;

import com.tecnocampus.project.domain.medication.LowStockAlert;
import com.tecnocampus.project.application.services.LowStockAlertService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alerts")
public class LowStockAlertRestController {
    private final LowStockAlertService alertService;

    public LowStockAlertRestController(LowStockAlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<List<LowStockAlert>> getAllAlerts(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(alertService.getAlerts(status));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LowStockAlert> updateAlertStatus(@PathVariable Long id, @RequestBody LowStockAlert body) {
        LowStockAlert updated = alertService.updateAlertStatus(id, body.getStatus(), body.getNote());
        return ResponseEntity.ok(updated);
    }
}
