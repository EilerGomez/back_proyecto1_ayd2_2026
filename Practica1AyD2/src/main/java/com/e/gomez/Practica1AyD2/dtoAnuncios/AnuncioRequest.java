/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author eiler
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class AnuncioRequest {
    private Integer anuncianteId;
    private Integer tipoAnuncioId;
    private String texto;
    private String imagenUrl;
    private String videoUrl;
    private String urlDestino;
    private String estado;
}
