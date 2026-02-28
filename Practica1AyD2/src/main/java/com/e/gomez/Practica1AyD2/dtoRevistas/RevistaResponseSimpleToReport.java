/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoRevistas;

import com.e.gomez.Practica1AyD2.dtoCategorias.CategoriaResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.servicios.CategoriaService;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class RevistaResponseSimpleToReport {
    int id;
    String titulo;
    String descripcion;
    String categoria;

    
    public RevistaResponseSimpleToReport(EntidadRevista er, String categoria){
        this.id=er.getId();
        this.descripcion=er.getDescripcion();
        this.titulo=er.getTitulo();
        this.categoria=categoria;
        
    }
}
