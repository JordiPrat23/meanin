package com.tecnocampus.project.domain.medication;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.tecnocampus.project.domain.exceptions.MedicationOutOfStockException;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "medication_batches")
public class MedicationBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medication_id")
    @JsonIgnore
    private Medication medication;

    @Column(nullable = false)
    private String lotNumber;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private Integer initialQuantity;

    @Column(nullable = false)
    private Integer currentQuantity;

    @Column(nullable = false)
    private BigDecimal purchasePricePerUnit;

    @Column(nullable = false)
    private LocalDate receivedDate;

    private String storageLocation;

    public void decreaseQuantity(int amount) {
        if (amount <= 0) throw new MedicationOutOfStockException("Amount must be positive");
        if (currentQuantity < amount) throw new MedicationOutOfStockException("Not enough stock in batch");
        currentQuantity -= amount;
    }

    public Long getId() { return id; }
    public Medication getMedication() { return medication; }
    public void setMedication(Medication medication) { this.medication = medication; }

    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Integer getInitialQuantity() { return initialQuantity; }
    public void setInitialQuantity(Integer initialQuantity) {
        this.initialQuantity = initialQuantity;
        this.currentQuantity = initialQuantity;
    }

    public Integer getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(Integer currentQuantity) { this.currentQuantity = currentQuantity; }

    public BigDecimal getPurchasePricePerUnit() { return purchasePricePerUnit; }
    public void setPurchasePricePerUnit(BigDecimal purchasePricePerUnit) { this.purchasePricePerUnit = purchasePricePerUnit; }

    public LocalDate getReceivedDate() { return receivedDate; }
    public void setReceivedDate(LocalDate receivedDate) { this.receivedDate = receivedDate; }

    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String storageLocation) { this.storageLocation = storageLocation; }
}
