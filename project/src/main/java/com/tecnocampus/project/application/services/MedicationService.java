package com.tecnocampus.project.application.services;

import com.tecnocampus.project.domain.medication.*;
import com.tecnocampus.project.persistance.*;
import com.tecnocampus.project.application.exceptions.MedicationNotFoundException;
import com.tecnocampus.project.application.exceptions.InvalidRequestException;
import com.tecnocampus.project.application.exceptions.BatchNotFoundException;
import com.tecnocampus.project.application.exceptions.MedicationOutOfStockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationBatchRepository batchRepository;
    private final LowStockAlertRepository alertRepository;

    public MedicationService(MedicationRepository medicationRepository,
                             MedicationBatchRepository batchRepository,
                             LowStockAlertRepository alertRepository) {
        this.medicationRepository = medicationRepository;
        this.batchRepository = batchRepository;
        this.alertRepository = alertRepository;
    }

    public MedicationBatch addBatch(Long medicationId, MedicationBatch batch) {
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new MedicationNotFoundException("Medication not found"));
        if (batch.getExpiryDate().isBefore(LocalDate.now()))
            throw new InvalidRequestException("Expiry date must be in the future");
        if (batch.getInitialQuantity() == null || batch.getInitialQuantity() <= 0)
            throw new InvalidRequestException("Initial quantity must be positive");
        medication.addBatch(batch);
        batchRepository.save(batch);
        checkLowStock(medication);
        return batch;
    }

    public List<MedicationBatch> getBatches(Long medicationId) {
        return batchRepository.findByMedicationId(medicationId);
    }

    public MedicationBatch updateBatch(Long id, MedicationBatch updated) {
        MedicationBatch batch = batchRepository.findById(id)
                .orElseThrow(() -> new BatchNotFoundException("Batch not found"));
        batch.setExpiryDate(updated.getExpiryDate());
        batch.setCurrentQuantity(updated.getCurrentQuantity());
        batch.setStorageLocation(updated.getStorageLocation());
        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id) {
        batchRepository.deleteById(id);
    }

    public void checkLowStock(Medication medication) {
        if (medication.isBelowReorderThreshold()) {
            boolean exists = alertRepository.findByMedicationIdAndStatus(
                    medication.getId(), LowStockAlert.Status.OPEN).size() > 0;
            if (!exists) {
                LowStockAlert alert = new LowStockAlert();
                alert.setMedication(medication);
                alertRepository.save(alert);
            }
        }
    }

    public Medication findByName(String name) {
        return medicationRepository.findAll().stream()
                .filter(med -> med.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Medication addMedication(Medication medication) {
        return medicationRepository.save(medication);
    }

    public void reduceStock(Long medicationId, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        Medication medication = medicationRepository.findById(medicationId)
                .orElseThrow(() -> new MedicationNotFoundException("Medication not found"));

        List<MedicationBatch> batches = batchRepository.findByMedicationId(medicationId);
        batches.sort(Comparator.comparing(MedicationBatch::getExpiryDate));

        int remaining = quantity;
        for (MedicationBatch batch : batches) {
            if (remaining <= 0) break;
            int available = batch.getCurrentQuantity() == null ? 0 : batch.getCurrentQuantity();
            if (available <= 0) continue;
            int take = Math.min(available, remaining);
            batch.setCurrentQuantity(available - take);
            batchRepository.save(batch);
            remaining -= take;
        }

        if (remaining > 0) {
            throw new MedicationOutOfStockException("Not enough stock for medication id " + medicationId);
        }

        checkLowStock(medication);
    }
}
