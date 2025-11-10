package com.tecnocampus.project.api;

import com.tecnocampus.project.api.dto.vetavailability.AvailabilityExceptionRequest;
import com.tecnocampus.project.api.dto.vetavailability.AvailabilityRequest;
import com.tecnocampus.project.api.dto.vetavailability.VetAvailabilityResponse;
import com.tecnocampus.project.application.services.VetAvailabilityService;
import com.tecnocampus.project.domain.vetavailability.Availability;
import com.tecnocampus.project.domain.vetavailability.ExceptionAvailability;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.DayOfWeek;

@RestController
public class VetAvailabilityRestController {

    private final VetAvailabilityService service;

    @Autowired
    public VetAvailabilityRestController(VetAvailabilityService service) {
        this.service = service;
    }

    @PutMapping("/vets/{id}/availability/{dayOfWeek}")
    public ResponseEntity<Availability> upsertAvailability(
            @PathVariable("id") Long vetId,
            @PathVariable DayOfWeek dayOfWeek,
            @RequestBody AvailabilityRequest body) {

        Availability saved = service.upsertAvailability(vetId, dayOfWeek, body);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/vets/{id}/availability/{dayOfWeek}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvailabilityByDay(
            @PathVariable("id") Long vetId,
            @PathVariable DayOfWeek dayOfWeek) {

        service.deleteAvailabilityByDay(vetId, dayOfWeek);
    }

    @PutMapping("/availability/{id}")
    public ResponseEntity<Availability> updateAvailabilityById(
            @PathVariable("id") Long availabilityId,
            @RequestBody AvailabilityRequest body) {

        Availability updated = service.updateAvailabilityById(availabilityId, body);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/availability/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAvailabilityById(@PathVariable("id") Long availabilityId) {
        service.deleteAvailabilityById(availabilityId);
    }

    @PostMapping("/vets/{id}/exceptions")
    public ResponseEntity<ExceptionAvailability> addException(
            @PathVariable("id") Long vetId,
            @RequestBody AvailabilityExceptionRequest body) {

        ExceptionAvailability created = service.addException(vetId, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/exceptions/{id}")
    public ResponseEntity<ExceptionAvailability> updateException(
            @PathVariable("id") Long exceptionId,
            @RequestBody AvailabilityExceptionRequest body) {

        ExceptionAvailability updated = service.updateException(exceptionId, body);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/exceptions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteException(@PathVariable("id") Long exceptionId) {
        service.deleteExceptionById(exceptionId);
    }

    @GetMapping("/vets/{id}/availability")
    public ResponseEntity<VetAvailabilityResponse> getVetAvailability(@PathVariable("id") Long vetId) {
        return ResponseEntity.ok(service.getVetAvailability(vetId));
    }
}
