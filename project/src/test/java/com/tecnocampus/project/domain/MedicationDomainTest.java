package com.tecnocampus.project.domain;

import com.tecnocampus.project.domain.medication.Medication;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MedicationDomainTest {
    @Test
    void testMedicationCreation() {
        Medication medication = new Medication("Ibuprofeno", "Dolor", 200);
        assertEquals("Ibuprofeno", medication.getName());
        assertEquals("Dolor", medication.getPurpose());
        assertEquals(200, medication.getDosage());
    }

    @Test
    void testMedicationUpdateDosage() {
        Medication medication = new Medication("Paracetamol", "Fiebre", 500);
        medication.setDosage(650);
        assertEquals(650, medication.getDosage());
    }
}
