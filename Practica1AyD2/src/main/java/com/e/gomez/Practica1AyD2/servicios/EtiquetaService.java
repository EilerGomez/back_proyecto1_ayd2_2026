/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaRequest;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

public interface EtiquetaService {
    List<EtiquetaResponse> findAll();
    EtiquetaResponse getById(Integer id) throws ExcepcionNoExiste;
    EtiquetaResponse crear(EtiquetaRequest request) throws ExcepcionEntidadDuplicada;
    EtiquetaResponse actualizar(Integer id, EtiquetaRequest request) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste;
    void eliminar(Integer id) throws ExcepcionNoExiste;
}