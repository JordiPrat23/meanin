// Java
package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.vetavailability.ExceptionAvailability;
import com.tecnocampus.project.domain.vetavailability.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExceptionAvailabilityRepository extends JpaRepository<ExceptionAvailability, Long> {
    List<ExceptionAvailability> findByVet(Vet vet);
}
