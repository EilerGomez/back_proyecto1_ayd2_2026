/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoRequest;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

public interface HistorialCostoService {
    HistorialCostoResponse asignarNuevoCosto(HistorialCostoRequest req) throws ExcepcionNoExiste;
    HistorialCostoResponse obtenerCostoVigente(Integer revistaId) throws ExcepcionNoExiste;
    List<HistorialCostoResponse> obtenerHistorialPorRevista(Integer revistaId) throws ExcepcionNoExiste;
}