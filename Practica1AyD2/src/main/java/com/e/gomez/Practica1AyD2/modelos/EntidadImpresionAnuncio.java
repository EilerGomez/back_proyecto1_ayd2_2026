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
@Table(name = "impresiones_anuncio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Builder
public class EntidadImpresionAnuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "anuncio_id")
    private Integer anuncioId;

    @Column(name = "compra_id")
    private Integer compraId;

    @Column(name = "fecha_mostrado")
    private LocalDateTime fechaMostrado;

    @Column(name = "url_pagina", length = 700)
    private String urlPagina;

    @Column(name = "revista_id")
    private Integer revistaId;
}