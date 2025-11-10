package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.medication.LowStockAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LowStockAlertRepository extends JpaRepository<LowStockAlert, Long> {
    List<LowStockAlert> findByStatus(LowStockAlert.Status status);
    List<LowStockAlert> findByMedicationIdAndStatus(Long medicationId, LowStockAlert.Status status);
}

