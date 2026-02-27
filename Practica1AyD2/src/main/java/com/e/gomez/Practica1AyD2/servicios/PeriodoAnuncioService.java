/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.PeriodoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

public interface PeriodoAnuncioService {
    List<PeriodoAnuncioResponse> listarTodos();
    PeriodoAnuncioResponse obtenerPorCodigo(String codigo) throws ExcepcionNoExiste;
    PeriodoAnuncioResponse obtenerPorId(Integer id) throws ExcepcionNoExiste;
}