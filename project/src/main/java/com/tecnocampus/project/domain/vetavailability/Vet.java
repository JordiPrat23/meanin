package com.tecnocampus.project.domain.vetavailability;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vets")
public class Vet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;


    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Availability> availabilities = new ArrayList<>();

    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ExceptionAvailability> exceptions = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Availability> getAvailabilities() { return availabilities; }
    public void setAvailabilities(List<Availability> availabilities) { this.availabilities = availabilities; }

    public List<ExceptionAvailability> getExceptions() { return exceptions; }
    public void setExceptions(List<ExceptionAvailability> exceptions) { this.exceptions = exceptions; }


    public void addAvailability(Availability availability) {
        if (availability == null) throw new IllegalArgumentException("Availability cannot be null");
        if (availability.getStartTime().isAfter(availability.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        boolean overlaps = availabilities.stream()
                .filter(a -> a.getDayOfWeek().equals(availability.getDayOfWeek()))
                .anyMatch(a -> availability.getStartTime().isBefore(a.getEndTime())
                        && availability.getEndTime().isAfter(a.getStartTime()));

        if (overlaps) {
            throw new IllegalArgumentException("New availability overlaps existing hours for " + availability.getDayOfWeek());
        }

        boolean exists = availabilities.stream()
                .anyMatch(a -> a.getDayOfWeek().equals(availability.getDayOfWeek()));
        if (exists) {
            throw new IllegalArgumentException("Availability already exists for " + availability.getDayOfWeek());
        }

        availability.setVet(this);
        availabilities.add(availability);
    }

    public void updateAvailability(Availability oldAvailability, Availability newAvailability) {
        if (oldAvailability == null || newAvailability == null)
            throw new IllegalArgumentException("Availability cannot be null");

        int index = availabilities.indexOf(oldAvailability);
        if (index == -1)
            throw new IllegalArgumentException("Old availability not found");

        if (newAvailability.getStartTime().isAfter(newAvailability.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        newAvailability.setVet(this);
        availabilities.set(index, newAvailability);
    }

    public void removeAvailability(Availability availability) {
        if (availability == null) throw new IllegalArgumentException("Availability cannot be null");
        availabilities.remove(availability);
    }

    public void addException(ExceptionAvailability exceptionAvailability) {
        if (exceptionAvailability == null) throw new IllegalArgumentException("ExceptionAvailability cannot be null");

        boolean exists = exceptions.stream()
                .anyMatch(e -> e.getDate().equals(exceptionAvailability.getDate()));
        if (exists) throw new IllegalArgumentException("Exception already exists for date " + exceptionAvailability.getDate());

        exceptionAvailability.setVet(this);
        exceptions.add(exceptionAvailability);
    }

    public void updateException(ExceptionAvailability oldException, ExceptionAvailability newException) {
        if (oldException == null || newException == null)
            throw new IllegalArgumentException("ExceptionAvailability cannot be null");

        int index = exceptions.indexOf(oldException);
        if (index == -1)
            throw new IllegalArgumentException("Old exception not found");

        newException.setVet(this);
        exceptions.set(index, newException);
    }

    public void removeException(ExceptionAvailability exception) {
        if (exception == null) throw new IllegalArgumentException("Exception cannot be null");
        exceptions.remove(exception);
    }
}
