package com.tecnocampus.project.domain.vetavailability;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vet_availability_exceptions")
public class ExceptionAvailability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @Column(nullable = false)
    private LocalDate date;

    private LocalTime startTime;
    private LocalTime endTime;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(length = 255)
    private String reason;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vet getVet() { return vet; }
    public void setVet(Vet vet) { this.vet = vet; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
