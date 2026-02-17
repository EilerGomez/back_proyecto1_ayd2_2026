/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  eiler
 * Created: 16 feb 2026
 */

-- USUARIOS ROLES Y PERFIL
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
    estado ENUM('ACTIVO','SUSPENDIDO','ELIMINADO') DEFAULT 'ACTIVO'
);

CREATE TABLE usuario_roles (
    usuario_id INT,
    rol_id INT,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

CREATE TABLE perfiles (
    usuario_id INT PRIMARY KEY,
    foto_url VARCHAR(500),
    hobbies TEXT,
    intereses TEXT,
    descripcion TEXT,
    gustos TEXT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);


-- INSERTAR UN USUARIO ADMINISTRADOR DEL SISTEMA
insert into roles (nombre) values ("ADMIN"),("EDITOR"),("SUSCRIPTOR"),("ANUNCIANTE");

insert into usuarios(id, nombre,apellido,correo,password_hash) values (1,"administrador","1","gomezeiler250@gmail.com", "$2a$10$Ip/ef5xWRE7p5uMsP2qwUuks1.P1xwjiI50iIgDAbN2KNS6McxU2u");
insert into perfiles (usuario_id) values (1);
insert into usuario_roles(usuario_id,rol_id) values (1,1);