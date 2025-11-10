package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.vetavailability.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetRepository extends JpaRepository<Vet, Long> {}
