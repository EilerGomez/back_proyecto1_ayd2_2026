/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadTransaccionCartera;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionCarteraRepositorio extends JpaRepository<EntidadTransaccionCartera, Integer> {

    List<EntidadTransaccionCartera> findByCarteraIdOrderByFechaCreacionDesc(Integer carteraId);

    List<EntidadTransaccionCartera> findByCarteraIdAndFechaCreacionBetweenOrderByFechaCreacionDesc(
            Integer carteraId, LocalDateTime desde, LocalDateTime hasta
    );
}
