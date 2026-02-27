/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoTransacciones;

import com.e.gomez.Practica1AyD2.modelos.EntidadTransaccionCartera;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class TransaccionResponse {
    int id;
    String tipo;
    String direccion;
    BigDecimal monto;
    String nota;
    LocalDateTime fechaCreacion;
    
    public TransaccionResponse(EntidadTransaccionCartera t) {
        this.id = t.getId();
        this.tipo = t.getTipo();
        this.direccion = t.getDireccion();
        this.monto = t.getMonto();
        this.nota = t.getNota();
        this.fechaCreacion = t.getFechaCreacion();
    }
}
