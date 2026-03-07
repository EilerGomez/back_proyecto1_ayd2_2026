/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionRequest;
import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadEdicion;
import com.e.gomez.Practica1AyD2.repositorios.EdicionRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author eiler
 */
@Service
public class EdicionServiceImpl implements EdicionService {
    private final EdicionRepositorio repo;

    public EdicionServiceImpl(EdicionRepositorio repo) { this.repo = repo; }

    @Override
    public EdicionResponse crearEdicion(EdicionRequest req) {
        long count = repo.countByRevistaId(req.getRevistaId());
        EntidadEdicion ed = new EntidadEdicion();
        ed.setRevistaId(req.getRevistaId());
        ed.setTitulo(req.getTitulo());
        ed.setPdfUrl(req.getPdfUrl());
        ed.setNumeroEdicion(generarOrdinal(count + 1));
        return new EdicionResponse(repo.save(ed));
    }

    private String generarOrdinal(long n) {
        return n + "° Edición";
    }

    

    @Override
    public List<EdicionResponse> listarPorRevista(Integer revistaId) {
        return repo.findByRevistaIdOrderByFechaPublicacionDesc(revistaId)
                .stream().map(EdicionResponse::new).toList();
    }
    
    @Override
    public EdicionResponse getById(Integer id) throws ExcepcionNoExiste{
        return new EdicionResponse(repo.findById(id).orElseThrow(() -> new ExcepcionNoExiste("Edición no existe")));
    }

    @Override
    public void eliminar(Integer id) { repo.deleteById(id); }

    @Override
    public List<EdicionResponse> findAll() {
        List<EdicionResponse> dtos = repo.findAll().stream()
                .map(EdicionResponse::new)
                .toList();
        return dtos;
    }
}
