/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author eiler
 */
@Entity
@Table(name = "revistas")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EntidadRevista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "editor_id")
    private Integer editorId;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "activa")
    private boolean activa = true;

    @Column(name = "permite_comentarios")
    private boolean permiteComentarios = true;

    @Column(name = "permite_likes")
    private boolean permiteLikes = true;

    @Column(name = "permite_suscripciones")
    private boolean permiteSuscripciones = true;
}