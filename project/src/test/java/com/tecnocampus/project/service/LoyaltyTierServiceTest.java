package com.tecnocampus.project.service;

import com.tecnocampus.project.application.services.LoyaltyTierService;
import com.tecnocampus.project.domain.loyalty.LoyaltyTier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoyaltyTierServiceTest {

    @Test
    void testFindTierByPoints() {
        LoyaltyTierService service = mock(LoyaltyTierService.class);
        LoyaltyTier tier = new LoyaltyTier("Gold", 100);
        when(service.findTierByPoints(120)).thenReturn(tier);

        LoyaltyTier result = service.findTierByPoints(120);
        assertEquals("Gold", result.getName());
    }

    @Test
    void testAddTier() {
        LoyaltyTierService service = mock(LoyaltyTierService.class);
        LoyaltyTier tier = new LoyaltyTier("Silver", 50);
        when(service.addTier(tier)).thenReturn(tier);

        LoyaltyTier result = service.addTier(tier);
        assertEquals(tier, result);
    }
    // ...más tests según la lógica del servicio...
}
