/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  eiler
 * Created: 25 feb 2026
 */

CREATE TABLE tipos_anuncio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(12) UNIQUE, -- 'TEXTO','TEXTO_IMAGEN','VIDEO'
    descripcion VARCHAR(200)
);

CREATE TABLE periodos_anuncio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(9) UNIQUE, -- '1_DIA','3_DIAS','1_SEMANA','2_SEMANAS'
    dias INT
);

CREATE TABLE precios_anuncio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tipo_anuncio_id INT,
    periodo_id INT,
    precio DECIMAL(12,2),
    admin_id INT,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (tipo_anuncio_id) REFERENCES tipos_anuncio(id),
    FOREIGN KEY (periodo_id) REFERENCES periodos_anuncio(id),
    FOREIGN KEY (admin_id) REFERENCES usuarios(id)
);

CREATE TABLE anuncios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    anunciante_id INT,
    tipo_anuncio_id INT,
    texto TEXT,
    imagen_url VARCHAR(700),
    video_url VARCHAR(700),
    url_destino VARCHAR(700),
    estado VARCHAR(9) DEFAULT 'BORRADOR', -- 'BORRADOR','ACTIVO','INACTIVO','EXPIRADO'
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (anunciante_id) REFERENCES usuarios(id),
    FOREIGN KEY (tipo_anuncio_id) REFERENCES tipos_anuncio(id)
);

CREATE TABLE compras_anuncio (
    id INT PRIMARY KEY AUTO_INCREMENT,
    anuncio_id INT,
    anunciante_id INT,
    precio_id INT,
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    estado VARCHAR(9) DEFAULT 'ACTIVO', -- 'ACTIVO','INACTIVO','EXPIRADO'
    desactivado_por VARCHAR(11), -- 'SISTEMA','ANUNCIANTE','ADMIN'
    fecha_desactivacion DATETIME,
    transaccion_id INT,
    FOREIGN KEY (anuncio_id) REFERENCES anuncios(id),
    FOREIGN KEY (anunciante_id) REFERENCES usuarios(id),
    FOREIGN KEY (precio_id) REFERENCES precios_anuncio(id),
    FOREIGN KEY (transaccion_id) REFERENCES transacciones_cartera(id)
);

INSERT INTO tipos_anuncio (codigo, descripcion) VALUES('TEXTO', 'Agrega anuncios solo de texto'),('IMAGEN_TEXTO', 'Agrega anuncios con imagen y texto'),
('VIDEO', 'Agrega videos a los anuncios');

INSERT INTO periodos_anuncio(codigo, dias) VALUES ('1_DIA',1),('3_DIAS',3),('1_SEMANA',7),('2_SEMANAS',14);
