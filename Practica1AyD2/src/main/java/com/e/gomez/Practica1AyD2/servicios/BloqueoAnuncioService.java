/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.BloqueoAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.BloqueoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

public interface BloqueoAnuncioService {
    
    BloqueoAnuncioResponse contratarBloqueo(BloqueoAnuncioRequest req) throws ExcepcionNoExiste;
    
    // Traer todos los bloqueos (historial) de una revista
    List<BloqueoAnuncioResponse> listarPorRevista(Integer revistaId);
    
    // Traer el bloqueo que está actualmente vigente para una revista
    BloqueoAnuncioResponse obtenerActivoPorRevista(Integer revistaId) throws ExcepcionNoExiste;
}
