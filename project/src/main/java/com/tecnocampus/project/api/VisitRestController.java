package com.tecnocampus.project.api;

import com.tecnocampus.project.api.dto.visit.*;
import com.tecnocampus.project.application.services.VisitService;
import com.tecnocampus.project.application.services.VetAvailabilityService;
import com.tecnocampus.project.domain.visit.MedicationPrescription;
import com.tecnocampus.project.domain.visit.Treatment;
import com.tecnocampus.project.domain.visit.Visit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VisitRestController {

    private final VisitService visitService;
    private final VetAvailabilityService vetAvailabilityService;

    @Autowired
    public VisitRestController(VisitService visitService, VetAvailabilityService vetAvailabilityService) {
        this.visitService = visitService;
        this.vetAvailabilityService = vetAvailabilityService;
    }

    @PutMapping("/visits/{id}/reschedule")
    public ResponseEntity<VisitResponse> reschedule(
            @PathVariable Long id,
            @RequestBody RescheduleVisitRequest body) {

        var v = visitService.rescheduleVisit(id, body.newDate(), body.newTime(), body.newDurationMinutes());
        var resp = new VisitResponse(
                v.getId(), v.getPetName(), v.getOwnerName(),
                v.getDate(), v.getTime(), v.getDurationMinutes(),
                v.getStatus(), v.getReason()
        );
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/vets/{id}/visits")
    public ResponseEntity<VisitResponse> scheduleVisit(@PathVariable("id") Long vetId,
                                                       @RequestBody ScheduleVisitRequest req) {
        Visit v = visitService.scheduleVisit(vetId, req);
        VisitResponse resp = new VisitResponse(v.getId(), v.getPetName(), v.getOwnerName(), v.getDate(), v.getTime(), v.getDurationMinutes(), v.getStatus(), v.getReason());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/vets/{id}/visits/walkin")
    public ResponseEntity<VisitResponse> createWalkInVisit(
            @PathVariable("id") Long vetId,
            @RequestBody(required = false) ScheduleVisitRequest body) {

        String petName = (body != null) ? body.petName() : "Unknown Pet";
        String ownerName = (body != null) ? body.ownerName() : "Unknown Owner";
        String reason = (body != null) ? body.reason() : "Walk-in visit";

        Visit v = visitService.createWalkInVisit(vetId, petName, ownerName, reason);
        VisitResponse resp = new VisitResponse(
                v.getId(), v.getPetName(), v.getOwnerName(),
                v.getDate(), v.getTime(), v.getDurationMinutes(),
                v.getStatus(), v.getReason()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/vets/{id}/visits")
    public ResponseEntity<List<VisitResponse>> getVisitsForVet(@PathVariable("id") Long vetId,
                                                               @RequestParam(required = false) LocalDate from,
                                                               @RequestParam(required = false) LocalDate to) {
        if (from == null) from = LocalDate.now();
        if (to == null) to = from.plusDays(7);
        List<Visit> visits = visitService.getScheduleForVet(vetId, from, to);
        List<VisitResponse> res = visits.stream().map(v -> new VisitResponse(v.getId(), v.getPetName(), v.getOwnerName(), v.getDate(), v.getTime(), v.getDurationMinutes(), v.getStatus(), v.getReason())).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/vets/{id}/schedule")
    public ResponseEntity<VetScheduleResponse> getScheduleWithSlots(@PathVariable("id") Long vetId,
                                                                    @RequestParam(required = false) LocalDate from,
                                                                    @RequestParam(required = false) LocalDate to) {
        if (from == null) from = LocalDate.now();
        if (to == null) to = from.plusDays(7);

        List<Visit> visits = visitService.getScheduleForVet(vetId, from, to);
        List<VisitResponse> visitResponses = visits.stream().map(v -> new VisitResponse(v.getId(), v.getPetName(), v.getOwnerName(), v.getDate(), v.getTime(), v.getDurationMinutes(), v.getStatus(), v.getReason())).collect(Collectors.toList());

        List<AvailableSlot> slots = computeAvailableSlots(vetId, from, to, visits);

        VetScheduleResponse resp = new VetScheduleResponse(visitResponses, slots);
        return ResponseEntity.ok(resp);
    }

    private List<AvailableSlot> computeAvailableSlots(Long vetId, LocalDate from, LocalDate to, List<Visit> visits) {
        List<AvailableSlot> slots = new ArrayList<>();
        final int slotDur = 15;
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            var availabilityResp = vetAvailabilityService.getVetAvailability(vetId);
            var availabilities = availabilityResp.availabilities().stream()
                    .filter(a -> a.dayOfWeek().equals(currentDate.getDayOfWeek()))
                    .toList();

            for (var a : availabilities) {
                LocalTime t = a.startTime();
                while (!t.isAfter(a.endTime().minusMinutes(1))) {
                    final LocalTime slotTime = t;
                    boolean allowed = vetAvailabilityService.isVetAvailable(vetId, currentDate, slotTime);
                    if (allowed) {
                        boolean conflict = visits.stream().anyMatch(v -> v.getDate().equals(currentDate) && timesOverlap(slotTime, slotDur, v.getTime(), v.getDurationMinutes()));
                        if (!conflict) {
                            slots.add(new AvailableSlot(currentDate, slotTime, slotDur));
                        }
                    }
                    t = t.plusMinutes(slotDur);
                }
            }
        }
        return slots;
    }

    private boolean timesOverlap(LocalTime t1, int dur1, LocalTime t2, int dur2) {
        LocalTime end1 = t1.plusMinutes(dur1);
        LocalTime end2 = t2.plusMinutes(dur2);
        return !t1.isAfter(end2.minusSeconds(1)) && !t2.isAfter(end1.minusSeconds(1));
    }

    @GetMapping("/visits/{id}")
    public ResponseEntity<VisitResponse> getVisit(@PathVariable Long id) {
        Visit v = visitService.findById(id);
        VisitResponse resp = new VisitResponse(v.getId(), v.getPetName(), v.getOwnerName(), v.getDate(), v.getTime(), v.getDurationMinutes(), v.getStatus(), v.getReason());
        return ResponseEntity.ok(resp);
    }

    @DeleteMapping("/visits/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelVisit(@PathVariable Long id) {
        visitService.cancelVisit(id);
    }

    @PutMapping("/visits/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelVisitWithReason(
            @PathVariable Long id,
            @RequestBody(required = false) CancelVisitRequest body) {
        String reason = (body == null) ? null : body.reason();
        visitService.cancelVisit(id, reason);
    }

    @PostMapping("/visits/{id}/start")
    public ResponseEntity<VisitResponse> startConsultation(@PathVariable Long id) {
        Visit v = visitService.startConsultation(id);
        VisitResponse resp = new VisitResponse(v.getId(), v.getPetName(), v.getOwnerName(), v.getDate(), v.getTime(), v.getDurationMinutes(), v.getStatus(), v.getReason());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/visits/{id}/complete")
    public ResponseEntity<VisitResponse> completeConsultation(@PathVariable Long id) {
        Visit v = visitService.completeConsultation(id);
        VisitResponse resp = new VisitResponse(v.getId(), v.getPetName(), v.getOwnerName(), v.getDate(), v.getTime(), v.getDurationMinutes(), v.getStatus(), v.getReason());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/visits/{id}/no-show")
    public ResponseEntity<VisitResponse> markNoShow(@PathVariable Long id) {
        Visit v = visitService.markNoShow(id);
        VisitResponse resp = new VisitResponse(
                v.getId(), v.getPetName(), v.getOwnerName(),
                v.getDate(), v.getTime(), v.getDurationMinutes(),
                v.getStatus(), v.getReason()
        );
        return ResponseEntity.ok(resp);
    }


    @PatchMapping("/visits/{id}/diagnosis")
    public ResponseEntity<VisitResponse> recordDiagnosis(@PathVariable Long id, @RequestBody DiagnosisRequest body) {
        Visit v = visitService.recordDiagnosis(id, body.diagnosis(), body.notes());
        VisitResponse resp = new VisitResponse(v.getId(), v.getPetName(), v.getOwnerName(), v.getDate(), v.getTime(), v.getDurationMinutes(), v.getStatus(), v.getReason());
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/visits/{id}/prescriptions")
    public ResponseEntity<MedicationPrescription> prescribeMedication(@PathVariable Long id, @RequestBody PrescriptionRequest body) {
        MedicationPrescription p = visitService.prescribeMedication(id, body.medicationId(), body.quantity(), body.dosageInstructions(), body.duration());
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @PostMapping("/visits/{id}/treatments")
    public ResponseEntity<Treatment> addTreatment(@PathVariable Long id, @RequestBody TreatmentRequest body) {
        Treatment t = visitService.addTreatment(id, body.treatmentName(), body.notes());
        return ResponseEntity.status(HttpStatus.CREATED).body(t);
    }
}
