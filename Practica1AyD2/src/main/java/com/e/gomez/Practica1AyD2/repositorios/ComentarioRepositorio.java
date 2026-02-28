/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */
import com.e.gomez.Practica1AyD2.modelos.EntidadComentario;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ComentarioRepositorio extends JpaRepository<EntidadComentario, Integer> {
    
    // Buscar comentarios de una revista ordenados por fecha (más recientes primero)
    List<EntidadComentario> findByRevistaIdOrderByFechaCreacionDesc(Integer revistaId);
    
    // Buscar todos los comentarios que ha hecho un usuario
    List<EntidadComentario> findByUsuarioIdOrderByFechaCreacionDesc(Integer usuarioId);
    
    // Contar cuántos comentarios tiene una revista
    int countByRevistaId(Integer revistaId);
    
    @Query("SELECT c FROM EntidadComentario c " +
       "JOIN EntidadRevista r ON c.revistaId = r.id " + // Unimos manualmente por ID
       "WHERE r.editorId = :editorId " +
       "AND (:revistaId IS NULL OR c.revistaId = :revistaId) " +
       "AND (:inicio IS NULL OR c.fechaCreacion >= :inicio) " +
       "AND (:fin IS NULL OR c.fechaCreacion <= :fin)")
    List<EntidadComentario> buscarComentariosReporte(
        @Param("editorId") Integer editorId, 
        @Param("revistaId") Integer revistaId, 
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin);
}
