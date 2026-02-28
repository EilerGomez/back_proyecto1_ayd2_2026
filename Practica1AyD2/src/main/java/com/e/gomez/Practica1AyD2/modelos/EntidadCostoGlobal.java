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
@Table(name = "costo_global_revistas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class EntidadCostoGlobal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(precision = 12, scale = 2)
    private BigDecimal monto;
}