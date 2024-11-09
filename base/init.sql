CREATE DATABASE IF NOT EXISTS prueba;

USE prueba;

CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    estado CHAR(1),
    intentos_fallidos INT,
    password VARCHAR(255),
    password_acambiar BOOLEAN,
    username VARCHAR(255),
    nombre VARCHAR(255),
    apellido VARCHAR(255)
);

INSERT INTO usuario (email, estado, intentos_fallidos, password, password_acambiar, username, nombre, apellido) 
VALUES ("capital.insigth.no.responder@gmail.com", 'A', 0, '$2a$12$MYgCaATgPN5WaYwZz7DXiu8r2k9PT8f.mi4taR9LpVTp8VATcHSgS', TRUE, 'adminContext', 'Contexto', 'Investments');