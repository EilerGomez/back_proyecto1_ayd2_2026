/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionRequest;
import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

/**
 *
 * @author eiler
 */
public interface EdicionService {
    List<EdicionResponse> listarPorRevista(Integer revistaId);
    EdicionResponse crearEdicion(EdicionRequest req);
    EdicionResponse getById(Integer id) throws ExcepcionNoExiste;
    List<EdicionResponse> findAll();
    void eliminar(Integer id);
}
