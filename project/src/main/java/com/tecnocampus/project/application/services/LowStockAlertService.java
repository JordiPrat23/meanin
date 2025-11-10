package com.tecnocampus.project.application.services;

import com.tecnocampus.project.domain.medication.LowStockAlert;
import com.tecnocampus.project.persistance.LowStockAlertRepository;
import com.tecnocampus.project.application.exceptions.AlertNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class LowStockAlertService {
    private final LowStockAlertRepository repository;

    public LowStockAlertService(LowStockAlertRepository repository) {
        this.repository = repository;
    }

    public List<LowStockAlert> getAlerts(String status) {
        if (status == null) return repository.findAll();
        return repository.findByStatus(LowStockAlert.Status.valueOf(status));
    }

    public LowStockAlert updateAlertStatus(Long id, LowStockAlert.Status status, String note) {
        LowStockAlert alert = repository.findById(id)
            .orElseThrow(() -> new AlertNotFoundException("Alert not found"));
        alert.setStatus(status);
        alert.setNote(note);
        return repository.save(alert);
    }
}
