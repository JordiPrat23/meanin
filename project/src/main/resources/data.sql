INSERT INTO vets (name) VALUES
                            ('Dr. Alice López'),
                            ('Dr. Bob García');

INSERT INTO vet_availabilities (vet_id, day_of_week, start_time, end_time)
VALUES
    (1, 'MONDAY', '09:00', '17:00'),
    (1, 'TUESDAY', '09:00', '17:00'),
    (2, 'WEDNESDAY', '10:00', '18:00'),
    (2, 'THURSDAY', '10:00', '18:00');

INSERT INTO vet_availability_exceptions (vet_id, date, start_time, end_time, is_available, reason)
VALUES
    (1, DATEADD('DAY', 2, CURRENT_DATE), NULL, NULL, FALSE, 'Conference'),
    (2, DATEADD('DAY', 3, CURRENT_DATE), NULL, NULL, FALSE, 'Personal Leave');


INSERT INTO medications (name, active_ingredient, dosage_unit, unit_price, reorder_threshold, dosage) VALUES
    ('Amoxicillin 250mg Tablets', 'Amoxicillin', 'mg', 0.50, 50, 250),
    ('Ivermectin 10mg', 'Ivermectin', 'mg', 1.20, 30, 10);

INSERT INTO medication_batches (medication_id, lot_number, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, received_date, storage_location)
VALUES
    (1, 'AMX-001', DATEADD('MONTH', 12, CURRENT_DATE), 200, 200, 0.25, CURRENT_DATE, 'Shelf A'),
    (2, 'IVM-002', DATEADD('MONTH', 8, CURRENT_DATE), 100, 100, 0.80, CURRENT_DATE, 'Shelf B');


INSERT INTO loyalty_tiers (tier_name, required_points, discount_percentage, benefits_description)
VALUES
    ('Bronze', 0, 0.00, 'Base level, no discount'),
    ('Silver', 200, 5.00, '5% discount on services'),
    ('Gold', 500, 10.00, '10% discount and one free annual check-up');
