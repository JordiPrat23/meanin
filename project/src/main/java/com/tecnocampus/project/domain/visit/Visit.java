package com.tecnocampus.project.domain.visit;

import com.tecnocampus.project.domain.vetavailability.Vet;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "visits")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vet_id")
    private Vet vet;

    private LocalDate date;
    private LocalTime time;
    private int durationMinutes;
    private String petName;
    private String ownerName;

    @Enumerated(EnumType.STRING)
    private VisitStatus status = VisitStatus.SCHEDULED;

    private String reason;

    // New fields for UC2.1 and UC2.2
    private LocalDateTime startTimestamp;
    private LocalDateTime endTimestamp;

    @Column(length = 2000)
    private String diagnosis;

    @Column(length = 2000)
    private String notes;

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationPrescription> prescriptions = new ArrayList<>();

    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Treatment> treatments = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vet getVet() { return vet; }
    public void setVet(Vet vet) { this.vet = vet; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public VisitStatus getStatus() { return status; }
    public void setStatus(VisitStatus status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getStartTimestamp() { return startTimestamp; }
    public void setStartTimestamp(LocalDateTime startTimestamp) { this.startTimestamp = startTimestamp; }

    public LocalDateTime getEndTimestamp() { return endTimestamp; }
    public void setEndTimestamp(LocalDateTime endTimestamp) { this.endTimestamp = endTimestamp; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<MedicationPrescription> getPrescriptions() { return prescriptions; }
    public void setPrescriptions(List<MedicationPrescription> prescriptions) { this.prescriptions = prescriptions; }

    public List<Treatment> getTreatments() { return treatments; }
    public void setTreatments(List<Treatment> treatments) { this.treatments = treatments; }
}
