/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPagoRevista;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author eiler
 */
public interface PagoRevistaRepositorio extends JpaRepository<EntidadPagoRevista, Integer> {
    List<EntidadPagoRevista> findByRevistaId(Integer revistaId) throws ExcepcionNoExiste;
    List<EntidadPagoRevista> findByEditorId(Integer editorId) throws ExcepcionNoExiste;
    @Query("SELECT p FROM EntidadPagoRevista p " +
           "WHERE p.editorId = :editorId " +
           "AND (:revistaId IS NULL OR p.revistaId = :revistaId) " +
           "AND (:inicio IS NULL OR p.fechaPago >= :inicio) " +
           "AND (:fin IS NULL OR p.fechaPago <= :fin) " +
           "ORDER BY p.fechaPago DESC")
    List<EntidadPagoRevista> buscarPagosReporte(
        @Param("editorId") Integer editorId, 
        @Param("revistaId") Integer revistaId, 
        @Param("inicio") LocalDate inicio, 
        @Param("fin") LocalDate fin
    );
    
    
    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM EntidadPagoRevista p " +
       "WHERE p.revistaId = :revistaId " +
       "AND (:inicio IS NULL OR p.fechaPago >= :inicio) " +
       "AND (:fin IS NULL OR p.fechaPago <= :fin)")
    BigDecimal sumMontoByRevista(Integer revistaId, LocalDate inicio, LocalDate fin);
    
    @Query("SELECT p FROM EntidadPagoRevista p " +
           "WHERE p.revistaId = :revistaId " +
           "AND (:inicio IS NULL OR p.fechaPago >= :inicio) " +
           "AND (:fin IS NULL OR p.fechaPago <= :fin) " +
           "ORDER BY p.fechaPago DESC")
    List<EntidadPagoRevista> buscarPorRevistaYPeriodo(
        @Param("revistaId") Integer revistaId, 
        @Param("inicio") LocalDate inicio, 
        @Param("fin") LocalDate fin
    );
}
