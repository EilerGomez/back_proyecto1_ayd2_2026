/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesAdmin;

import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoResponse;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author eiler
 */
@Data
@AllArgsConstructor
public class ReporteGananciaRevistaDTO {
    private Integer revistaId;
    private String nombreRevista;
    private BigDecimal ingresosPagosEditor; // total de ingresos por pagos de editor

    List<HistorialCostoResponse> costos;
}
