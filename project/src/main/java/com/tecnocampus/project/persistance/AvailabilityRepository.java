package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.vetavailability.Availability;
import com.tecnocampus.project.domain.vetavailability.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    List<Availability> findByVet(Vet vet);
}
