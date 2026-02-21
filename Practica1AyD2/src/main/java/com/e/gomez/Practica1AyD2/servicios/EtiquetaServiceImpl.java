/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaRequest;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadEtiqueta;
import com.e.gomez.Practica1AyD2.repositorios.EtiquetaRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtiquetaServiceImpl implements EtiquetaService {

    private final EtiquetaRepositorio repositorio;

    public EtiquetaServiceImpl(EtiquetaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<EtiquetaResponse> findAll() {
        return repositorio.findAll().stream()
                .map(EtiquetaResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public EtiquetaResponse getById(Integer id) throws ExcepcionNoExiste {
        EntidadEtiqueta ee = repositorio.findById(id)
                .orElseThrow(() -> new ExcepcionNoExiste("Etiqueta no encontrada"));
        return new EtiquetaResponse(ee);
    }

    @Override
    public EtiquetaResponse crear(EtiquetaRequest request) throws ExcepcionEntidadDuplicada {
        if (repositorio.existsByNombre(request.getNombre())) {
            throw new ExcepcionEntidadDuplicada("La etiqueta ya existe");
        }
        EntidadEtiqueta nueva = new EntidadEtiqueta();
        nueva.setNombre(request.getNombre());
        return new EtiquetaResponse(repositorio.save(nueva));
    }

    @Override
    public EtiquetaResponse actualizar(Integer id, EtiquetaRequest request) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste {
        EntidadEtiqueta existente = repositorio.findById(id)
                .orElseThrow(() -> new ExcepcionNoExiste("No existe la etiqueta"));
        
        if (!existente.getNombre().equals(request.getNombre()) && repositorio.existsByNombre(request.getNombre())) {
            throw new ExcepcionEntidadDuplicada("El nombre ya está en uso");
        }

        existente.setNombre(request.getNombre());
        return new EtiquetaResponse(repositorio.save(existente));
    }

    @Override
    public void eliminar(Integer id) throws ExcepcionNoExiste {
        if (!repositorio.existsById(id)) throw new ExcepcionNoExiste("No existe");
        repositorio.deleteById(id);
    }
}
