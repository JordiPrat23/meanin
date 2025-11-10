package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.customer.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {
}

