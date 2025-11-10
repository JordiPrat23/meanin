CREATE TABLE vets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255)
);

CREATE TABLE vet_availabilities (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vet_id BIGINT,
    day_of_week VARCHAR(10),
    start_time TIME,
    end_time TIME,
    FOREIGN KEY (vet_id) REFERENCES vets(id)
);

CREATE TABLE vet_availability_exceptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vet_id BIGINT,
    date DATE,
    start_time TIME,
    end_time TIME,
    is_available BOOLEAN,
    reason VARCHAR(255),
    FOREIGN KEY (vet_id) REFERENCES vets(id)
);

CREATE TABLE medications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    active_ingredient VARCHAR(255),
    dosage_unit VARCHAR(10),
    unit_price DECIMAL(10, 2),
    reorder_threshold INT
);

CREATE TABLE medication_batches (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    medication_id BIGINT,
    lot_number VARCHAR(50),
    expiry_date DATE,
    initial_quantity INT,
    current_quantity INT,
    purchase_price_per_unit DECIMAL(10, 2),
    received_date DATE,
    storage_location VARCHAR(100),
    FOREIGN KEY (medication_id) REFERENCES medications(id)
);

CREATE TABLE loyalty_tiers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    tier_name VARCHAR(50),
    required_points INT,
    discount_percentage DECIMAL(5, 2),
    benefits_description VARCHAR(255)
);

CREATE TABLE promotions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    description VARCHAR(255),
    start_date DATE,
    end_date DATE,
    max_uses INT,
    promo_code VARCHAR(50)
);

CREATE TABLE discount (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(50),
    discount_value DOUBLE,
    target VARCHAR(100)
);

CREATE TABLE promotion_discounts (
    promotion_id BIGINT NOT NULL,
    discounts_id BIGINT NOT NULL,
    PRIMARY KEY (promotion_id, discounts_id),
    FOREIGN KEY (promotion_id) REFERENCES promotions(id),
    FOREIGN KEY (discounts_id) REFERENCES discount(id)
);

CREATE TABLE low_stock_alert (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    medication_id BIGINT,
    status VARCHAR(20),
    note VARCHAR(255),
    FOREIGN KEY (medication_id) REFERENCES medications(id)
);

INSERT INTO vets (id, name)
VALUES (1, 'Dr. Alice'), (2, 'Dr. Bob');

INSERT INTO vet_availabilities (id, vet_id, day_of_week, start_time, end_time)
VALUES
    (1, 1, 'MONDAY', '09:00', '17:00'),
    (2, 1, 'TUESDAY', '09:00', '17:00'),
    (3, 2, 'WEDNESDAY', '10:00', '18:00');

INSERT INTO vet_availability_exceptions (id, vet_id, date, start_time, end_time, is_available, reason)
VALUES
    (1, 1, DATEADD('DAY', 1, CURRENT_DATE), NULL, NULL, FALSE, 'Holiday');

INSERT INTO medications (id, name, active_ingredient, dosage_unit, unit_price, reorder_threshold)
VALUES
    (1, 'Amoxicillin 250mg Tablets', 'Amoxicillin', 'mg', 0.50, 50),
    (2, 'Ivermectin 10mg', 'Ivermectin', 'mg', 1.20, 30);

INSERT INTO medication_batches (id, medication_id, lot_number, expiry_date, initial_quantity, current_quantity, purchase_price_per_unit, received_date, storage_location)
VALUES
    (1, 1, 'AMX-001', DATEADD('MONTH', 12, CURRENT_DATE), 200, 200, 0.25, CURRENT_DATE, 'Shelf A'),
    (2, 2, 'IVM-002', DATEADD('MONTH', 6, CURRENT_DATE), 100, 100, 0.80, CURRENT_DATE, 'Shelf B');

INSERT INTO loyalty_tiers (id, tier_name, required_points, discount_percentage, benefits_description)
VALUES
    (1, 'Bronze', 0, 0.00, 'Base tier, no discount'),
    (2, 'Silver', 200, 5.00, '5% discount'),
    (3, 'Gold', 500, 10.00, '10% discount and free annual check-up');

INSERT INTO promotions (id, name, description, start_date, end_date, max_uses, promo_code)
VALUES (1, 'Autumn Vaccination Drive', '20% off on all vaccination services during autumn', CURRENT_DATE, DATEADD('MONTH', 2, CURRENT_DATE), 150, 'AUTUMN20');

INSERT INTO discount (id, type, discount_value, target)
VALUES (1, 'percentage', 20, 'ClinicService:Vaccination');

INSERT INTO promotion_discounts (promotion_id, discounts_id)
VALUES (1, 1);

INSERT INTO low_stock_alert (id, medication_id, status, note)
VALUES (1, 1, 'OPEN', 'Stock below threshold'),
       (2, 2, 'ACKNOWLEDGED', 'Order placed');
