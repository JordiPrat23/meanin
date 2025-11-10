package com.tecnocampus.project.service;

import com.tecnocampus.project.api.dto.sale.SaleItemRequest;
import com.tecnocampus.project.api.dto.sale.SaleRequest;
import com.tecnocampus.project.application.services.SaleService;
import com.tecnocampus.project.application.services.MedicationService;
import com.tecnocampus.project.application.services.LoyaltyTierService;
import com.tecnocampus.project.domain.customer.PetOwner;
import com.tecnocampus.project.domain.invoice.Invoice;
import com.tecnocampus.project.domain.invoice.InvoiceItem;
import com.tecnocampus.project.domain.loyalty.LoyaltyTier;
import com.tecnocampus.project.domain.medication.Medication;
import com.tecnocampus.project.persistance.InvoiceRepository;
import com.tecnocampus.project.persistance.MedicationRepository;
import com.tecnocampus.project.persistance.PetOwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleServiceTest {

    private InvoiceRepository invoiceRepository;
    private MedicationRepository medicationRepository;
    private MedicationService medicationService;
    private PetOwnerRepository petOwnerRepository;
    private LoyaltyTierService loyaltyTierService;
    private SaleService saleService;

    @BeforeEach
    void setUp() {
        invoiceRepository = mock(InvoiceRepository.class);
        medicationRepository = mock(MedicationRepository.class);
        medicationService = mock(MedicationService.class);
        petOwnerRepository = mock(PetOwnerRepository.class);
        loyaltyTierService = mock(LoyaltyTierService.class);

        saleService = new SaleService(invoiceRepository, medicationRepository, medicationService, petOwnerRepository, loyaltyTierService);
    }

    @Test
    void createSale_appliesDiscountAndReducesStock() {
        Medication med = new Medication();
        med.setId(1L);
        med.setName("TestMed");
        med.setUnitPrice(BigDecimal.valueOf(10.00));

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(med));
        when(invoiceRepository.save(any())).thenAnswer(invocation -> {
            Invoice inv = invocation.getArgument(0);
            inv.setId(100L);
            return inv;
        });

        LoyaltyTier tier = new LoyaltyTier();
        tier.setDiscountPercentage(BigDecimal.valueOf(10));
        PetOwner owner = new PetOwner();
        owner.setId(50L);
        owner.setName("Alice");
        owner.setLoyaltyTier(tier);

        when(petOwnerRepository.findById(50L)).thenReturn(Optional.of(owner));

        SaleItemRequest itemReq = new SaleItemRequest(1L, 2);
        SaleRequest req = new SaleRequest(50L, List.of(itemReq));

        Invoice saved = saleService.createSale(req);

        assertNotNull(saved);
        assertEquals(100L, saved.getId());
        assertEquals(0, BigDecimal.valueOf(20).compareTo(saved.getSubtotal()));
        assertEquals(0, BigDecimal.valueOf(2).compareTo(saved.getDiscountAmount()));
        assertEquals(0, BigDecimal.valueOf(18).compareTo(saved.getFinalAmount()));

        verify(medicationService, times(1)).reduceStock(1L, 2);
    }

    @Test
    void payInvoice_awardsPointsAndUpdatesLoyaltyTier() {
        Invoice invoice = new Invoice();
        invoice.setId(200L);
        invoice.setFinalAmount(BigDecimal.valueOf(45));
        PetOwner owner = new PetOwner();
        owner.setId(51L);
        owner.setName("Bob");
        invoice.setPetOwner(owner);

        when(invoiceRepository.findById(200L)).thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LoyaltyTier tier = new LoyaltyTier();
        tier.setDiscountPercentage(BigDecimal.ZERO);
        when(loyaltyTierService.findTierByPoints(anyInt())).thenReturn(tier);

        Invoice paid = saleService.payInvoice(200L);

        assertEquals(Invoice.Status.PAID, paid.getStatus());
        assertEquals(4, owner.getFidelityPoints());
        verify(petOwnerRepository, times(1)).save(owner);
    }
}

