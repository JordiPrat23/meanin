package com.tecnocampus.project.domain.promotions;

import jakarta.persistence.*;

@Entity
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // percentage, fixed, etc.

    private Double discountValue;

    @Column(name = "discount_target")
    private String target; // Ej: ClinicService:Vaccination
    public Long getId() { return id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Double getDiscountValue() { return discountValue; }
    public void setDiscountValue(Double discountValue) { this.discountValue = discountValue; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
}
