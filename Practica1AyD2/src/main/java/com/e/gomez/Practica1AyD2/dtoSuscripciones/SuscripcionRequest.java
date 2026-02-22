/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoSuscripciones;

import java.time.LocalDate;
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
public class SuscripcionRequest {
    private Integer revistaId;

    private Integer usuarioId;

    private LocalDate fechaSuscripcion;

    private boolean activa = true;
}
