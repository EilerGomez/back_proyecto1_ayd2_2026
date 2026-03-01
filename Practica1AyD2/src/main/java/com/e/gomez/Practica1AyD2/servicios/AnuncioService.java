/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

/**
 *
 * @author eiler
 */
public interface AnuncioService {
    AnuncioResponse crear(AnuncioRequest req) throws ExcepcionNoExiste;
    AnuncioResponse actualizar(Integer id, AnuncioRequest req) throws ExcepcionNoExiste;
    AnuncioResponse obtenerPorId(Integer id) throws ExcepcionNoExiste;
    List<AnuncioResponse> listarTodos();
    List<AnuncioResponse> listarPorEstado(String estado);
    List<AnuncioResponse> listarPorAnunciante(Integer anuncianteId);
    void cambiarEstado(Integer id, String nuevoEstado) throws ExcepcionNoExiste;
    List<AnuncioResponse> obtenerAnunciosParaRevista(Integer revistaId);
}