/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.ImpresionAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.ImpresionAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

public interface ImpresionAnuncioService {
    void registrarImpresion(ImpresionAnuncioRequest req) throws ExcepcionNoExiste;
    long obtenerTotalVistasPorAnuncio(Integer anuncioId);
    List<ImpresionAnuncioResponse> listarPorRevista(Integer revistaId);
}