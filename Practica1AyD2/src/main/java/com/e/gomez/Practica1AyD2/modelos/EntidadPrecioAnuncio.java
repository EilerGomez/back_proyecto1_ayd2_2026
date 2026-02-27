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
@Table(name = "precios_anuncio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class EntidadPrecioAnuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_anuncio_id")
    private Integer tipoAnuncioId;

    @Column(name = "periodo_id")
    private Integer periodoId;

    @Column(precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(name = "admin_id")
    private Integer adminId;

    private boolean activo;


}
