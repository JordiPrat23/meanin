package com.tecnocampus.project.domain.promotions;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import com.tecnocampus.project.domain.exceptions.PromotionExpiredException;

@Entity
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer maxUses;
    private String promoCode;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discount> discounts;

    // Getters y setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Integer getMaxUses() { return maxUses; }
    public void setMaxUses(Integer maxUses) { this.maxUses = maxUses; }
    public String getPromoCode() { return promoCode; }
    public void setPromoCode(String promoCode) { this.promoCode = promoCode; }
    public List<Discount> getDiscounts() { return discounts; }
    public void setDiscounts(List<Discount> discounts) { this.discounts = discounts; }

    public void validateDates() {
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new PromotionExpiredException("End date cannot be before start date");
        }
        if (endDate != null && endDate.isBefore(LocalDate.now())) {
            throw new PromotionExpiredException("Promotion is already expired");
        }
    }
}
