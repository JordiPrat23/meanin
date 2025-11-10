package com.tecnocampus.project.domain.medication;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.tecnocampus.project.domain.exceptions.MedicationOutOfStockException;

@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String activeIngredient;
    private String dosageUnit;
    private BigDecimal unitPrice;
    private Integer reorderThreshold = 0;

    private String purpose;
    private int dosage;

    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationBatch> batches = new ArrayList<>();

    public Medication() {

    }

    public void addBatch(MedicationBatch batch) {
        if (batch == null) throw new MedicationOutOfStockException("Batch cannot be null");
        batch.setMedication(this);
        batches.add(batch);
    }

    public int getTotalStock() {
        return batches.stream().mapToInt(MedicationBatch::getCurrentQuantity).sum();
    }

    public boolean isBelowReorderThreshold() {
        return getTotalStock() < reorderThreshold;
    }

    public Medication(String name, String purpose, int dosage) {
        this.name = name;
        this.purpose = purpose;
        this.dosage = dosage;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getActiveIngredient() { return activeIngredient; }
    public void setActiveIngredient(String activeIngredient) { this.activeIngredient = activeIngredient; }

    public String getDosageUnit() { return dosageUnit; }
    public void setDosageUnit(String dosageUnit) { this.dosageUnit = dosageUnit; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Integer getReorderThreshold() { return reorderThreshold; }
    public void setReorderThreshold(Integer reorderThreshold) { this.reorderThreshold = reorderThreshold; }

    public List<MedicationBatch> getBatches() { return batches; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public int getDosage() { return dosage; }
    public void setDosage(int dosage) { this.dosage = dosage; }
}
