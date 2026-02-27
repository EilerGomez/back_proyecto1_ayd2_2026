/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.TipoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

/**
 *
 * @author eiler
 */
public interface TipoAnuncioService {
    List<TipoAnuncioResponse> listarTodos();
    TipoAnuncioResponse obtenerPorCodigo(String codigo) throws ExcepcionNoExiste;
    TipoAnuncioResponse obtenerPorId(Integer id) throws ExcepcionNoExiste;
}
