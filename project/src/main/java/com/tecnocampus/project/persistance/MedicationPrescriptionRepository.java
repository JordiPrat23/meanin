package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.visit.MedicationPrescription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationPrescriptionRepository extends JpaRepository<MedicationPrescription, Long> {
}

