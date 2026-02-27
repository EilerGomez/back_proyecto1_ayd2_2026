/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoTransacciones;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author eiler
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class TransaccionRequest {
    private Integer carteraId;
    private String tipo;
    private String direccion; // CREDITO o DEBITO
    private BigDecimal monto;
    private String referenciaTipo;
    private int referenciaId;
    private String nota;
    private LocalDate fechaRegistradaUsuario;
}
