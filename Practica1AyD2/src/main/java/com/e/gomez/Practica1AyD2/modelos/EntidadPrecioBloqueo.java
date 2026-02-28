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

@Entity
@Table(name = "precio_bloqueo_anuncio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class EntidadPrecioBloqueo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "revista_id", unique = true)
    private Integer revistaId;

    @Column(name = "costo_por_dia", precision = 12, scale = 2)
    private BigDecimal costoPorDia;

    @Column(name = "admin_id")
    private Integer adminId;
}
