package com.tecnocampus.project.persistance;

import com.tecnocampus.project.domain.invoice.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
}

