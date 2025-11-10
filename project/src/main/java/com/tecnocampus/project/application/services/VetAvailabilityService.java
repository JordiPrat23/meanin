package com.tecnocampus.project.application.services;

import com.tecnocampus.project.api.dto.vetavailability.AvailabilityExceptionRequest;
import com.tecnocampus.project.api.dto.vetavailability.AvailabilityRequest;
import com.tecnocampus.project.api.dto.vetavailability.VetAvailabilityResponse;
import com.tecnocampus.project.domain.vetavailability.*;
import com.tecnocampus.project.persistance.AvailabilityRepository;
import com.tecnocampus.project.persistance.ExceptionAvailabilityRepository;
import com.tecnocampus.project.persistance.VetRepository;
import com.tecnocampus.project.application.exceptions.VetNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class VetAvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final ExceptionAvailabilityRepository exceptionAvailabilityRepository;
    private final VetRepository vetRepository;

    @Autowired
    public VetAvailabilityService(AvailabilityRepository availabilityRepository,
                                  ExceptionAvailabilityRepository exceptionAvailabilityRepository,
                                  VetRepository vetRepository) {
        this.availabilityRepository = availabilityRepository;
        this.exceptionAvailabilityRepository = exceptionAvailabilityRepository;
        this.vetRepository = vetRepository;
    }

    public VetAvailabilityService(AvailabilityRepository availabilityRepository,
                                  ExceptionAvailabilityRepository exceptionAvailabilityRepository) {
        this(availabilityRepository, exceptionAvailabilityRepository, null);
    }

    private Vet findVetOrThrow(Long vetId) {
        return vetRepository.findById(vetId)
                .orElseThrow(() -> new VetNotFoundException("Vet not found: " + vetId));
    }


    public Availability addAvailability(Vet vet, Availability availability) {
        vet.addAvailability(availability);
        return availabilityRepository.save(availability);
    }

    public Availability updateAvailability(Vet vet, Availability oldAvailability, Availability newAvailability) {
        vet.updateAvailability(oldAvailability, newAvailability);
        return availabilityRepository.save(newAvailability);
    }

    public void deleteAvailability(Vet vet, Availability availability) {
        vet.removeAvailability(availability);
        availabilityRepository.delete(availability);
    }


    public ExceptionAvailability addException(Vet vet, ExceptionAvailability exception) {
        vet.addException(exception);
        return exceptionAvailabilityRepository.save(exception);
    }

    public void updateException(Vet vet, ExceptionAvailability oldExc, ExceptionAvailability newExc) {
        vet.updateException(oldExc, newExc);
        exceptionAvailabilityRepository.save(newExc);
    }

    public void deleteException(Vet vet, ExceptionAvailability exception) {
        vet.removeException(exception);
        exceptionAvailabilityRepository.delete(exception);
    }


    public List<Availability> getAvailabilities(Vet vet) {
        return availabilityRepository.findByVet(vet);
    }

    public List<ExceptionAvailability> getExceptions(Vet vet) {
        return exceptionAvailabilityRepository.findByVet(vet);
    }

    public Availability upsertAvailability(Long vetId, DayOfWeek dayOfWeek, AvailabilityRequest request) {
        Vet vet = findVetOrThrow(vetId);
        Availability existing = availabilityRepository.findByVet(vet).stream()
                .filter(a -> a.getDayOfWeek().equals(dayOfWeek))
                .findFirst().orElse(null);

        if (existing != null) {
            existing.setStartTime(request.startTime());
            existing.setEndTime(request.endTime());
            return availabilityRepository.save(existing);
        } else {
            Availability newAvailability = new Availability(dayOfWeek, request.startTime(), request.endTime());
            vet.addAvailability(newAvailability);
            return availabilityRepository.save(newAvailability);
        }
    }

    public void deleteAvailabilityByDay(Long vetId, DayOfWeek dayOfWeek) {
        Vet vet = findVetOrThrow(vetId);
        Availability existing = availabilityRepository.findByVet(vet).stream()
                .filter(a -> a.getDayOfWeek().equals(dayOfWeek))
                .findFirst().orElse(null);
        if (existing != null) {
            vet.removeAvailability(existing);
            availabilityRepository.delete(existing);
        }
    }

    public Availability updateAvailabilityById(Long id, AvailabilityRequest request) {
        Availability availability = availabilityRepository.findById(id).orElseThrow(() -> new RuntimeException("Availability not found"));
        availability.setStartTime(request.startTime());
        availability.setEndTime(request.endTime());
        return availabilityRepository.save(availability);
    }

    public void deleteAvailabilityById(Long id) {
        Availability availability = availabilityRepository.findById(id).orElseThrow(() -> new RuntimeException("Availability not found"));
        availabilityRepository.delete(availability);
    }

    public ExceptionAvailability addException(Long vetId, AvailabilityExceptionRequest request) {
        if (request.date() == null) {
            throw new com.tecnocampus.project.application.exceptions.InvalidRequestException("Exception date is required");
        }
        Vet vet = findVetOrThrow(vetId);
        ExceptionAvailability exception = new ExceptionAvailability();
        exception.setDate(request.date());
        exception.setStartTime(request.startTime());
        exception.setEndTime(request.endTime());
        exception.setAvailable(request.isAvailable());
        exception.setReason(request.reason());
        vet.addException(exception);
        return exceptionAvailabilityRepository.save(exception);
    }

    public ExceptionAvailability updateException(Long exceptionId, AvailabilityExceptionRequest request) {
        ExceptionAvailability exception = exceptionAvailabilityRepository.findById(exceptionId)
                .orElseThrow(() -> new RuntimeException("Exception not found"));
        if (request.date() == null) {
            throw new com.tecnocampus.project.application.exceptions.InvalidRequestException("Exception date is required");
        }
        exception.setDate(request.date());
        exception.setStartTime(request.startTime());
        exception.setEndTime(request.endTime());
        exception.setAvailable(request.isAvailable());
        exception.setReason(request.reason());
        return exceptionAvailabilityRepository.save(exception);
    }

    public void deleteExceptionById(Long exceptionId) {
        ExceptionAvailability exception = exceptionAvailabilityRepository.findById(exceptionId)
                .orElseThrow(() -> new RuntimeException("Exception not found"));
        exceptionAvailabilityRepository.delete(exception);
    }

    public VetAvailabilityResponse getVetAvailability(Long vetId) {
        Vet vet = findVetOrThrow(vetId);
        List<Availability> availabilities = availabilityRepository.findByVet(vet);
        List<ExceptionAvailability> exceptions = exceptionAvailabilityRepository.findByVet(vet);

        List<AvailabilityRequest> availabilityRequests = availabilities.stream()
            .map(a -> new AvailabilityRequest(
                a.getDayOfWeek(),
                a.getStartTime(),
                a.getEndTime()
            ))
            .toList();

        List<AvailabilityExceptionRequest> exceptionRequests = exceptions.stream()
            .map(e -> new AvailabilityExceptionRequest(
                e.getDate(),
                e.getStartTime(),
                e.getEndTime(),
                e.isAvailable(),
                e.getReason()
            ))
            .toList();

        return new VetAvailabilityResponse(vet.getId(), availabilityRequests, exceptionRequests);
    }

    public boolean isVetAvailable(Long vetId, LocalDate date, LocalTime time) {
        Vet vet = findVetOrThrow(vetId);
        DayOfWeek dow = date.getDayOfWeek();

        boolean baseAvailable = availabilityRepository.findByVet(vet).stream()
                .filter(a -> a.getDayOfWeek().equals(dow))
                .anyMatch(a -> !time.isBefore(a.getStartTime()) && time.isBefore(a.getEndTime()));

        List<ExceptionAvailability> exs = exceptionAvailabilityRepository.findByVet(vet).stream()
                .filter(e -> e.getDate().equals(date))
                .toList();

        boolean blocked = exs.stream().anyMatch(e -> {
            if (!e.isAvailable()) {
                if (e.getStartTime() == null && e.getEndTime() == null) return true; // whole day blocked
                if (e.getStartTime() != null && e.getEndTime() != null) {
                    return !time.isBefore(e.getStartTime()) && time.isBefore(e.getEndTime());
                }
            }
            return false;
        });
        if (blocked) return false;

        boolean explicitlyAllowed = exs.stream().anyMatch(e -> {
            if (e.isAvailable()) {
                if (e.getStartTime() == null && e.getEndTime() == null) return true; // whole day allowed
                if (e.getStartTime() != null && e.getEndTime() != null) {
                    return !time.isBefore(e.getStartTime()) && time.isBefore(e.getEndTime());
                }
            }
            return false;
        });
        if (explicitlyAllowed) return true;

        return baseAvailable;
    }
}
