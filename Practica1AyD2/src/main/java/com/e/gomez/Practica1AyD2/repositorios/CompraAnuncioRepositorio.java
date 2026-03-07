/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

import com.e.gomez.Practica1AyD2.dtoReportesAdmin.AnuncioCompradoDetalleDTO;
import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.AnuncioCompradoDetalleDTO;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadAnuncio;      // 
import com.e.gomez.Practica1AyD2.modelos.EntidadTipoAnuncio;  // 
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;      //
import com.e.gomez.Practica1AyD2.modelos.EntidadPrecioAnuncio;// 
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Modifying;

/**
 *
 * @author eiler
 */
public interface CompraAnuncioRepositorio extends JpaRepository<EntidadCompraAnuncio, Integer> {
    List<EntidadCompraAnuncio> findByEstado(String estado);
    List<EntidadCompraAnuncio> findByDesactivadoPor(String desactivadoPor);
    List<EntidadCompraAnuncio> findByAnuncianteId(Integer anuncianteId) throws ExcepcionNoExiste;
    List<EntidadCompraAnuncio> findByAnuncioId(Integer anuncioId) throws ExcepcionNoExiste;
    
    @Query("SELECT new com.e.gomez.Practica1AyD2.dtoReportesAdmin.AnuncioCompradoDetalleDTO(" +
               "ca.anuncioId, t.codigo, u.username, pr.precio, ca.fechaInicio) " +
               "FROM EntidadCompraAnuncio ca, " +
               "com.e.gomez.Practica1AyD2.modelos.EntidadAnuncio a, " +
               "com.e.gomez.Practica1AyD2.modelos.EntidadTipoAnuncio t, " +
               "com.e.gomez.Practica1AyD2.modelos.EntidadUsuario u, " +
               "com.e.gomez.Practica1AyD2.modelos.EntidadPrecioAnuncio pr " +
               "WHERE ca.anuncioId = a.id " +
               "AND a.tipoAnuncioId = t.id " +
               "AND ca.anuncianteId = u.id " +
               "AND ca.precioId = pr.id " +
               "AND (:inicio IS NULL OR ca.fechaInicio >= :inicio) " +
               "AND (:fin IS NULL OR ca.fechaInicio <= :fin)")
        List<AnuncioCompradoDetalleDTO> listarAnunciosComprados(
            @Param("inicio") LocalDateTime inicio, 
            @Param("fin") LocalDateTime fin);
        
        
    
    @Query("SELECT ca FROM EntidadCompraAnuncio ca, EntidadAnuncio a, EntidadTipoAnuncio t " +
           "WHERE ca.anuncioId = a.id " +
           "AND a.tipoAnuncioId = t.id " +
           "AND (:tipo IS NULL OR t.codigo = :tipo) " +
           "AND (:inicio IS NULL OR ca.fechaInicio >= :inicio) " +
           "AND (:fin IS NULL OR ca.fechaInicio <= :fin) " +
           "ORDER BY ca.fechaInicio DESC")
    List<EntidadCompraAnuncio> buscarComprasConFiltros(
        @Param("tipo") String tipo, 
        @Param("inicio") LocalDateTime inicio, 
        @Param("fin") LocalDateTime fin);
    
    @Modifying
    @Query("UPDATE EntidadCompraAnuncio c SET c.estado = 'EXPIRADO', c.desactivadoPor = 'SISTEMA' " +
           "WHERE c.estado = 'ACTIVO' AND c.fechaFin < :ahora")
    int expirarComprasVencidas(@Param("ahora") LocalDateTime ahora);

}
