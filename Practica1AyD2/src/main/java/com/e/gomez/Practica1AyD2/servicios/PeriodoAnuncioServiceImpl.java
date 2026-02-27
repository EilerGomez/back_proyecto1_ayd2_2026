/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.PeriodoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPeriodoAnuncio;
import com.e.gomez.Practica1AyD2.repositorios.PeriodoAnuncioRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PeriodoAnuncioServiceImpl implements PeriodoAnuncioService {

    private final PeriodoAnuncioRepositorio repo;

    public PeriodoAnuncioServiceImpl(PeriodoAnuncioRepositorio repo) {
        this.repo = repo;
    }

    @Override
    public List<PeriodoAnuncioResponse> listarTodos() {
        return repo.findAll().stream()
                .map(PeriodoAnuncioResponse::new)
                .toList();
    }

    @Override
    public PeriodoAnuncioResponse obtenerPorCodigo(String codigo) throws ExcepcionNoExiste {
        return repo.findByCodigo(codigo)
                .map(PeriodoAnuncioResponse::new)
                .orElseThrow(() -> new ExcepcionNoExiste("Periodo no encontrado: " + codigo));
    }

    @Override
    public PeriodoAnuncioResponse obtenerPorId(Integer id) throws ExcepcionNoExiste {
        return repo.findById(id)
                .map(PeriodoAnuncioResponse::new)
                .orElseThrow(() -> new ExcepcionNoExiste("ID de periodo no válido: " + id));
    }
}
