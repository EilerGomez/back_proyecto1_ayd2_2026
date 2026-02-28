/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadImpresionAnuncio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImpresionAnuncioRepositorio extends JpaRepository<EntidadImpresionAnuncio, Long> {
    // Contar cuántas veces se ha visto un anuncio específico
    long countByAnuncioId(Integer anuncioId);
    
    // Listar impresiones por revista para reportes
    List<EntidadImpresionAnuncio> findByRevistaId(Integer revistaId);
}
