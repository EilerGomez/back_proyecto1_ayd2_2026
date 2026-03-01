/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadBloqueoAnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BloqueoAnuncioRepositorio extends JpaRepository<EntidadBloqueoAnuncio, Integer> {
    List<EntidadBloqueoAnuncio> findByRevistaIdAndEstado(Integer revistaId, String estado);
    List<EntidadBloqueoAnuncio> findByEditorId(Integer editorId);

     List<EntidadBloqueoAnuncio> findByRevistaId(Integer revistaId);
    Optional<EntidadBloqueoAnuncio> findFirstByRevistaIdAndEstado(Integer revistaId, String estado);
}
