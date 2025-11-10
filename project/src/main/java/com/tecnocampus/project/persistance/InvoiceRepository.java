package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.invoice.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}

