/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadAnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnuncioRepositorio extends JpaRepository<EntidadAnuncio, Integer> {
    List<EntidadAnuncio> findByEstado(String estado);
    List<EntidadAnuncio> findByAnuncianteId(Integer anuncianteId);
    List<EntidadAnuncio> findByAnuncianteIdAndEstado(Integer anuncianteId, String estado);
}
