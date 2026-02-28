/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioBloqueoRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioBloqueoResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;

/**
 *
 * @author eiler
 */
public interface PrecioBloqueoService {
    PrecioBloqueoResponse asignarOActualizar(PrecioBloqueoRequest req);
    PrecioBloqueoResponse obtenerPorRevista(Integer revistaId) throws ExcepcionNoExiste;
    void eliminarPrecio(Integer id) throws ExcepcionNoExiste;
}