/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.*;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPrecioAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.PrecioAnuncioRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

@Service
public class PrecioAnuncioServiceImpl implements PrecioAnuncioService {

    private final PrecioAnuncioRepositorio repo;
    private final TipoAnuncioService tipoService;
    private final PeriodoAnuncioService periodoService;
    private final UsuarioService usuarioService;
    private final PerfilService perfilService;

    public PrecioAnuncioServiceImpl(PrecioAnuncioRepositorio repo, 
                                    TipoAnuncioService tipoService, 
                                    PeriodoAnuncioService periodoService,
                                    UsuarioService usuarioService,
                                    PerfilService perfilService) {
        this.repo = repo;
        this.tipoService = tipoService;
        this.periodoService = periodoService;
        this.usuarioService=usuarioService;
        this.perfilService=perfilService;
    }

    @Override
    @Transactional
    public PrecioAnuncioResponse crear(PrecioAnuncioRequest req) throws ExcepcionNoExiste {
        // Desactivar cualquier precio anterior para esta combinación 
        repo.findByTipoAnuncioIdAndPeriodoIdAndActivoTrue(req.getTipoAnuncioId(), req.getPeriodoId())
            .ifPresent(p -> { p.setActivo(false); repo.save(p); });

        EntidadPrecioAnuncio entidad = EntidadPrecioAnuncio.builder()
                .tipoAnuncioId(req.getTipoAnuncioId())
                .periodoId(req.getPeriodoId())
                .precio(req.getPrecio())
                .adminId(req.getAdminId())
                .activo(true)
                .build();
        
        return mapToResponse(repo.save(entidad));
    }

    @Override
    public List<PrecioAnuncioResponse> listarTodos() {
        return repo.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PrecioAnuncioResponse obtenerPorId(Integer id) throws ExcepcionNoExiste {
        EntidadPrecioAnuncio p = repo.findById(id)
                .orElseThrow(() -> new ExcepcionNoExiste("Precio no encontrado"));
        return mapToResponse(p);
    }

    @Override
    @Transactional
    public void desactivar(Integer id) throws ExcepcionNoExiste {
        EntidadPrecioAnuncio p = repo.findById(id)
                .orElseThrow(() -> new ExcepcionNoExiste("Precio no encontrado"));
        p.setActivo(false);
        repo.save(p);
    }

    // metodo para mapear la entidad response completa
    private PrecioAnuncioResponse mapToResponse(EntidadPrecioAnuncio p) {
        try {
            EntidadUsuario eu =  usuarioService.getById(p.getAdminId());
            UsuarioResponse u = new UsuarioResponse(eu,perfilService.findByUsuarioId(eu.getId()));
            TipoAnuncioResponse t = tipoService.obtenerPorId(p.getTipoAnuncioId());
            PeriodoAnuncioResponse per = periodoService.obtenerPorId(p.getPeriodoId());
            return new PrecioAnuncioResponse(p, t, per,u);
        } catch (ExcepcionNoExiste e) {
            
            return new PrecioAnuncioResponse(p, null, null,null);
        }
    }

    @Override
    public List<PrecioAnuncioResponse> obtenerPorTipoAnuncio(Integer tipoAnuncioId) throws ExcepcionNoExiste {
        return repo.findByTipoAnuncioId(tipoAnuncioId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    
}
