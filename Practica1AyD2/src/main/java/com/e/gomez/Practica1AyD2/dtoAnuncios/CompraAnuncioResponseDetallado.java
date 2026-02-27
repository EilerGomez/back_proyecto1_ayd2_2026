/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import java.time.LocalDateTime;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class CompraAnuncioResponseDetallado {
    int id;
    LocalDateTime fechaInicio;
    LocalDateTime fechaFin;
    String estado;
    UsuarioResponse anunciante;
    AnuncioResponse anuncio; // Objeto completo
    PrecioAnuncioResponse precio;
    int transaccionId;
    
    public CompraAnuncioResponseDetallado(EntidadCompraAnuncio eca,
    UsuarioResponse anunciante,
    AnuncioResponse anuncio, // Objeto completo
    PrecioAnuncioResponse precio){
        this.id=eca.getId();
        this.fechaInicio=eca.getFechaInicio();
        this.fechaFin=eca.getFechaFin();
        this.estado=eca.getEstado();
        this.anunciante=anunciante;
        this.anuncio=anuncio;
        this.precio=precio;
        this.transaccionId=eca.getTransaccionId();
    }
}
