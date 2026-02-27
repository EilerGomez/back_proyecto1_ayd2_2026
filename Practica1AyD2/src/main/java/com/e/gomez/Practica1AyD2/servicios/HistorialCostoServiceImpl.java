/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoRequest;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadHistorialCosto;
import com.e.gomez.Practica1AyD2.repositorios.HistorialCostoRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Service
public class HistorialCostoServiceImpl implements HistorialCostoService {

    private final HistorialCostoRepositorio repo;

    public HistorialCostoServiceImpl(HistorialCostoRepositorio repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public HistorialCostoResponse asignarNuevoCosto(HistorialCostoRequest req) throws ExcepcionNoExiste {
        //  Finalizar el costo anterior si existe (el que no tiene fecha_fin)
        repo.findByRevistaIdAndFechaFinIsNull(req.getRevistaId())
                .ifPresent(costoAnterior -> {
                    // La fecha fin es un día antes de que inicie el nuevo costo
                    costoAnterior.setFechaFin(req.getFechaInicio().minusDays(1));
                    repo.save(costoAnterior);
                });

        //  Crear el nuevo registro de costo
        EntidadHistorialCosto nuevoCosto = new EntidadHistorialCosto();
        nuevoCosto.setRevistaId(req.getRevistaId());
        nuevoCosto.setAdminId(req.getAdminId());
        nuevoCosto.setCostoPorDia(req.getCostoPorDia());
        nuevoCosto.setFechaInicio(req.getFechaInicio());
        nuevoCosto.setFechaFin(null); // Vigente

        return new HistorialCostoResponse(repo.save(nuevoCosto));
    }

    @Override
    public HistorialCostoResponse obtenerCostoVigente(Integer revistaId) throws ExcepcionNoExiste {
        return repo.findByRevistaIdAndFechaFinIsNull(revistaId)
                .map(HistorialCostoResponse::new)
                .orElseThrow(() -> new ExcepcionNoExiste("La revista no tiene un costo asignado actualmente."));
    }

    @Override
    public List<HistorialCostoResponse> obtenerHistorialPorRevista(Integer revistaId) throws ExcepcionNoExiste {
        return repo.findByRevistaId(revistaId).stream()
                .map(HistorialCostoResponse::new)
                .toList();
    }
}