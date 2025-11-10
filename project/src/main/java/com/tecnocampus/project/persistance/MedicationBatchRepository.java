package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.medication.MedicationBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MedicationBatchRepository extends JpaRepository<MedicationBatch, Long> {
    List<MedicationBatch> findByMedicationId(Long medicationId);
}
