/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

/**
 *
 * @author eiler
 */
public interface PrecioAnuncioService {
    PrecioAnuncioResponse crear(PrecioAnuncioRequest req) throws ExcepcionNoExiste;
    List<PrecioAnuncioResponse> listarTodos();
    PrecioAnuncioResponse obtenerPorId(Integer id) throws ExcepcionNoExiste;
    List<PrecioAnuncioResponse > obtenerPorTipoAnuncio(Integer anuncioId) throws ExcepcionNoExiste;
    void desactivar(Integer id) throws ExcepcionNoExiste;
}