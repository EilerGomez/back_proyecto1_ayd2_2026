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
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueos_anuncio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class EntidadBloqueoAnuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "revista_id")
    private Integer revistaId;

    @Column(name = "editor_id")
    private Integer editorId;

    private Integer dias;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    private BigDecimal monto;

    @Column(length = 9)
    private String estado; // 'ACTIVO','INACTIVO','EXPIRADO'

    @Column(name = "transaccion_id")
    private Integer transaccionId;
}