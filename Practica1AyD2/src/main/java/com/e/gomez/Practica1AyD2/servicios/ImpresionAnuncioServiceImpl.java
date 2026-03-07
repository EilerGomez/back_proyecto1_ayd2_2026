/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.ImpresionAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.ImpresionAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadImpresionAnuncio;
import com.e.gomez.Practica1AyD2.repositorios.CompraAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.ImpresionAnuncioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImpresionAnuncioServiceImpl implements ImpresionAnuncioService {

    private final ImpresionAnuncioRepositorio repo;
    private final CompraAnuncioRepositorio compraService;

    public ImpresionAnuncioServiceImpl(ImpresionAnuncioRepositorio repo, CompraAnuncioRepositorio compraService) {
        this.repo = repo;
        this.compraService=compraService;
    }

    @Override
    @Transactional
    public void registrarImpresion(ImpresionAnuncioRequest req)throws ExcepcionNoExiste {
        List<EntidadCompraAnuncio> compra = compraService.findByAnuncioId(req.getAnuncioId());
        EntidadCompraAnuncio compraActiva = compra.stream()
                .filter(c -> "ACTIVO".equals(c.getEstado()))
                .findFirst() 
                .orElse(null); 
        EntidadImpresionAnuncio impresion = EntidadImpresionAnuncio.builder()
                .anuncioId(req.getAnuncioId())
                .compraId(compraActiva.getId())
                .revistaId(req.getRevistaId())
                .urlPagina(req.getUrlPagina())
                .fechaMostrado(LocalDateTime.now())
                .build();
        repo.save(impresion);
    }

    @Override
    public long obtenerTotalVistasPorAnuncio(Integer anuncioId) {
        return repo.countByAnuncioId(anuncioId);
    }

    @Override
    public List<ImpresionAnuncioResponse> listarPorRevista(Integer revistaId) {
        return repo.findByRevistaId(revistaId).stream()
                .map(ImpresionAnuncioResponse::new)
                .toList();
    }
}