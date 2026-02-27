/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadHistorialCosto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author eiler
 */
public interface HistorialCostoRepositorio extends JpaRepository<EntidadHistorialCosto, Integer> {
    List<EntidadHistorialCosto> findByRevistaId(Integer revistaId) throws ExcepcionNoExiste;
    // Buscar el costo vigente (donde fechaFin es null)
    Optional<EntidadHistorialCosto> findByRevistaIdAndFechaFinIsNull(Integer revistaId) throws ExcepcionNoExiste;
}
