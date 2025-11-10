package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.promotions.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}

