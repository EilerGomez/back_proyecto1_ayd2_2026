/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadPrecioAnuncio;
import java.math.BigDecimal;
import lombok.Value;

@Value
public class PrecioAnuncioResponse {
    int id;
    BigDecimal precio;
    boolean activo;
    TipoAnuncioResponse tipoAnuncio;
    PeriodoAnuncioResponse periodoAnuncio;
    UsuarioResponse admin;

    public PrecioAnuncioResponse(EntidadPrecioAnuncio p, TipoAnuncioResponse t, PeriodoAnuncioResponse per, UsuarioResponse u) {
        this.id = p.getId();
        this.precio = p.getPrecio();
        this.activo = p.isActivo();
        this.tipoAnuncio = t;
        this.periodoAnuncio = per;
        this.admin=u;
    }
}
