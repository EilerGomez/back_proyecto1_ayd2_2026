/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesAdmin;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author eiler
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteGananciasMaestroDTO {
    private List<ReporteGananciaRevistaDTO> reporteRevistas; // 
    private List<AnuncioCompradoDetalleDTO> anunciosComprados; 
    
    private BigDecimal granTotalIngresos;
    private BigDecimal granTotalCostos;
    private BigDecimal granTotalGanancia;
}