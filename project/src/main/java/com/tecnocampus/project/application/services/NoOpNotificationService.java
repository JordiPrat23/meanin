package com.tecnocampus.project.application.services;

import com.tecnocampus.project.domain.visit.Visit;
import org.springframework.stereotype.Service;

@Service
public class NoOpNotificationService implements NotificationService {
    @Override
    public void sendVisitScheduled(Visit visit) {
        System.out.println("(Notification) Visit scheduled: " + visit.getId() + " for " + visit.getPetName());
    }
}

