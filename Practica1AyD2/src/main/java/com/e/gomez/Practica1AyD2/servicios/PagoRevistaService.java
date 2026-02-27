/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoPagosyCostos.PagoRevistaRequest;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.PagoRevistaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

public interface PagoRevistaService {
    // Se requiere el ID de cartera para realizar el débito
    PagoRevistaResponse procesarPago(PagoRevistaRequest req, Integer carteraId) throws ExcepcionNoExiste;
    List<PagoRevistaResponse> listarPagosPorRevista(Integer revistaId) throws ExcepcionNoExiste;
    List<PagoRevistaResponse> listarPagosPorEditor(Integer editorId) throws ExcepcionNoExiste;
}
