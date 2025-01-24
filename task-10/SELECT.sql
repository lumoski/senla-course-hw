USE computer_store;

-- 1
SELECT model, speed, hd FROM pc
WHERE price < 500;

-- 2
SELECT DISTINCT product.maker FROM product JOIN printer 
ON printer.model = product.model;

-- 3
SELECT model, ram, hd, screen FROM laptop
WHERE price > 1000;

-- 4
SELECT * FROM printer WHERE color = 'Y';

-- 5
SELECT model, speed, hd FROM pc
WHERE (cd LIKE '%12x' OR cd LIKE '%24x') AND price < 500;

-- 6
SELECT product.maker, laptop.speed FROM product JOIN laptop
ON product.model = laptop.model
WHERE laptop.hd >= 100;

-- 7
WITH all_products AS (
    SELECT model, price, 'PC' as type FROM pc
    UNION ALL
    SELECT model, price, 'Laptop' as type FROM laptop
    UNION ALL
    SELECT model, price, 'Printer' as type FROM printer
)
SELECT 
    p.model,
    p.type,
    a.price as price
FROM product p
JOIN all_products a ON p.model = a.model
WHERE p.maker LIKE 'D%'
ORDER BY p.model;

-- 8
SELECT DISTINCT maker FROM product
WHERE maker IN (SELECT maker FROM product WHERE type = 'PC')
AND maker NOT IN (SELECT maker FROM product WHERE type = 'laptop');

-- 9
SELECT DISTINCT product.maker FROM product
JOIN pc ON product.model = pc.model AND pc.speed > 4500;

-- 10
SELECT model, price FROM printer
ORDER BY price DESC
LIMIT 3;

-- 11
SELECT AVG(speed) FROM pc;

-- 12
SELECT AVG(speed) FROM laptop
WHERE price > 1000;

-- 13
SELECT AVG(pc.speed) FROM product
JOIN pc ON product.model = pc.model
WHERE product.maker = 'Lenovo';

-- 14
SELECT speed, AVG(price) FROM pc
GROUP BY speed;

-- 15
SELECT hd FROM pc
GROUP BY hd HAVING COUNT(*) >= 2;

-- 16
SELECT DISTINCT 
    pc1.model as model_1,
    pc2.model as model_2,
    pc1.speed,
    pc1.ram
FROM pc pc1
JOIN pc pc2 ON pc1.speed = pc2.speed 
    AND pc1.ram = pc2.ram 
    AND pc1.model > pc2.model
ORDER BY pc1.model;

-- 17
SELECT product.type,
	laptop.model,
    laptop.speed
FROM laptop
JOIN product ON laptop.model = product.model
WHERE laptop.speed < (SELECT MIN(speed) FROM pc);

-- 18
SELECT product.maker, printer.price
FROM product
JOIN printer ON printer.model = product.model
	AND printer.color = 'Y'
ORDER BY price
LIMIT 3;

-- 19
SELECT product.maker AS maker,
	AVG(laptop.screen) AS average_screen
FROM product
JOIN laptop ON product.model = laptop.model
GROUP BY maker;

-- 20
SELECT maker, COUNT(*)
FROM product
WHERE type = 'PC'
GROUP BY maker HAVING COUNT(*) >= 3;

-- 21
SELECT product.maker,
	MAX(pc.price)
FROM product
JOIN pc ON product.model = pc.model
GROUP BY product.maker;

-- 22
SELECT speed, AVG(price)
FROM pc
WHERE speed > 600
GROUP BY speed;

-- 23
SELECT DISTINCT p1.maker
FROM product p1
JOIN pc ON p1.model = pc.model AND pc.speed >= 750
JOIN product p2 ON p1.maker = p2.maker
JOIN laptop l ON p2.model = l.model AND l.speed >= 750;

-- 24
WITH all_prices AS (
    SELECT model, price, 'PC' as type FROM pc
    UNION ALL
    SELECT model, price, 'Laptop' FROM laptop
    UNION ALL
    SELECT model, price, 'Printer' FROM printer
)
SELECT 
    p.maker,
    a.model,
    a.type,
    a.price
FROM all_prices a
JOIN product p ON a.model = p.model
WHERE a.price = (
    SELECT MAX(price) 
    FROM all_prices
)
ORDER BY a.model;

-- 25
WITH min_ram_pcs AS (
    SELECT pc.*
    FROM pc
    WHERE ram = (SELECT MIN(ram) FROM pc)
)
SELECT DISTINCT 
    p.maker,
    pc.model as pc_model,
    pc.speed as pc_speed,
    pc.ram as pc_ram,
    pr.model as printer_model
FROM product p
JOIN printer pr ON p.model = pr.model
JOIN product p2 ON p.maker = p2.maker
JOIN min_ram_pcs pc ON p2.model = pc.model
WHERE pc.speed = (
    SELECT MAX(speed)
    FROM min_ram_pcs
)
ORDER BY p.maker;