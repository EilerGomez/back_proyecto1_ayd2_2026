/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoCategorias.CategoriaRequest;
import com.e.gomez.Practica1AyD2.dtoCategorias.CategoriaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCategoria;
import com.e.gomez.Practica1AyD2.repositorios.CategoriaRepositorio;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepositorio repositorio;

    public CategoriaServiceImpl(CategoriaRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<EntidadCategoria> findAll() {
        return repositorio.findAll();
    }

    @Override
    public EntidadCategoria getById(Integer id) throws ExcepcionNoExiste{
        return repositorio.findById(id)
                .orElseThrow(() -> new ExcepcionNoExiste("La categoría con ID " + id + " no existe."));
    }

    @Override
    public EntidadCategoria crear(CategoriaRequest request) throws ExcepcionEntidadDuplicada{
        if (repositorio.existsByNombre(request.getNombre())) {
            throw new ExcepcionEntidadDuplicada("Ya existe una categoría con el nombre: " + request.getNombre());
        }
        EntidadCategoria cat = new EntidadCategoria();
        cat.setNombre(request.getNombre());
        cat.setDescripcion(request.getDescripcion());
        return repositorio.save(cat);
    }

    @Override
    public EntidadCategoria actualizar(Integer id, CategoriaRequest request) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste{
        EntidadCategoria cat = getById(id);
        
        // Validar que el nuevo nombre no lo tenga otra categoría diferente
        if (!cat.getNombre().equals(request.getNombre()) && repositorio.existsByNombre(request.getNombre())) {
            throw new ExcepcionEntidadDuplicada("El nombre '" + request.getNombre() + "' ya está en uso.");
        }

        cat.setNombre(request.getNombre());
        cat.setDescripcion(request.getDescripcion());
        return repositorio.save(cat);
    }

    @Override
    public void eliminar(Integer id) throws ExcepcionNoExiste{
        EntidadCategoria cat = getById(id);
        repositorio.delete(cat);
    }
}
