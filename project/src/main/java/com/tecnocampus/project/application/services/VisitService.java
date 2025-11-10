package com.tecnocampus.project.application.services;

import com.tecnocampus.project.api.dto.visit.ScheduleVisitRequest;
import com.tecnocampus.project.domain.visit.*;
import com.tecnocampus.project.domain.vetavailability.Vet;
import com.tecnocampus.project.domain.medication.Medication;
import com.tecnocampus.project.persistance.*;
import com.tecnocampus.project.application.exceptions.VisitNotFoundException;
import com.tecnocampus.project.application.exceptions.VetNotFoundException;
import com.tecnocampus.project.application.exceptions.VetUnavailableException;
import com.tecnocampus.project.application.exceptions.MedicationNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class VisitService {

    private final VisitRepository visitRepository;
    private final VetRepository vetRepository;
    private final VetAvailabilityService vetAvailabilityService;
    private final MedicationService medicationService;
    private final MedicationRepository medicationRepository;
    private final MedicationPrescriptionRepository prescriptionRepository;
    private final TreatmentRepository treatmentRepository;
    private final NotificationService notificationService;

    @Autowired
    public VisitService(VisitRepository visitRepository,
                        VetRepository vetRepository,
                        VetAvailabilityService vetAvailabilityService,
                        MedicationService medicationService,
                        MedicationRepository medicationRepository,
                        MedicationPrescriptionRepository prescriptionRepository,
                        TreatmentRepository treatmentRepository,
                        NotificationService notificationService) {
        this.visitRepository = visitRepository;
        this.vetRepository = vetRepository;
        this.vetAvailabilityService = vetAvailabilityService;
        this.medicationService = medicationService;
        this.medicationRepository = medicationRepository;
        this.prescriptionRepository = prescriptionRepository;
        this.treatmentRepository = treatmentRepository;
        this.notificationService = notificationService;
    }

    public VisitService(VisitRepository visitRepository, VetRepository vetRepository) {
        this(visitRepository, vetRepository, null, null, null, null, null, null);
    }

    public VisitService(VisitRepository visitRepository, VetRepository vetRepository, VetAvailabilityService vetAvailabilityService) {
        this(visitRepository, vetRepository, vetAvailabilityService, null, null, null, null, null);
    }

    public Visit scheduleVisit(Long vetId, ScheduleVisitRequest req) {
        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new VetNotFoundException("Vet not found: " + vetId));

        if (req.date() == null || req.time() == null)
            throw new IllegalArgumentException("date and time are required");

        if (req.durationMinutes() <= 0)
            req = new ScheduleVisitRequest(req.petName(), req.ownerName(), req.date(), req.time(), 15, req.reason());

        if (vetAvailabilityService != null) {
            boolean available = vetAvailabilityService.isVetAvailable(vetId, req.date(), req.time());
            if (!available)
                throw new VetUnavailableException("Vet not available at requested time");
        }

        Visit visit = new Visit();
        visit.setVet(vet);
        visit.setDate(req.date());
        visit.setTime(req.time());
        visit.setDurationMinutes(req.durationMinutes());
        visit.setPetName(req.petName());
        visit.setOwnerName(req.ownerName());
        visit.setReason(req.reason());
        visit.setStatus(VisitStatus.SCHEDULED);

        Visit saved = visitRepository.save(visit);

        if (notificationService != null)
            notificationService.sendVisitScheduled(saved);

        return saved;
    }

    public List<Visit> getScheduleForVet(Long vetId, LocalDate from, LocalDate to) {
        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new VetNotFoundException("Vet not found: " + vetId));
        return visitRepository.findByVetAndDateBetween(vet, from, to);
    }

    public Visit findById(Long id) {
        return visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Visit not found: " + id));
    }

    public void cancelVisit(Long id) {
        Visit v = findById(id);
        v.setStatus(VisitStatus.CANCELLED);
        visitRepository.save(v);
    }

    public void cancelVisit(Long id, String reason) {
        Visit v = findById(id);
        if (v.getStatus() != VisitStatus.SCHEDULED)
            throw new IllegalArgumentException("Only SCHEDULED visits can be cancelled");
        v.setStatus(VisitStatus.CANCELLED);
        if (reason != null && !reason.isBlank())
            v.setReason(reason);
        v.setEndTimestamp(LocalDateTime.now());
        visitRepository.save(v);
    }

    public Visit startConsultation(Long visitId) {
        Visit v = findById(visitId);
        v.setStatus(VisitStatus.IN_PROGRESS);
        if (v.getStartTimestamp() == null)
            v.setStartTimestamp(LocalDateTime.now());
        return visitRepository.save(v);
    }

    public Visit completeConsultation(Long visitId) {
        Visit v = findById(visitId);
        v.setStatus(VisitStatus.COMPLETED);
        v.setEndTimestamp(LocalDateTime.now());
        return visitRepository.save(v);
    }

    public Visit recordDiagnosis(Long visitId, String diagnosis, String notes) {
        Visit v = findById(visitId);
        v.setDiagnosis(diagnosis);
        v.setNotes(notes);
        return visitRepository.save(v);
    }

    public MedicationPrescription prescribeMedication(Long visitId, Long medicationId, int quantityPrescribed,
                                                      String dosageInstructions, String duration) {
        if (medicationService == null)
            throw new IllegalStateException("MedicationService not available");

        Visit v = findById(visitId);
        Medication med = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new MedicationNotFoundException("Medication not found: " + medicationId));

        medicationService.reduceStock(medicationId, quantityPrescribed);

        MedicationPrescription p = new MedicationPrescription();
        p.setVisit(v);
        p.setMedication(med);
        p.setQuantityPrescribed(quantityPrescribed);
        p.setDosageInstructions(dosageInstructions);
        p.setDuration(duration);

        MedicationPrescription saved = prescriptionRepository.save(p);
        v.getPrescriptions().add(saved);
        visitRepository.save(v);
        return saved;
    }

    public Treatment addTreatment(Long visitId, String treatmentName, String notes) {
        Visit v = findById(visitId);
        Treatment t = new Treatment();
        t.setVisit(v);
        t.setTreatmentName(treatmentName);
        t.setNotes(notes);
        Treatment saved = treatmentRepository.save(t);
        v.getTreatments().add(saved);
        visitRepository.save(v);
        return saved;
    }

    public Visit rescheduleVisit(Long visitId, LocalDate newDate, LocalTime newTime, Integer newDurationMinutes) {
        if (newDate == null || newTime == null)
            throw new IllegalArgumentException("New date and time are required");

        Visit v = findById(visitId);
        if (v.getStatus() != VisitStatus.SCHEDULED)
            throw new IllegalArgumentException("Only SCHEDULED visits can be rescheduled");

        if (vetAvailabilityService != null) {
            boolean available = vetAvailabilityService.isVetAvailable(v.getVet().getId(), newDate, newTime);
            if (!available)
                throw new VetUnavailableException("Vet not available at requested time");
        }

        int finalDuration = (newDurationMinutes != null && newDurationMinutes > 0)
                ? newDurationMinutes
                : v.getDurationMinutes();

        v.setDate(newDate);
        v.setTime(newTime);
        v.setDurationMinutes(finalDuration);

        return visitRepository.save(v);
    }

    public Visit createWalkInVisit(Long vetId, String petName, String ownerName, String reason) {
        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new VetNotFoundException("Vet not found: " + vetId));
        Visit visit = new Visit();
        visit.setVet(vet);
        visit.setPetName(petName);
        visit.setOwnerName(ownerName);
        visit.setDate(LocalDate.now());
        visit.setTime(LocalTime.now());
        visit.setDurationMinutes(15);
        visit.setReason(reason != null ? reason : "Walk-in visit");
        visit.setStatus(VisitStatus.IN_PROGRESS);
        visit.setStartTimestamp(LocalDateTime.now());
        return visitRepository.save(visit);
    }

    public Visit markNoShow(Long id) {
        Visit v = findById(id);
        if (v.getStatus() != VisitStatus.SCHEDULED)
            throw new IllegalArgumentException("Only SCHEDULED visits can be marked as no-show");
        v.setStatus(VisitStatus.NO_SHOW);
        v.setEndTimestamp(LocalDateTime.now());
        return visitRepository.save(v);
    }

}
