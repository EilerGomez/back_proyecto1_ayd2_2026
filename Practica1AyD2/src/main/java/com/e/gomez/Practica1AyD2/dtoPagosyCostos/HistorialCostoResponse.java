/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoPagosyCostos;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadHistorialCosto;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HistorialCostoResponse {
    int id;
    int revistaId;
    int adminId;
    BigDecimal costoPorDia;
    LocalDate fechaInicio;
    LocalDate fechaFin;

    public HistorialCostoResponse(EntidadHistorialCosto h) {
        this.id = h.getId();
        this.revistaId = h.getRevistaId();
        this.adminId = h.getAdminId();
        this.costoPorDia = h.getCostoPorDia();
        this.fechaInicio = h.getFechaInicio();
        this.fechaFin = h.getFechaFin();
    }
}
