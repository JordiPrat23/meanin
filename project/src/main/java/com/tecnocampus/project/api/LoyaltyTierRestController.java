package com.tecnocampus.project.api;

import com.tecnocampus.project.application.services.LoyaltyTierService;
import com.tecnocampus.project.domain.loyalty.LoyaltyTier;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loyalty-tiers")
public class LoyaltyTierRestController {

    private final LoyaltyTierService loyaltyTierService;

    public LoyaltyTierRestController(LoyaltyTierService loyaltyTierService) {
        this.loyaltyTierService = loyaltyTierService;
    }

    @GetMapping
    public ResponseEntity<List<LoyaltyTier>> getAllTiers() {
        return ResponseEntity.ok(loyaltyTierService.getAll());
    }

    @PostMapping
    public ResponseEntity<LoyaltyTier> createTier(@Valid @RequestBody LoyaltyTier tier) {
        LoyaltyTier created = loyaltyTierService.createTier(tier);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoyaltyTier> updateTier(
            @PathVariable("id") Long id,
            @Valid @RequestBody LoyaltyTier tier) {
        LoyaltyTier updated = loyaltyTierService.updateTier(id, tier);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTier(@PathVariable("id") Long id) {
        loyaltyTierService.deleteTier(id);
    }
}

