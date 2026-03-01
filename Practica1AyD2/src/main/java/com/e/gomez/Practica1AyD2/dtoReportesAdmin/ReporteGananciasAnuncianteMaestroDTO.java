/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesAdmin;

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
public class ReporteGananciasAnuncianteMaestroDTO {
    private List<DetalleAnuncianteDTO> anunciantes;
    private BigDecimal totalGeneralIngresos;
}
