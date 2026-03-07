/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.CompraAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.CompraAnuncioResponseDetallado;
import com.e.gomez.Practica1AyD2.dtoAnuncios.CompraAnuncioResponseSimple;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author eiler
 */
public interface CompraAnuncioService {
    CompraAnuncioResponseSimple comprar(CompraAnuncioRequest req) throws ExcepcionNoExiste;
    List<CompraAnuncioResponseDetallado> listarPorEstado(String estado);
    List<CompraAnuncioResponseDetallado> listarPorDesactivadoPor(String desactivadoPor);
    void desactivarManualmente(Integer compraId, String responsable, LocalDateTime fecha) throws ExcepcionNoExiste;
    
    List<CompraAnuncioResponseDetallado> listarPorAnunciante(Integer anuncianteId)throws ExcepcionNoExiste;
    
    CompraAnuncioResponseSimple CambiarFechaFin(Integer idCompra, LocalDateTime fechaFin) throws ExcepcionNoExiste;

}