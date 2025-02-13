CREATE DATABASE IF NOT EXISTS hotel;
USE hotel;

-- Создание таблицы для гостей
CREATE TABLE guest (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(20),
    birth_date DATE
);

-- Создание таблицы для комнат
CREATE TABLE room (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    price DECIMAL(10,2) NOT NULL,
    capacity INT NOT NULL,
    star_rating INT NOT NULL,
    status ENUM('AVAILABLE', 'OCCUPIED', 'REPAIR') NOT NULL DEFAULT 'AVAILABLE'
);

-- Создание таблицы для услуг
CREATE TABLE service (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category ENUM('FOOD', 'CLEANING', 'TRANSPORT', 'WELLNESS', 'OTHER') NOT NULL
);

-- Создание таблицы для бронирований
CREATE TABLE booking (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    room_id BIGINT NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (room_id) REFERENCES room(id) ON DELETE RESTRICT
);

-- Создание связующей таблицы между бронированиями и гостями
CREATE TABLE booking_guest (
    booking_id BIGINT,
    guest_id BIGINT,
    PRIMARY KEY (booking_id, guest_id),
    FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE,
    FOREIGN KEY (guest_id) REFERENCES guest(id) ON DELETE RESTRICT
);

-- Создание таблицы для покупок услуг гостями
CREATE TABLE guest_service_purchase (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    guest_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    purchase_date DATE NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (guest_id) REFERENCES guest(id) ON DELETE RESTRICT,
    FOREIGN KEY (service_id) REFERENCES service(id) ON DELETE RESTRICT
);
