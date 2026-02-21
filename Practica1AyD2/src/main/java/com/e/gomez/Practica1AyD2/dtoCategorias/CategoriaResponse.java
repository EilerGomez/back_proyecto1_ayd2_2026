/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoCategorias;

/**
 *
 * @author eiler
 */
import com.e.gomez.Practica1AyD2.modelos.EntidadCategoria;
import lombok.Value;

@Value
public class CategoriaResponse{
    private int id;
    private String nombre;
    private String descripcion;
    
    public CategoriaResponse(EntidadCategoria ec){
     this.id=ec.getId();
     this.nombre=ec.getNombre();
     this.descripcion=ec.getDescripcion();
    }
}