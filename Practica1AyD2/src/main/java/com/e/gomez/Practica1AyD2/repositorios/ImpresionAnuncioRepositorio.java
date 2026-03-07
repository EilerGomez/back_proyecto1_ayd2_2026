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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImpresionAnuncioRepositorio extends JpaRepository<EntidadImpresionAnuncio, Long> {
    // Contar cuántas veces se ha visto un anuncio específico
    long countByAnuncioId(Integer anuncioId);
    
    // Listar impresiones por revista para reportes
    List<EntidadImpresionAnuncio> findByRevistaId(Integer revistaId);
    
    @Query("SELECT COALESCE(SUM(pr.precio), 0) FROM EntidadImpresionAnuncio i " +
       "JOIN EntidadCompraAnuncio ca ON i.compraId = ca.id " +
       "JOIN EntidadPrecioAnuncio pr ON ca.precioId = pr.id " +
       "WHERE i.revistaId = :revistaId " +
       "AND (:inicio IS NULL OR i.fechaMostrado >= :inicio) " +
       "AND (:fin IS NULL OR i.fechaMostrado <= :fin)")
    BigDecimal sumIngresosAnunciosPorRevista(
        @Param("revistaId") Integer revistaId, 
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin);
    
    
    @Query("SELECT i.anuncioId, i.revistaId, i.urlPagina, COUNT(i.id) " +
       "FROM EntidadImpresionAnuncio i " +
       "WHERE (:inicio IS NULL OR i.fechaMostrado >= :inicio) " +
       "AND (:fin IS NULL OR i.fechaMostrado <= :fin) " +
       "GROUP BY i.anuncioId, i.revistaId,i.urlPagina")
    List<Object[]> contarVistasPorAnuncioYRevista(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin);
}
