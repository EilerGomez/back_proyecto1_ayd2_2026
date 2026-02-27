/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author eiler
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class CompraAnuncioRequest {
    private Integer anuncioId;
    private Integer anuncianteId;
    private Integer precioId;
    private Integer carteraId; 
    private LocalDateTime fechaInicio;
}