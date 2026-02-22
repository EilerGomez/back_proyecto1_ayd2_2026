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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComentarioRepositorio extends JpaRepository<EntidadComentario, Integer> {
    
    // Buscar comentarios de una revista ordenados por fecha (más recientes primero)
    List<EntidadComentario> findByRevistaIdOrderByFechaCreacionDesc(Integer revistaId);
    
    // Buscar todos los comentarios que ha hecho un usuario
    List<EntidadComentario> findByUsuarioIdOrderByFechaCreacionDesc(Integer usuarioId);
    
    // Contar cuántos comentarios tiene una revista
    int countByRevistaId(Integer revistaId);
}
