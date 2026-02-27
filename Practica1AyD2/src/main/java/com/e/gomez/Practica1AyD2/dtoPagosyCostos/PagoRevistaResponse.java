/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoPagosyCostos;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadPagoRevista;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Value;

@Value
public class PagoRevistaResponse {
    int id;
    int revistaId;
    int editorId;
    BigDecimal monto;
    LocalDate fechaPago;
    LocalDate periodoInicio;
    LocalDate periodoFin;
    int transaccionId;

    public PagoRevistaResponse(EntidadPagoRevista p) {
        this.id = p.getId();
        this.revistaId = p.getRevistaId();
        this.editorId = p.getEditorId();
        this.monto = p.getMonto();
        this.fechaPago = p.getFechaPago();
        this.periodoInicio = p.getPeriodoInicio();
        this.periodoFin = p.getPeriodoFin();
        this.transaccionId = p.getTransaccionId();
    }
}
