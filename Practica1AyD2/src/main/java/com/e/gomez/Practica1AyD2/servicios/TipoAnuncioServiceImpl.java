/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.TipoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadTipoAnuncio;
import com.e.gomez.Practica1AyD2.repositorios.TipoAnuncioRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TipoAnuncioServiceImpl implements TipoAnuncioService {

    private final TipoAnuncioRepositorio repo;

    public TipoAnuncioServiceImpl(TipoAnuncioRepositorio repo) {
        this.repo = repo;
    }

    @Override
    public List<TipoAnuncioResponse> listarTodos() {
        return repo.findAll().stream()
                .map(TipoAnuncioResponse::new)
                .toList();
    }

    @Override
    public TipoAnuncioResponse obtenerPorCodigo(String codigo) throws ExcepcionNoExiste {
        return repo.findByCodigo(codigo)
                .map(TipoAnuncioResponse::new)
                .orElseThrow(() -> new ExcepcionNoExiste("Tipo de anuncio no encontrado: " + codigo));
    }

    @Override
    public TipoAnuncioResponse obtenerPorId(Integer id) throws ExcepcionNoExiste {
        return repo.findById(id)
                .map(TipoAnuncioResponse::new)
                .orElseThrow(() -> new ExcepcionNoExiste("ID de tipo de anuncio no válido: " + id));
    }
}
