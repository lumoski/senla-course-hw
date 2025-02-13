USE hotel;

INSERT INTO room (id, price, capacity, star_rating, status) VALUES
    (1, 50.00, 2, 3, 'AVAILABLE'),
    (2, 100.00, 1, 2, 'AVAILABLE'),
    (3, 90.00, 1, 1, 'AVAILABLE'),
    (4, 150.00, 2, 3, 'AVAILABLE'),
    (5, 200.00, 3, 4, 'AVAILABLE'),
    (6, 120.00, 1, 1, 'AVAILABLE'),
    (7, 80.00, 1, 2, 'AVAILABLE'),
    (8, 180.00, 2, 3, 'AVAILABLE'),
    (9, 220.00, 3, 4, 'AVAILABLE'),
    (10, 300.00, 4, 5, 'AVAILABLE'),
    (11, 1000.00, 1, 5, 'AVAILABLE');
ALTER TABLE room AUTO_INCREMENT = 12;
    
INSERT INTO service (id, name, price, category) VALUES
    (1, 'Room Cleaning', 15.00, 'CLEANING'),
    (2, 'Laundry', 20.00, 'CLEANING'),
    (3, 'Spa Treatment', 50.00, 'WELLNESS'),
    (4, 'Gym Access', 10.00, 'WELLNESS'),
    (5, 'Breakfast', 25.00, 'FOOD'),
    (6, 'Lunch', 30.00, 'FOOD'),
    (7, 'Dinner', 40.00, 'FOOD'),
    (8, 'Airport Shuttle', 60.00, 'TRANSPORT'),
    (9, 'Car Rental', 100.00, 'TRANSPORT'),
    (10, 'Wi-Fi', 5.00, 'OTHER');
ALTER TABLE service AUTO_INCREMENT = 11;

INSERT INTO guest (id, first_name, last_name, email, phone_number, birth_date) VALUES
    (1, 'Ivan', 'Ivanov', 'ivanov@yandex.ru', '+7(999)324-32-43', '1998-12-12'),
    (2, 'Petr', 'Petrov', 'petrov@mail.ru', '+7(912)123-45-67', '1985-05-15'),
    (3, 'Anna', 'Sidorova', 'sidorova@gmail.com', '+7(901)543-21-09', '1990-03-10'),
    (4, 'Olga', 'Smirnova', 'smirnova@outlook.com', '+7(950)123-33-22', '1995-07-20'),
    (5, 'Sergey', 'Kuznetsov', 'kuznetsov@mail.ru', '+7(987)654-32-10', '1988-11-11'),
    (6, 'Elena', 'Morozova', 'morozova@yandex.ru', '+7(996)223-45-67', '1992-06-18'),
    (7, 'Dmitry', 'Volkov', 'volkov@gmail.com', '+7(903)111-22-33', '1980-02-02'),
    (8, 'Ekaterina', 'Zaitseva', 'zaitseva@mail.ru', '+7(915)345-67-89', '2000-01-25'),
    (9, 'Nikolay', 'Popov', 'popov@mail.com', '+7(918)765-43-21', '1993-09-14'),
    (10, 'Maria', 'Sokolova', 'sokolova@outlook.com', '+7(901)876-54-32', '1997-04-05'),
    (11, 'Squid', 'Game', 'game@game.ro', '+65(123)456-78-90', '1999-03-15');
ALTER TABLE guest AUTO_INCREMENT = 12;

INSERT INTO booking (id, room_id, check_in_date, check_out_date, total_price) VALUES
    (1, 1, '2025-01-01', '2025-01-05', 100.00),
    (2, 9, '2025-01-10', '2025-01-15', 660.00);
ALTER TABLE booking AUTO_INCREMENT = 3;

INSERT INTO booking_guest (booking_id, guest_id) VALUES
    (1, 7),
    (1, 9),
    (2, 1),
    (2, 2),
    (2, 3);
    
