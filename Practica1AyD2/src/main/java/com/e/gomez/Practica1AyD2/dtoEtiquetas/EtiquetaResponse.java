/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoEtiquetas;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadEtiqueta;
import lombok.Value;

@Value
public class EtiquetaResponse {
    private int id;
    private String nombre;

    public EtiquetaResponse(EntidadEtiqueta ee) {
        this.id = ee.getId();
        this.nombre = ee.getNombre();
    }
}