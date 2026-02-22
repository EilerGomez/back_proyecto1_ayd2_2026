/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoComentarios.ComentarioRequest;
import com.e.gomez.Practica1AyD2.dtoComentarios.ComentarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

/**
 *
 * @author eiler
 */
public interface ComentarioService {
    ComentarioResponse crear(ComentarioRequest req) throws ExcepcionNoExiste;
    ComentarioResponse actualizar(Integer id, String nuevoContenido) throws ExcepcionNoExiste;
    void eliminar(Integer id) throws ExcepcionNoExiste;
    List<ComentarioResponse> listarPorRevista(Integer revistaId) throws ExcepcionNoExiste;
    List<ComentarioResponse> listarPorUsuario(Integer usuarioId) throws ExcepcionNoExiste;
}
