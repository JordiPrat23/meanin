package com.tecnocampus.project.api;

import com.tecnocampus.project.application.services.MedicationService;
import com.tecnocampus.project.domain.medication.MedicationBatch;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medications")
public class MedicationRestController {

    private final MedicationService medicationService;

    public MedicationRestController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping("/{id}/batches")
    public ResponseEntity<List<MedicationBatch>> getMedicationBatches(@PathVariable("id") Long medicationId) {
        return ResponseEntity.ok(medicationService.getBatches(medicationId));
    }

    @PostMapping("/{id}/batches")
    public ResponseEntity<MedicationBatch> addMedicationBatch(
            @PathVariable("id") Long medicationId,
            @Valid @RequestBody MedicationBatch body) {
        MedicationBatch created = medicationService.addBatch(medicationId, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/batches/{id}")
    public ResponseEntity<MedicationBatch> updateMedicationBatch(
            @PathVariable("id") Long batchId,
            @Valid @RequestBody MedicationBatch body) {
        MedicationBatch updated = medicationService.updateBatch(batchId, body);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/batches/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedicationBatch(@PathVariable("id") Long batchId) {
        medicationService.deleteBatch(batchId);
    }
}
