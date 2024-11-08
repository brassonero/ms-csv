-- Create CAJEROS table
CREATE TABLE CAJEROS (
    Codigo SERIAL PRIMARY KEY,
    NomApels VARCHAR(255)
);

-- Create MAQUINAS_REGISTRADORAS table
CREATE TABLE MAQUINAS_REGISTRADORAS (
    Codigo SERIAL PRIMARY KEY,
    Piso INT
);

-- Create PRODUCTOS table
CREATE TABLE PRODUCTOS (
    Codigo SERIAL PRIMARY KEY,
    Nombre VARCHAR(100),
    Precio INT
);

-- Create VENTA table
CREATE TABLE VENTA (
    Cajero INT,
    Maquina INT,
    Producto INT,
    FOREIGN KEY (Cajero) REFERENCES CAJEROS(Codigo),
    FOREIGN KEY (Maquina) REFERENCES MAQUINAS_REGISTRADORAS(Codigo),
    FOREIGN KEY (Producto) REFERENCES PRODUCTOS(Codigo),
    PRIMARY KEY (Cajero, Maquina, Producto)
);

-- Insert data into Cajeros
INSERT INTO CAJEROS (NomApels) VALUES 
    ('Homero Gomez'),
    ('Daniela Ambriz'),
    ('Jose Amador');

-- Insert data into Productos
INSERT INTO PRODUCTOS (Nombre, Precio) VALUES 
    ('Producto A', 100),
    ('Producto B', 150),
    ('Producto C', 200);

-- Insert data into Maquinas_Registradoras
INSERT INTO MAQUINAS_REGISTRADORAS (Piso) VALUES 
    (1),
    (2),
    (3);

-- Insert data into Venta
INSERT INTO VENTA (Cajero, Maquina, Producto) VALUES
    (1, 1, 1),
    (1, 2, 2),
    (2, 1, 3),
    (3, 3, 1);

-- b. Number of sales per product, ordered by most to least sales
SELECT p.Codigo, p.Nombre, COUNT(*) as num_ventas
FROM PRODUCTOS p
JOIN VENTA v ON p.Codigo = v.Producto
GROUP BY p.Codigo, p.Nombre
ORDER BY num_ventas DESC;

-- c. Complete sales report with cashier name, product details and floor
SELECT c.NomApels as cajero, p.Nombre as producto, p.Precio, m.Piso
FROM VENTA v
JOIN CAJEROS c ON v.Cajero = c.Codigo
JOIN PRODUCTOS p ON v.Producto = p.Codigo
JOIN MAQUINAS_REGISTRADORAS m ON v.Maquina = m.Codigo;

-- d. Total sales by floor
SELECT m.Piso, COUNT(*) as total_ventas
FROM VENTA v
JOIN MAQUINAS_REGISTRADORAS m ON v.Maquina = m.Codigo
GROUP BY m.Piso
ORDER BY m.Piso;

-- e. Total sales amount per cashier
SELECT c.Codigo, c.NomApels, SUM(p.Precio) as total_ventas
FROM VENTA v
JOIN CAJEROS c ON v.Cajero = c.Codigo
JOIN PRODUCTOS p ON v.Producto = p.Codigo
GROUP BY c.Codigo, c.NomApels
ORDER BY c.Codigo;

-- f. Cashiers with total sales less than 5000 on any floor
SELECT DISTINCT c.Codigo, c.NomApels
FROM CAJEROS c
JOIN VENTA v ON c.Codigo = v.Cajero
JOIN PRODUCTOS p ON v.Producto = p.Codigo
JOIN MAQUINAS_REGISTRADORAS m ON v.Maquina = m.Codigo
GROUP BY c.Codigo, c.NomApels, m.Piso
HAVING SUM(p.Precio) < 5000;
