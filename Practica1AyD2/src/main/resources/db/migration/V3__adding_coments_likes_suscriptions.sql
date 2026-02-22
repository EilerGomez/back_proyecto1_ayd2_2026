/* 
 * Click nGfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  eiler
 * Created: 21 feb 2026
 */

DROP TABLE IF EXISTS suscripciones;
CREATE TABLE suscripciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    revista_id INT,
    usuario_id INT,
    fecha_suscripcion DATE NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (revista_id) REFERENCES revistas(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (revista_id, usuario_id)
);


DROP TABLE IF EXISTS likes_revista;
CREATE TABLE likes_revista (
    id INT PRIMARY KEY AUTO_INCREMENT,
    revista_id INT,
    usuario_id INT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (revista_id) REFERENCES revistas(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (revista_id, usuario_id)
);

DROP TABLE IF EXISTS comentarios;
CREATE TABLE comentarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    revista_id INT,
    usuario_id INT,
    contenido TEXT,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (revista_id) REFERENCES revistas(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE
);
