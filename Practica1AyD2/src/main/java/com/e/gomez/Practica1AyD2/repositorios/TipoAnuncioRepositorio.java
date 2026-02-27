/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadTipoAnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoAnuncioRepositorio extends JpaRepository<EntidadTipoAnuncio, Integer> {
    Optional<EntidadTipoAnuncio> findByCodigo(String codigo);
}
