/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface SuscripcionRepositorio extends JpaRepository<EntidadSuscripcion, Integer> {
    
    // Buscar todas las suscripciones de una revista
    List<EntidadSuscripcion> findByRevistaId(Integer revistaId);
    
    // Buscar todas las suscripciones de un usuario
    List<EntidadSuscripcion> findByUsuarioId(Integer usuarioId);
    
    // Buscar suscripción específica (para editar o validar UNIQUE)
    Optional<EntidadSuscripcion> findByRevistaIdAndUsuarioId(Integer revistaId, Integer usuarioId) throws ExcepcionNoExiste;
    
    // Verificar si ya está suscrito
    boolean existsByRevistaIdAndUsuarioId(Integer revistaId, Integer usuarioId);
    
    Integer countByRevistaId(Integer revistaId);
    
    @Query("SELECT s FROM EntidadSuscripcion s " +
       "JOIN EntidadRevista r ON s.revistaId = r.id " +
       "WHERE r.editorId = :editorId " +
       "AND (:revistaId IS NULL OR s.revistaId = :revistaId) " +
       "AND (:inicio IS NULL OR s.fechaSuscripcion >= :inicio) " +
       "AND (:fin IS NULL OR s.fechaSuscripcion <= :fin)")
    List<EntidadSuscripcion> buscarSuscripcionesReporte(
        @Param("editorId") Integer editorId, 
        @Param("revistaId") Integer revistaId, 
        @Param("inicio") LocalDate inicio, 
        @Param("fin") LocalDate fin);
}
