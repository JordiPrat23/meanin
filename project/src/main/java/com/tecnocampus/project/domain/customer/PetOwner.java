package com.tecnocampus.project.domain.customer;

import com.tecnocampus.project.domain.loyalty.LoyaltyTier;
import jakarta.persistence.*;

@Entity
@Table(name = "pet_owners")
public class PetOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int fidelityPoints = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loyalty_tier_id")
    private LoyaltyTier loyaltyTier;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getFidelityPoints() { return fidelityPoints; }
    public void setFidelityPoints(int fidelityPoints) { this.fidelityPoints = fidelityPoints; }

    public LoyaltyTier getLoyaltyTier() { return loyaltyTier; }
    public void setLoyaltyTier(LoyaltyTier loyaltyTier) { this.loyaltyTier = loyaltyTier; }
}

