/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  eiler
 * Created: 20 feb 2026
 */

-- CREACION DE REVISTAS, ETIQUETAS, CATEGORIAS, ETC
CREATE TABLE categorias (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(120) UNIQUE,
    descripcion VARCHAR(255)
);

CREATE TABLE etiquetas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) UNIQUE
);

CREATE TABLE revistas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    editor_id INT,
    titulo VARCHAR(200),
    descripcion TEXT,
    categoria_id INT,
    fecha_creacion DATE NOT NULL,
    activa BOOLEAN DEFAULT TRUE,
    permite_comentarios BOOLEAN DEFAULT TRUE,
    permite_likes BOOLEAN DEFAULT TRUE,
    permite_suscripciones BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (editor_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE revista_etiquetas (
    revista_id INT,
    etiqueta_id INT,
    PRIMARY KEY (revista_id, etiqueta_id),
    FOREIGN KEY (revista_id) REFERENCES revistas(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (etiqueta_id) REFERENCES etiquetas(id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ediciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    revista_id INT,
    numero_edicion VARCHAR(60),
    titulo VARCHAR(200),
    pdf_url VARCHAR(700),
    fecha_publicacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (revista_id) REFERENCES revistas(id) ON DELETE CASCADE ON UPDATE CASCADE
);


INSERT INTO categorias (nombre, descripcion) VALUES 
('Hogar y Jardín', 'Decoración, bricolaje, jardinería y organización de espacios.'),
('Automotriz', 'Reseñas de vehículos, mecánica, industria automotriz y deportes de motor.'),
('Historia y Arqueología', 'Relatos del pasado, descubrimientos arqueológicos y civilizaciones antiguas.'),
('Psicología y Autoayuda', 'Bienestar emocional, desarrollo personal y comportamiento humano.'),
('Religión y Espiritualidad', 'Estudio de credos, prácticas espirituales y filosofía de vida.'),
('Música', 'Reseñas discográficas, perfiles de artistas y tendencias musicales.'),
('Videojuegos y E-sports', 'Análisis de juegos, consolas y el mundo competitivo de los e-sports.'),
('Infantil y Juvenil', 'Contenido educativo y de entretenimiento para niños y adolescentes.'),
('Arquitectura y Diseño', 'Planificación urbana, diseño de interiores y tendencias arquitectónicas.'),
('Derecho y Justicia', 'Análisis legal, jurisprudencia y temas de justicia social.'),
('Fotografía', 'Técnicas fotográficas, equipo, galerías y arte visual.'),
('Cine y Series', 'Crítica cinematográfica, estrenos y mundo del streaming.');


INSERT INTO etiquetas (nombre) VALUES 
('JavaScript'), ('Python'), ('Java'), ('Spring Boot'), ('React'), -- Programación
('Real Madrid'), ('FC Barcelona'), ('NBA'), ('Fórmula 1'), ('Tenis'), -- Deportes específicos
('Postres'), ('Comida Italiana'), ('Sushi'), ('Vinos'), ('Café'), -- Gastronomía
('Inversiones'), ('Bitcoin'), ('Acciones'), ('Startups'), ('Liderazgo'), -- Finanzas/Negocios
('Yoga'), ('Running'), ('Meditación'), ('Salud Mental'), ('Nutrición'), -- Salud
('NASA'), ('Cambio Climático'), ('Biología'), ('Física Cuántica'), -- Ciencia
('Netflix'), ('Marvel'), ('Anime'), ('Hollywood'), ('K-Pop'), -- Entretenimiento
('Europa'), ('Sudamérica'), ('Mochileros'), ('Hoteles de Lujo'), ('Playas'), -- Viajes
('DIY'), ('Minimalismo'), ('Vintage'), ('Plantas de interior'), -- Hogar
('Android'), ('iOS'), ('Gadgets'), ('Hardware'), ('Ciberseguridad'), -- Tecnología específica
('Derechos Humanos'), ('Elecciones'), ('Geopolítica'), ('Sustentabilidad'); -- Actualidad