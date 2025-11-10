package com.tecnocampus.project.application.services;

import com.tecnocampus.project.api.dto.sale.SaleItemRequest;
import com.tecnocampus.project.api.dto.sale.SaleRequest;
import com.tecnocampus.project.domain.customer.PetOwner;
import com.tecnocampus.project.domain.invoice.Invoice;
import com.tecnocampus.project.domain.invoice.InvoiceItem;
import com.tecnocampus.project.domain.loyalty.LoyaltyTier;
import com.tecnocampus.project.domain.medication.Medication;
import com.tecnocampus.project.persistance.InvoiceRepository;
import com.tecnocampus.project.persistance.PetOwnerRepository;
import com.tecnocampus.project.persistance.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class SaleService {

    private final InvoiceRepository invoiceRepository;
    private final MedicationRepository medicationRepository;
    private final MedicationService medicationService;
    private final PetOwnerRepository petOwnerRepository;
    private final LoyaltyTierService loyaltyTierService;

    public SaleService(InvoiceRepository invoiceRepository,
                       MedicationRepository medicationRepository,
                       MedicationService medicationService,
                       PetOwnerRepository petOwnerRepository,
                       LoyaltyTierService loyaltyTierService) {
        this.invoiceRepository = invoiceRepository;
        this.medicationRepository = medicationRepository;
        this.medicationService = medicationService;
        this.petOwnerRepository = petOwnerRepository;
        this.loyaltyTierService = loyaltyTierService;
    }

    public Invoice createSale(SaleRequest request) {
        if (request == null || request.items() == null || request.items().isEmpty())
            throw new IllegalArgumentException("Sale must include at least one item");

        Invoice invoice = new Invoice();

        BigDecimal subtotal = BigDecimal.ZERO;

        for (SaleItemRequest it : request.items()) {
            Medication med = medicationRepository.findById(it.medicationId())
                    .orElseThrow(() -> new IllegalArgumentException("Medication not found: " + it.medicationId()));

            BigDecimal unitPrice = med.getUnitPrice() == null ? BigDecimal.ZERO : med.getUnitPrice();
            int qty = it.quantity();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(qty));

            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setMedicationId(med.getId());
            item.setDescription(med.getName());
            item.setUnitPrice(unitPrice);
            item.setQuantity(qty);
            item.setLineTotal(lineTotal.setScale(2, RoundingMode.HALF_UP));

            invoice.getItems().add(item);
            subtotal = subtotal.add(lineTotal);

            medicationService.reduceStock(med.getId(), qty);
        }

        invoice.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));

        PetOwner owner = null;
        if (request.petOwnerId() != null) {
            owner = petOwnerRepository.findById(request.petOwnerId()).orElse(null);
            invoice.setPetOwner(owner);
        }

        BigDecimal discountAmount = BigDecimal.ZERO;
        if (owner != null && owner.getLoyaltyTier() != null && owner.getLoyaltyTier().getDiscountPercentage() != null) {
            BigDecimal discountPercentage = owner.getLoyaltyTier().getDiscountPercentage();
            if (discountPercentage.compareTo(BigDecimal.ZERO) > 0) {
                discountAmount = invoice.getSubtotal().multiply(discountPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            }
        }

        invoice.setDiscountAmount(discountAmount.setScale(2, RoundingMode.HALF_UP));
        BigDecimal finalAmount = invoice.getSubtotal().subtract(invoice.getDiscountAmount()).setScale(2, RoundingMode.HALF_UP);
        invoice.setFinalAmount(finalAmount);

        Invoice saved = invoiceRepository.save(invoice);
        return saved;
    }

    public Invoice payInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new IllegalArgumentException("Invoice not found: " + invoiceId));
        if (invoice.getStatus() == Invoice.Status.PAID) return invoice;

        invoice.setStatus(Invoice.Status.PAID);
        invoiceRepository.save(invoice);

        PetOwner owner = invoice.getPetOwner();
        if (owner != null) {
            BigDecimal divisor = BigDecimal.TEN;
            int pointsEarned = invoice.getFinalAmount().divide(divisor, RoundingMode.DOWN).intValue();
            owner.setFidelityPoints(owner.getFidelityPoints() + pointsEarned);

            LoyaltyTier newTier = loyaltyTierService.findTierByPoints(owner.getFidelityPoints());
            if (newTier != null) owner.setLoyaltyTier(newTier);

            petOwnerRepository.save(owner);
        }

        return invoice;
    }
}

