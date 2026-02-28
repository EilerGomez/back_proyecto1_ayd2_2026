/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */
import com.e.gomez.Practica1AyD2.modelos.EntidadLike;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface LikeRepositorio extends JpaRepository<EntidadLike, Integer> {
    
    List<EntidadLike> findByRevistaId(Integer revistaId);
    
    List<EntidadLike> findByUsuarioId(Integer usuarioId);
    
    boolean existsByRevistaIdAndUsuarioId(Integer revistaId, Integer usuarioId);

    // Para "quitar like" directamente
    @Transactional
    void deleteByRevistaIdAndUsuarioId(Integer revistaId, Integer usuarioId);
    
    // Contar likes de una revista
    Integer countByRevistaId(Integer revistaId);

    public EntidadLike findByRevistaIdAndUsuarioId(Integer revistaId, Integer usuarioId);
    @Query("SELECT l FROM EntidadLike l " +
       "JOIN EntidadRevista r ON l.revistaId = r.id " + // Unimos manualmente
       "WHERE r.editorId = :editorId " +
       "AND (:revistaId IS NULL OR l.revistaId = :revistaId) " +
       "AND (:inicio IS NULL OR l.fechaCreacion >= :inicio) " +
       "AND (:fin IS NULL OR l.fechaCreacion <= :fin)")
    List<EntidadLike> buscarLikesReporte(
        @Param("editorId") Integer editorId, 
        @Param("revistaId") Integer revistaId, 
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin);
    }
