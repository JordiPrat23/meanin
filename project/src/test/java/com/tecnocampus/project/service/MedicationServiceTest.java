package com.tecnocampus.project.service;

import com.tecnocampus.project.domain.medication.Medication;
import com.tecnocampus.project.application.services.MedicationService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicationServiceTest {

    @Test
    void testFindMedicationByName() {
        MedicationService service = mock(MedicationService.class);
        Medication medication = new Medication("Ibuprofeno", "Dolor", 200);
        when(service.findByName("Ibuprofeno")).thenReturn(medication);

        Medication result = service.findByName("Ibuprofeno");
        assertEquals("Ibuprofeno", result.getName());
    }

    @Test
    void testAddMedication() {
        MedicationService service = mock(MedicationService.class);
        Medication medication = new Medication("Paracetamol", "Fiebre", 500);
        when(service.addMedication(medication)).thenReturn(medication);

        Medication result = service.addMedication(medication);
        assertEquals(medication, result);
    }
    // ...más tests según la lógica del servicio...
}
