/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoSuscripciones;

import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
import java.time.LocalDate;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class dtoRevistasPorSuscripcionByUsuarioResponse {
    private int id;    
    private RevistaResponse revista;

    private Integer usuarioId;

    private LocalDate fechaSuscripcion;

    private boolean activa;
    public dtoRevistasPorSuscripcionByUsuarioResponse(EntidadSuscripcion es, RevistaResponse rr){
        this.id=es.getId();
        this.revista=rr;
        this.usuarioId=es.getUsuarioId();
        this.fechaSuscripcion=es.getFechaSuscripcion();
        this.activa=es.isActiva();
    }
}
