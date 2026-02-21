/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadEdicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EdicionRepositorio extends JpaRepository<EntidadEdicion, Integer> {
    List<EntidadEdicion> findByRevistaIdOrderByFechaPublicacionDesc(Integer revistaId);
    long countByRevistaId(Integer revistaId);
}
