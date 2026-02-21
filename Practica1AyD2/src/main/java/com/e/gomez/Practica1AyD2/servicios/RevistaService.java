/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoEtiquetas.RevistaEtiquetasRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

public interface RevistaService {
    // CRUD Básico
    RevistaResponse crear(RevistaRequest req) throws ExcepcionEntidadDuplicada;
    RevistaResponse actualizar(Integer id, RevistaRequest req) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste;
    void eliminar(Integer id) throws ExcepcionNoExiste;
    
    // Consultas
    RevistaResponse getById(Integer id) throws ExcepcionNoExiste;
    List<RevistaResponse> findAll();
    List<RevistaResponse> findByActivas();
    List<RevistaResponse> findByEditorId(Integer editorId);
    List<RevistaResponse> findByCategoriaId(Integer categoriaId);
    
    void guardarEtiquetas(RevistaEtiquetasRequest req) throws ExcepcionNoExiste;
}
