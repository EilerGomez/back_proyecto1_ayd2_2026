/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioBloqueoRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioBloqueoResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPrecioBloqueo;
import com.e.gomez.Practica1AyD2.repositorios.PrecioBloqueoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Service
public class PrecioBloqueoServiceImpl implements PrecioBloqueoService {

    private final PrecioBloqueoRepositorio repo;

    public PrecioBloqueoServiceImpl(PrecioBloqueoRepositorio repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public PrecioBloqueoResponse asignarOActualizar(PrecioBloqueoRequest req) {
        // Buscamos si ya existe para actualizar, o creamos uno nuevo
        EntidadPrecioBloqueo entidad = repo.findByRevistaId(req.getRevistaId())
                .orElse(new EntidadPrecioBloqueo());

        entidad.setRevistaId(req.getRevistaId());
        entidad.setCostoPorDia(req.getCostoPorDia());
        entidad.setAdminId(req.getAdminId());

        return new PrecioBloqueoResponse(repo.save(entidad));
    }

    @Override
    public PrecioBloqueoResponse obtenerPorRevista(Integer revistaId) throws ExcepcionNoExiste {
        return repo.findByRevistaId(revistaId)
                .map(PrecioBloqueoResponse::new)
                .orElseThrow(() -> new ExcepcionNoExiste("No hay precio de bloqueo definido para la revista " + revistaId));
    }

    @Override
    @Transactional
    public void eliminarPrecio(Integer id) throws ExcepcionNoExiste {
        if (!repo.existsById(id)) throw new ExcepcionNoExiste("El registro no existe");
        repo.deleteById(id);
    }
}
