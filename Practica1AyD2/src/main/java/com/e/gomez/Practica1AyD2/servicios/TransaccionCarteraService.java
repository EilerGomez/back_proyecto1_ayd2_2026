/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionRequest;
import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

/**
 *
 * @author eiler
 */
public interface TransaccionCarteraService {
   
    TransaccionResponse registrar(TransaccionRequest req) throws ExcepcionNoExiste;

    List<TransaccionResponse> listarPorCartera(Integer carteraId);

    
    TransaccionResponse obtenerPorId(Integer id) throws ExcepcionNoExiste;
}
