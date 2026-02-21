/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  eiler
 * Created: 16 feb 2026
 */

-- USUARIOS ROLES, PERFIL Y CARTERA DIGITAL
CREATE TABLE roles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE-- ADMIN, EDITOR, SUSCRIPTOR, ANUNCIANTE
);

CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    nombre VARCHAR(150),
    apellido VARCHAR(150),    
    correo VARCHAR(180) NOT NULL UNIQUE,
    password_hash VARCHAR(260) NOT NULL,
    estado VARCHAR(10) DEFAULT "ACTIVO"
);

CREATE TABLE usuario_roles (
    usuario_id INT,
    rol_id INT,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE perfiles (
    usuario_id INT PRIMARY KEY,
    foto_url VARCHAR(500),
    hobbies TEXT,
    intereses TEXT,
    descripcion TEXT,
    gustos TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE carteras (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT UNIQUE,
    saldo DECIMAL(12,2) DEFAULT 0.00,
    moneda CHAR(3) DEFAULT 'GTQ',
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE transacciones_cartera (
    id INT PRIMARY KEY AUTO_INCREMENT,
    cartera_id INT,
    tipo VARCHAR(16), -- 'RECARGA','COMPRA_ANUNCIO','BLOQUEO_ANUNCIO','PAGO_REVISTA','AJUSTE'
    direccion VARCHAR(7), -- 'CREDITO','DEBITO'
    monto DECIMAL(12,2),
    referencia_tipo VARCHAR(50),
    referencia_id INT,
    nota VARCHAR(255),
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_registrada_usuario DATE,
    FOREIGN KEY (cartera_id) REFERENCES carteras(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- INSERTAR UN USUARIO ADMINISTRADOR DEL SISTEMA
insert into roles (nombre) values ("ADMIN"),("EDITOR"),("SUSCRIPTOR"),("ANUNCIANTE");

insert into usuarios(id, nombre,username,apellido,correo,password_hash) values (1,"administrador","admin1","1","gomezeiler250@gmail.com", "$2a$10$maFJe5RGwJUuIArBS9brxO.ZC6B1P4dzau7ZdxDiAJUJ.9emxnpEy");-- admin123
insert into perfiles (usuario_id) values (1);
insert into usuario_roles(usuario_id,rol_id) values (1,1);
insert into carteras(usuario_id) values(1);