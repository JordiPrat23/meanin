package com.tecnocampus.project.domain.medication;

import jakarta.persistence.*;

@Entity
public class LowStockAlert {
    public enum Status { OPEN, ACKNOWLEDGED, CLOSED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Medication medication;
    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;
    private String note;

    public Long getId() { return id; }
    public Medication getMedication() { return medication; }
    public void setMedication(Medication medication) { this.medication = medication; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
