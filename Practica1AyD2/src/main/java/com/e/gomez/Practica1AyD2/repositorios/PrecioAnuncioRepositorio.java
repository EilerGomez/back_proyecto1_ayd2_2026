/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPrecioAnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PrecioAnuncioRepositorio extends JpaRepository<EntidadPrecioAnuncio, Integer> {
    List<EntidadPrecioAnuncio> findByActivoTrue();
List<EntidadPrecioAnuncio> findByTipoAnuncioIdAndActivoTrue(Integer id);
List<EntidadPrecioAnuncio> findByTipoAnuncioId(Integer tipoAnuncioId);

    // Buscar si ya existe un precio para esa combinación específica
    Optional<EntidadPrecioAnuncio> findByTipoAnuncioIdAndPeriodoIdAndActivoTrue(Integer tipoId, Integer periodoId) throws ExcepcionNoExiste;
}
