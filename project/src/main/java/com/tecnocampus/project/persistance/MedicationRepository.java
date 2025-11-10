package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.medication.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
}
