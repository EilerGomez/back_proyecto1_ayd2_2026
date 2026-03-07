/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadAnuncio;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import lombok.Value;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class AnuncioResponse {
    int id;
    String texto;
    String imagenUrl;
    String videoUrl;
    String urlDestino;
    String estado;
    LocalDateTime fechaCreacion;
    UsuarioResponse anunciante;
    TipoAnuncioResponse tipoAnuncio;

    public AnuncioResponse(EntidadAnuncio a, UsuarioResponse ur, TipoAnuncioResponse tr) {
        this.id = a.getId();
        this.texto = a.getTexto();
        this.imagenUrl = a.getImagenUrl();
        this.videoUrl = a.getVideoUrl();
        this.urlDestino = a.getUrlDestino();
        this.estado = a.getEstado();
        this.fechaCreacion = a.getFechaCreacion();
        this.anunciante = ur;
        this.tipoAnuncio = tr;
    }
}
