/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

import com.e.gomez.Practica1AyD2.modelos.EntidadBloqueoAnuncio;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class BloqueoAnuncioResponse {
    int id;
    int revistaId;
    int dias;
    LocalDateTime fechaInicio;
    LocalDateTime fechaFin;
    BigDecimal monto;
    String estado;
    int transaccionId;

    public BloqueoAnuncioResponse(EntidadBloqueoAnuncio e) {
        this.id = e.getId();
        this.revistaId = e.getRevistaId();
        this.dias = e.getDias();
        this.fechaInicio = e.getFechaInicio();
        this.fechaFin = e.getFechaFin();
        this.monto = e.getMonto();
        this.estado = e.getEstado();
        this.transaccionId = e.getTransaccionId();
    }
}