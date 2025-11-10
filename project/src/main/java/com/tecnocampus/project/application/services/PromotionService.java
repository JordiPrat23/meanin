package com.tecnocampus.project.application.services;

import com.tecnocampus.project.domain.promotions.Promotion;
import com.tecnocampus.project.persistance.PromotionRepository;
import com.tecnocampus.project.application.exceptions.PromotionInvalidException;
import com.tecnocampus.project.application.exceptions.PromotionNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PromotionService {
    private final PromotionRepository repository;

    public PromotionService(PromotionRepository repository) {
        this.repository = repository;
    }

    public Promotion createPromotion(Promotion promotion) {
        if (promotion.getStartDate() == null || promotion.getEndDate() == null)
            throw new PromotionInvalidException("Start and end date are required");
        if (promotion.getEndDate().isBefore(promotion.getStartDate()))
            throw new PromotionInvalidException("End date must be after start date");
        if (promotion.getMaxUses() != null && promotion.getMaxUses() < 0)
            throw new PromotionInvalidException("Max uses must be positive");
        return repository.save(promotion);
    }

    public List<Promotion> getAllPromotions() {
        return repository.findAll();
    }

    public Promotion updatePromotion(Long id, Promotion updated) {
        Promotion promo = repository.findById(id)
            .orElseThrow(() -> new PromotionNotFoundException("Promotion not found"));
        promo.setName(updated.getName());
        promo.setDescription(updated.getDescription());
        promo.setStartDate(updated.getStartDate());
        promo.setEndDate(updated.getEndDate());
        promo.setMaxUses(updated.getMaxUses());
        promo.setPromoCode(updated.getPromoCode());
        promo.setDiscounts(updated.getDiscounts());
        return repository.save(promo);
    }

    public void deletePromotion(Long id) {
        repository.deleteById(id);
    }
}
