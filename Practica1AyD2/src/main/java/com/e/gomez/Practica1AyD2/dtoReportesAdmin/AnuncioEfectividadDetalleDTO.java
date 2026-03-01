/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesAdmin;

import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author eiler
 */
@Data
@AllArgsConstructor
public class AnuncioEfectividadDetalleDTO {
    private Integer anuncioId;
    private String textoAnuncio;
    private Long cantidadVistas;
    private String url;
    private RevistaResponse revistaDondeSeMostro;
}