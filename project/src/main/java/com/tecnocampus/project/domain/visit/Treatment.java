package com.tecnocampus.project.domain.visit;

import jakarta.persistence.*;

@Entity
public class Treatment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Visit visit;

    private String treatmentName;
    private String notes;

    public Long getId() { return id; }
    public Visit getVisit() { return visit; }
    public void setVisit(Visit visit) { this.visit = visit; }
    public String getTreatmentName() { return treatmentName; }
    public void setTreatmentName(String treatmentName) { this.treatmentName = treatmentName; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}

