/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.modelos;

/**
 *
 * @author eiler
 */

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "anuncios")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class EntidadAnuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "anunciante_id")
    private Integer anuncianteId;

    @Column(name = "tipo_anuncio_id")
    private Integer tipoAnuncioId;

    @Column(columnDefinition = "TEXT")
    private String texto;

    @Column(name = "imagen_url", length = 700)
    private String imagenUrl;

    @Column(name = "video_url", length = 700)
    private String videoUrl;

    @Column(name = "url_destino", length = 700)
    private String urlDestino;

    @Column(length = 9)
    private String estado; // 'BORRADOR','ACTIVO','INACTIVO','EXPIRADO'

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}