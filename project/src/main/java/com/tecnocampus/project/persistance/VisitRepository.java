package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.visit.Visit;
import com.tecnocampus.project.domain.vetavailability.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {
    List<Visit> findByVetAndDateBetween(Vet vet, LocalDate start, LocalDate end);
    List<Visit> findByVetAndDate(Vet vet, LocalDate date);
}

