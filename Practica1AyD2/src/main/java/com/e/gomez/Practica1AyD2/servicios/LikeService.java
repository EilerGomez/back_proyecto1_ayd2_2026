/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoLikes.LikeRequest;
import com.e.gomez.Practica1AyD2.dtoLikes.LikeResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

/**
 *
 * @author eiler
 */
public interface LikeService {
    LikeResponse darLike(LikeRequest req) throws ExcepcionNoExiste, ExcepcionEntidadDuplicada;
    void quitarLike(Integer revistaId, Integer usuarioId);
    int contarLikesRevista(Integer revistaId);
    boolean yaDioLike(Integer revistaId, Integer usuarioId);
    List<LikeResponse> findByRevistaId(Integer revistaId) throws ExcepcionNoExiste;
}
