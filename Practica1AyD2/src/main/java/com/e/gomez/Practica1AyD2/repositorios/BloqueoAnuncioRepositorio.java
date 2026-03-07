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
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BloqueoAnuncioRepositorio extends JpaRepository<EntidadBloqueoAnuncio, Integer> {
    List<EntidadBloqueoAnuncio> findByRevistaIdAndEstado(Integer revistaId, String estado);
    List<EntidadBloqueoAnuncio> findByEditorId(Integer editorId);

     List<EntidadBloqueoAnuncio> findByRevistaId(Integer revistaId);
    Optional<EntidadBloqueoAnuncio> findFirstByRevistaIdAndEstado(Integer revistaId, String estado);
    
    @Modifying
    @Query("UPDATE EntidadBloqueoAnuncio b SET b.estado = 'EXPIRADO' " +
           "WHERE b.estado = 'ACTIVO' AND b.fechaFin < :ahora")
    int desactivarBloqueosExpirados(@Param("ahora") LocalDateTime ahora);
}
