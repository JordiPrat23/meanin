package com.tecnocampus.project.domain.vetavailability;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import com.tecnocampus.project.domain.exceptions.VetAvailabilityConflictException;

@Entity
@Table(
        name = "vet_availabilities",
        uniqueConstraints = @UniqueConstraint(name = "uk_vet_day", columnNames = {"vet_id", "day_of_week"})
)

public class Availability {
    public Availability() {

    }
    public Availability(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
        if (dayOfWeek == null) throw new VetAvailabilityConflictException("Day of week cannot be null");
        if (startTime == null || endTime == null) throw new VetAvailabilityConflictException("Start and end time cannot be null");
        if (startTime.isAfter(endTime)) throw new VetAvailabilityConflictException("Start time must be before end time");

        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 16)
    private DayOfWeek dayOfWeek;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Vet getVet() { return vet; }
    public void setVet(Vet vet) { this.vet = vet; }

    public DayOfWeek getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(DayOfWeek dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Availability)) return false;
        Availability that = (Availability) o;
        return dayOfWeek == that.dayOfWeek &&
                startTime.equals(that.startTime) &&
                endTime.equals(that.endTime);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(dayOfWeek, startTime, endTime);
    }
    public void validateTimeRange() {
        if (startTime == null || endTime == null) {
            throw new VetAvailabilityConflictException("Start and end time cannot be null");
        }
        if (startTime.isAfter(endTime)) {
            throw new VetAvailabilityConflictException("Start time must be before end time");
        }
    }


}
