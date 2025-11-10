package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.loyalty.LoyaltyTier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyTierRepository extends JpaRepository<LoyaltyTier, Long> {
    boolean existsByTierName(String tierName);
}
