package com.tecnocampus.project.domain;

import com.tecnocampus.project.domain.loyalty.LoyaltyTier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoyaltyTierDomainTest {

    @Test
    void testLoyaltyTierCreation() {
        LoyaltyTier tier = new LoyaltyTier("Gold", 100);
        assertEquals("Gold", tier.getName());
        assertEquals(100, tier.getPointsRequired());
    }

    @Test
    void testLoyaltyTierEquality() {
        LoyaltyTier tier1 = new LoyaltyTier("Silver", 50);
        LoyaltyTier tier2 = new LoyaltyTier("Silver", 50);
        assertEquals(tier1, tier2);
    }
    // ...puedes añadir más tests según la lógica de LoyaltyTier...
}
