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