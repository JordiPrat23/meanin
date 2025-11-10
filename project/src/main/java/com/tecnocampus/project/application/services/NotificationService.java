package com.tecnocampus.project.application.services;

import com.tecnocampus.project.domain.visit.Visit;

public interface NotificationService {
    void sendVisitScheduled(Visit visit);
}

