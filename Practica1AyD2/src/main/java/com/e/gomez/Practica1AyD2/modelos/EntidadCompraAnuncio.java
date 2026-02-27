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
@Table(name = "compras_anuncio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class EntidadCompraAnuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "anuncio_id")
    private Integer anuncioId;

    @Column(name = "anunciante_id")
    private Integer anuncianteId;

    @Column(name = "precio_id")
    private Integer precioId;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    @Column(length = 9)
    private String estado; // 'ACTIVO','INACTIVO','EXPIRADO'

    @Column(name = "desactivado_por", length = 11)
    private String desactivadoPor; // 'SISTEMA','ANUNCIANTE','ADMIN'

    @Column(name = "fecha_desactivacion")
    private LocalDateTime fechaDesactivacion;

    @Column(name = "transaccion_id")
    private Integer transaccionId;
}