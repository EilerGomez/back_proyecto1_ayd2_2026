/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author eiler
 */
public interface CompraAnuncioRepositorio extends JpaRepository<EntidadCompraAnuncio, Integer> {
    List<EntidadCompraAnuncio> findByEstado(String estado);
    List<EntidadCompraAnuncio> findByDesactivadoPor(String desactivadoPor);
    
}
