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
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
}
