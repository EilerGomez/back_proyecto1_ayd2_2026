/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

import com.e.gomez.Practica1AyD2.modelos.EntidadImpresionAnuncio;
import java.time.LocalDateTime;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class ImpresionAnuncioResponse {
    Integer id;
    Integer anuncioId;
    LocalDateTime fechaMostrado;
    String urlPagina;
    Integer revistaId;

    public ImpresionAnuncioResponse(EntidadImpresionAnuncio e) {
        this.id = e.getId();
        this.anuncioId = e.getAnuncioId();
        this.fechaMostrado = e.getFechaMostrado();
        this.urlPagina = e.getUrlPagina();
        this.revistaId = e.getRevistaId();
    }
}
