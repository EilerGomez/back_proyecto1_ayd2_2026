/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesEditor;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author eiler
 */
@Data
@AllArgsConstructor
public class PagoDetalleDTO {
    private BigDecimal monto;
    private LocalDate fechaPago;
    private LocalDate periodoInicio;
    private LocalDate periodoFin;
}
