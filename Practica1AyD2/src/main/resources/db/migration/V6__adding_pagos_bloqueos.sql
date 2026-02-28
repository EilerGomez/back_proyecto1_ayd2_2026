/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  eiler
 * Created: 27 feb 2026
 */

CREATE TABLE precio_bloqueo_anuncio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    revista_id INT UNIQUE,
    costo_por_dia DECIMAL(12,2),
    admin_id INT,
    FOREIGN KEY (revista_id) REFERENCES revistas(id),
    FOREIGN KEY (admin_id) REFERENCES usuarios(id)
);

CREATE TABLE bloqueos_anuncio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    revista_id INT,
    editor_id INT,
    dias INT,
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    monto DECIMAL(12,2),
    estado VARCHAR(9) DEFAULT 'ACTIVO', -- 'ACTIVO','INACTIVO','EXPIRADO'
    transaccion_id INT,
    FOREIGN KEY (revista_id) REFERENCES revistas(id),
    FOREIGN KEY (editor_id) REFERENCES usuarios(id),
    FOREIGN KEY (transaccion_id) REFERENCES transacciones_cartera(id)
);

CREATE TABLE costo_global_revistas(
    id INT PRIMARY KEY AUTO_INCREMENT,
    monto DECIMAL(12,2)
);

INSERT INTO costo_global_revistas(monto) VALUES (30.00);