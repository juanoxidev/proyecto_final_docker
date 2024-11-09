-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS prueba;
USE prueba;

-- Crear tabla 'usuario' con los nuevos campos y relaciones
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    apellido VARCHAR(255) DEFAULT NULL,
    fecha_creacion DATETIME DEFAULT NULL,
    fecha_modi DATETIME DEFAULT NULL,
    email VARCHAR(255) DEFAULT NULL,
    estado VARCHAR(255) DEFAULT NULL,
    ultimo_intento DATETIME DEFAULT NULL,
    intentos_fallidos INT DEFAULT NULL,
    nombre VARCHAR(255) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    password_acambiar BIT(1) DEFAULT NULL,
    username VARCHAR(255) DEFAULT NULL,
    usuario_creacion BIGINT DEFAULT NULL,
    usuario_modi BIGINT DEFAULT NULL,
    PRIMARY KEY (id),
    KEY FKk54tats85mliosxjaqhva5lr6 (usuario_creacion),
    KEY FKj9mj9pomweet8iate6uyhl109 (usuario_modi),
    CONSTRAINT FKj9mj9pomweet8iate6uyhl109 FOREIGN KEY (usuario_modi) REFERENCES usuario(id),
    CONSTRAINT FKk54tats85mliosxjaqhva5lr6 FOREIGN KEY (usuario_creacion) REFERENCES usuario(id)
);

-- Insertar un usuario de ejemplo
INSERT INTO usuario (email, estado, intentos_fallidos, password, password_acambiar, username, nombre, apellido) 
VALUES 
    ("capital.insigth.no.responder@gmail.com", 'A', 0, '$2a$12$MYgCaATgPN5WaYwZz7DXiu8r2k9PT8f.mi4taR9LpVTp8VATcHSgS', TRUE, 'adminContext', 'Contexto', 'Investments');

-- Crear tabla de 'rol' si no existe
CREATE TABLE IF NOT EXISTS rol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL
);

-- Insertar los roles
INSERT INTO rol (descripcion) VALUES ('ADMIN');
INSERT INTO rol (descripcion) VALUES ('USER');

-- Crear tabla de 'usuario_rol' si no existe (relación entre usuarios y roles)
CREATE TABLE IF NOT EXISTS usuario_rol (
    usuario_id BIGINT,  -- Cambiado a BIGINT
    rol_id BIGINT,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES rol(id) ON DELETE CASCADE
);


INSERT INTO usuario_rol (usuario_id, rol_id) VALUES (1, 1);  -- Asignar 'ADMIN' a usuario con id 1
