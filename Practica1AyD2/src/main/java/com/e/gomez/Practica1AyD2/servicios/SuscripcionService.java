/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscricpionResponseByRevistaId;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscripcionRequest;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.dtoRevistasPorSuscripcionByUsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
import java.util.List;

/**
 *
 * @author eiler
 */
public interface SuscripcionService {
    SuscricpionResponseByRevistaId suscribir(SuscripcionRequest sr) throws ExcepcionEntidadDuplicada,ExcepcionNoExiste;
    void cancelarSuscripcion(Integer id);
    void cambiarEstado(Integer id, boolean activa) throws ExcepcionNoExiste;
    List<dtoRevistasPorSuscripcionByUsuarioResponse> listarPorUsuario(Integer usuarioId) throws ExcepcionNoExiste;
    List<SuscricpionResponseByRevistaId> listarPorRevista(Integer revistaId) throws ExcepcionNoExiste;
}