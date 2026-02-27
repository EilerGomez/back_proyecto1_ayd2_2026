/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import java.time.LocalDateTime;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class CompraAnuncioResponseSimple {
    int id;
    int anuncioId; // Solo el ID
    LocalDateTime fechaInicio;
    LocalDateTime fechaFin;
    String estado;
    String desactivadoPor;
    PrecioAnuncioResponse precio;
    
    public CompraAnuncioResponseSimple(EntidadCompraAnuncio eca, PrecioAnuncioResponse precio){
        this.id=eca.getId();
        this.anuncioId=eca.getAnuncioId();
        this.fechaInicio=eca.getFechaInicio();
        this.fechaFin=eca.getFechaFin();
        this.estado=eca.getEstado();
        this.desactivadoPor=eca.getDesactivadoPor();
        this.precio=precio;
        
    }
}
