/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoEdicion;

import com.e.gomez.Practica1AyD2.modelos.EntidadEdicion;
import java.time.LocalDateTime;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class EdicionResponse {
    int id;
    int revistaId;
    String numeroEdicion;
    String titulo;
    String pdfUrl;
    LocalDateTime fechaPublicacion;

    public EdicionResponse(EntidadEdicion ee) {
        this.id = ee.getId();
        this.revistaId = ee.getRevistaId();
        this.numeroEdicion = ee.getNumeroEdicion();
        this.titulo = ee.getTitulo();
        this.pdfUrl = ee.getPdfUrl();
        this.fechaPublicacion = ee.getFechaPublicacion();
    }
}
