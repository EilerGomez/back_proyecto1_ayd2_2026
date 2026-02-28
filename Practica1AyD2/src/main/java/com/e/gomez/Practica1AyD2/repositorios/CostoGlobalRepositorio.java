/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadCostoGlobal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface CostoGlobalRepositorio extends JpaRepository<EntidadCostoGlobal, Integer> {
    
    // Método para obtener siempre el primer registro (el global)
    @Query("SELECT c FROM EntidadCostoGlobal c ORDER BY c.id ASC LIMIT 1")
    Optional<EntidadCostoGlobal> findGlobalConfig();
}
