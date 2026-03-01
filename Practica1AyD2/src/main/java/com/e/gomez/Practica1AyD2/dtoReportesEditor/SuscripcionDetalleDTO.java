/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesEditor;

import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author eiler
 */
@Data
@AllArgsConstructor
public class SuscripcionDetalleDTO {
    private String username;
    private LocalDate fechaSuscripcion;
    private boolean activa;
    public SuscripcionDetalleDTO(EntidadUsuario eu, EntidadSuscripcion es){
        this.username=eu.getUsername();
        this.fechaSuscripcion=es.getFechaSuscripcion();
        this.activa=es.isActiva();
    }
}