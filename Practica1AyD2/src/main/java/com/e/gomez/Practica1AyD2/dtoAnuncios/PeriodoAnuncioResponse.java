/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadPeriodoAnuncio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PeriodoAnuncioResponse {
    int id;
    String codigo;
    int dias;

    public PeriodoAnuncioResponse(EntidadPeriodoAnuncio p) {
        this.id = p.getId();
        this.codigo = p.getCodigo();
        this.dias = p.getDias();
    }
}
