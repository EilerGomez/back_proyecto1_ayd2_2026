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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author eiler
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReporteGananciasMaestroDTO {
    private List<ReporteGananciaRevistaDTO> reporteRevistas; // 
        private List<ReporteAnunciosCompradosDTO> anunciosComprados; 

    private BigDecimal costosTotal; //suma de los costos
    private BigDecimal totalIngresos; //de los editores
    private BigDecimal totalGanancias; // anuncios + ganancia de revista
    private BigDecimal totalAnuncios;
}