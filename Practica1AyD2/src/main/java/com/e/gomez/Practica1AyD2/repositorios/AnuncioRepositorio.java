/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadAnuncio;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnuncioRepositorio extends JpaRepository<EntidadAnuncio, Integer> {
    List<EntidadAnuncio> findByEstado(String estado);
    List<EntidadAnuncio> findByAnuncianteId(Integer anuncianteId);
    List<EntidadAnuncio> findByAnuncianteIdAndEstado(Integer anuncianteId, String estado);
    
    
    @Query("SELECT a FROM EntidadAnuncio a " +
       "JOIN EntidadCompraAnuncio ca ON a.id = ca.anuncioId " +
       "WHERE a.estado = 'ACTIVO' " +
       "AND ca.estado = 'COMPRADO' " +
       "AND ca.fechaInicio <= :hoy " +
       "AND ca.fechaFin >= :hoy")
    List<EntidadAnuncio> buscarAnunciosVigentes(@Param("hoy") LocalDateTime hoy);
    
    
}
