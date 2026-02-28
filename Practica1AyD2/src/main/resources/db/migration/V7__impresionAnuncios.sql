/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  eiler
 * Created: 28 feb 2026
 */

CREATE TABLE impresiones_anuncio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    anuncio_id INT,
    compra_id INT,
    fecha_mostrado DATETIME DEFAULT CURRENT_TIMESTAMP,
    url_pagina VARCHAR(700),
    revista_id INT,
    FOREIGN KEY (anuncio_id) REFERENCES anuncios(id),
    FOREIGN KEY (compra_id) REFERENCES compras_anuncio(id),
    FOREIGN KEY (revista_id) REFERENCES revistas(id)
);