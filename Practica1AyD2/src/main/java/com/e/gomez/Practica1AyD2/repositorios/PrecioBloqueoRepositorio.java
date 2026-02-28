/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadPrecioBloqueo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PrecioBloqueoRepositorio extends JpaRepository<EntidadPrecioBloqueo, Integer> {
    Optional<EntidadPrecioBloqueo> findByRevistaId(Integer revistaId);
    boolean existsByRevistaId(Integer revistaId);
}
