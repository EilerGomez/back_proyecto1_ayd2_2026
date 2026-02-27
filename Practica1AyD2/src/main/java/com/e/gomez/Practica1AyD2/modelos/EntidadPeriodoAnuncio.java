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

@Entity
@Table(name = "periodos_anuncio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EntidadPeriodoAnuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, length = 9, nullable = false)
    private String codigo;

    @Column(nullable = false)
    private Integer dias;
}
