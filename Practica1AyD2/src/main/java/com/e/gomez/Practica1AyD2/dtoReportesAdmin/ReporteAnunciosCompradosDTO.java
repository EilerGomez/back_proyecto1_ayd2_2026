/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesAdmin;

/**
 *
 * @author eiler
 */
//REPORTE 2 DEL ADMINISTRADOR
import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReporteAnunciosCompradosDTO {
    private AnuncioResponse anuncio; // Reutilizamos tu DTO existente
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private BigDecimal montoPagado;
    private String estadoCompra; // ACTIVO, EXPIRADO, etc.
}
