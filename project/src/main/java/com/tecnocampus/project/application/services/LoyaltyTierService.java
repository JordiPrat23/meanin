package com.tecnocampus.project.application.services;

import com.tecnocampus.project.domain.loyalty.LoyaltyTier;
import com.tecnocampus.project.persistance.LoyaltyTierRepository;
import com.tecnocampus.project.application.exceptions.LoyaltyTierAlreadyExistsException;
import com.tecnocampus.project.application.exceptions.LoyaltyTierNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class LoyaltyTierService {

    private final LoyaltyTierRepository repository;

    public LoyaltyTierService(LoyaltyTierRepository repository) {
        this.repository = repository;
    }

    public LoyaltyTier createTier(LoyaltyTier tier) {
        if (repository.existsByTierName(tier.getTierName()))
            throw new LoyaltyTierAlreadyExistsException("Tier already exists");
        return repository.save(tier);
    }

    public List<LoyaltyTier> getAll() {
        return repository.findAll();
    }

    public LoyaltyTier updateTier(Long id, LoyaltyTier updated) {
        LoyaltyTier tier = repository.findById(id)
                .orElseThrow(() -> new LoyaltyTierNotFoundException("Tier not found"));
        tier.setRequiredPoints(updated.getRequiredPoints());
        tier.setDiscountPercentage(updated.getDiscountPercentage());
        tier.setBenefitsDescription(updated.getBenefitsDescription());
        return repository.save(tier);
    }

    public void deleteTier(Long id) {
        repository.deleteById(id);
    }

    public LoyaltyTier findTierByPoints(int points) {
        // Busca el tier con los puntos requeridos menores o iguales a 'points'
        return repository.findAll().stream()
                .filter(tier -> tier.getRequiredPoints() <= points)
                .max((a, b) -> Integer.compare(a.getRequiredPoints(), b.getRequiredPoints()))
                .orElse(null);
    }

    public LoyaltyTier addTier(LoyaltyTier tier) {
        return repository.save(tier);
    }
}
