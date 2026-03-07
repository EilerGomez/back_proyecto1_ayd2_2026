/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadTipoAnuncio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TipoAnuncioResponse {
    int id;
    String codigo;
    String descripcion;

    public TipoAnuncioResponse(EntidadTipoAnuncio t) {
        this.id = t.getId();
        this.codigo = t.getCodigo();
        this.descripcion = t.getDescripcion();
    }
}
