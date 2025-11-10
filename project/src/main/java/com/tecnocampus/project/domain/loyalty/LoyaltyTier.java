package com.tecnocampus.project.domain.loyalty;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import com.tecnocampus.project.domain.exceptions.LoyaltyTierNotFoundException;
import com.tecnocampus.project.domain.exceptions.InvalidTierPointsException;

@Entity
@Table(name = "loyalty_tiers")
public class LoyaltyTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tierName;

    @Column(nullable = false)
    private Integer requiredPoints;

    @Column(nullable = false)
    private BigDecimal discountPercentage;

    @Column(length = 255)
    private String benefitsDescription;

    public LoyaltyTier() {}

    public LoyaltyTier(String tierName, int requiredPoints) {
        if (tierName == null || tierName.isBlank()) {
            throw new LoyaltyTierNotFoundException("Tier name cannot be null or blank");
        }
        if (requiredPoints < 0) {
            throw new InvalidTierPointsException("Required points cannot be negative");
        }
        this.tierName = tierName;
        this.requiredPoints = requiredPoints;
    }

    public Long getId() { return id; }
    public String getTierName() { return tierName; }
    public void setTierName(String tierName) { this.tierName = tierName; }
    public Integer getRequiredPoints() { return requiredPoints; }
    public void setRequiredPoints(Integer requiredPoints) {
        if (requiredPoints == null || requiredPoints < 0) {
            throw new InvalidTierPointsException("Required points cannot be null or negative");
        }
        this.requiredPoints = requiredPoints;
    }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    public String getBenefitsDescription() { return benefitsDescription; }
    public void setBenefitsDescription(String benefitsDescription) { this.benefitsDescription = benefitsDescription; }

    public String getName() {
        return tierName;
    }

    public int getPointsRequired() {
        return requiredPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoyaltyTier that = (LoyaltyTier) o;
        return Objects.equals(tierName, that.tierName) && Objects.equals(requiredPoints, that.requiredPoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tierName, requiredPoints);
    }
}
