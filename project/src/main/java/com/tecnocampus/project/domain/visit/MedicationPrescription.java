package com.tecnocampus.project.domain.visit;

import com.tecnocampus.project.domain.medication.Medication;
import jakarta.persistence.*;

@Entity
public class MedicationPrescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Visit visit;

    @ManyToOne
    private Medication medication;

    private int quantityPrescribed;
    private String dosageInstructions;
    private String duration;

    public Long getId() { return id; }
    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }
    public Medication getMedication() { return medication; }
    public void setMedication(Medication medication) { this.medication = medication; }
    public int getQuantityPrescribed() { return quantityPrescribed; }
    public void setQuantityPrescribed(int quantityPrescribed) { this.quantityPrescribed = quantityPrescribed; }
    public String getDosageInstructions() { return dosageInstructions; }
    public void setDosageInstructions(String dosageInstructions) { this.dosageInstructions = dosageInstructions; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
}

