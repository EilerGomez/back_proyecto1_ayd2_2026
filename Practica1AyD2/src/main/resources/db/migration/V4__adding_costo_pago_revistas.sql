/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  eiler
 * Created: 25 feb 2026
 */

CREATE TABLE historial_costo_diario_revista (
    id INT PRIMARY KEY AUTO_INCREMENT,
    revista_id INT,
    admin_id INT,
    costo_por_dia DECIMAL(12,2),
    fecha_inicio DATE,
    fecha_fin DATE,
    FOREIGN KEY (revista_id) REFERENCES revistas(id),
    FOREIGN KEY (admin_id) REFERENCES usuarios(id)
);

CREATE TABLE pagos_revista (
    id INT PRIMARY KEY AUTO_INCREMENT,
    revista_id INT,
    editor_id INT,
    monto DECIMAL(12,2),
    fecha_pago DATE NOT NULL,
    periodo_inicio DATE,
    periodo_fin DATE,
    transaccion_id INT,
    FOREIGN KEY (revista_id) REFERENCES revistas(id),
    FOREIGN KEY (editor_id) REFERENCES usuarios(id),
    FOREIGN KEY (transaccion_id) REFERENCES transacciones_cartera(id)
);