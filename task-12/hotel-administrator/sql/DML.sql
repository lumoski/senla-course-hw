USE hotel;

INSERT INTO room (room_number, price, capacity, star_rating, status, created_at, updated_at) VALUES
    ('B12', 100.00, 1, 4, 'AVAILABLE', NOW(), NOW()),
    ('A01', 150.00, 2, 3, 'AVAILABLE', NOW(), NOW()),
    ('C03', 80.00, 1, 4, 'AVAILABLE', NOW(), NOW()),
    ('D45', 120.50, 3, 5, 'AVAILABLE', NOW(), NOW()),
    ('E99', 200.00, 5, 5, 'AVAILABLE', NOW(), NOW());
ALTER TABLE room AUTO_INCREMENT = 6;

INSERT INTO guest (first_name, last_name, email, phone_number, passport_number, gender, address, birth_date, status) VALUES
    ('Ivan', 'Ivanov', 'ivan.ivanov@example.com', '+71234567890', '1234567890', 'male', 'Moscow, Red Square, 1', '1990-01-01', 'REGULAR'),
    ('Maria', 'Petrova', 'maria.petrova@example.com', '+71234567891', '1234567891', 'female', 'Saint-Petersburg, Neva River, 2', '1985-02-14', 'VIP'),
    ('Aleksey', 'Sidorov', 'aleksey.sidorov@example.com', '+71234567892', '1234567892', 'male', 'Yekaterinburg, Ural St, 3', '1988-05-21', 'NEW'),
    ('Elena', 'Tikhonova', 'elena.tikhonova@example.com', '+71234567893', '1234567893', 'female', 'Kazan, Kremlin, 4', '1995-03-15', 'BLACKLISTED'),
    ('Sergey', 'Kovalev', 'sergey.kovalev@example.com', '+71234567894', '1234567894', 'male', 'Nizhny Novgorod, Volga River, 5', '1980-12-30', 'LOYAL');
ALTER TABLE room AUTO_INCREMENT = 6;

INSERT INTO amenity (name, price, category) VALUES
    ('Massage', 50.00, 'WELLNESS'),
    ('Airport Taxi', 15.00, 'TRANSPORT'),
    ('Lunch at Restaurant', 25.00, 'FOOD'),
    ('Laundry Service', 10.00, 'CLEANING'),
    ('City Tour', 80.00, 'OTHER');
ALTER TABLE room AUTO_INCREMENT = 6;