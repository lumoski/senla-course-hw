-- создание бд
CREATE DATABASE IF NOT EXISTS computer_store;
USE computer_store;

-- drop table if exists pc;
-- drop table if exists laptop;
-- drop table if exists printer;
-- drop table if exists product;

-- Создание таблицы product
CREATE TABLE product (
    maker VARCHAR(10),
    model VARCHAR(50) PRIMARY KEY,
    type VARCHAR(50)
);

-- Создание таблицы pc
CREATE TABLE pc (
    code INT PRIMARY KEY AUTO_INCREMENT,
    model VARCHAR(50),
    speed SMALLINT,
    ram SMALLINT,
    hd REAL,
    cd VARCHAR(10),
    price DECIMAL(10,2),
    FOREIGN KEY (model) REFERENCES product(model)
) AUTO_INCREMENT = 1;

-- Создание таблицы laptop
CREATE TABLE laptop (
    code INT PRIMARY KEY AUTO_INCREMENT,
    model VARCHAR(50),
    speed SMALLINT,
    ram SMALLINT,
    hd REAL,
    price DECIMAL(10,2),
    screen TINYINT,
    FOREIGN KEY (model) REFERENCES product(model)
) AUTO_INCREMENT = 1;

-- Создание таблицы printer
CREATE TABLE printer (
    code INT PRIMARY KEY AUTO_INCREMENT,
    model VARCHAR(50),
    color CHAR(1),
    type VARCHAR(10),
    price DECIMAL(10,2),
    FOREIGN KEY (model) REFERENCES product(model)
) AUTO_INCREMENT = 1;

SHOW TABLES;