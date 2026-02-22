/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoSuscripciones;

import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
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
public class SuscricpionResponseByRevistaId {
    private int id;    
    private Integer revistaId;

    private UsuarioResponse usuario;

    private LocalDate fechaSuscripcion;

    private boolean activa;
    public SuscricpionResponseByRevistaId(EntidadSuscripcion es, UsuarioResponse u){
        this.id=es.getId();
        this.revistaId=es.getRevistaId();
        this.usuario=u;
        this.fechaSuscripcion=es.getFechaSuscripcion();
        this.activa=es.isActiva();
    }
    
}
