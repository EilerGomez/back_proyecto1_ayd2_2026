/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoAnuncios.TipoAnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.mantenimiento.MantenimientoSistemaService;
import com.e.gomez.Practica1AyD2.modelos.EntidadAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadBloqueoAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.AnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.BloqueoAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.CompraAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RevistaRepositorio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Service
public class AnuncioServiceImpl implements AnuncioService {

    private final AnuncioRepositorio repo;
    private final UsuarioService usuarioService;
    private final TipoAnuncioService tipoAnuncioService;
    private final RevistaRepositorio revistaRepo;
    private final BloqueoAnuncioRepositorio bloqueoAnuncioRepo;
    private final PerfilRepositorio perfilRepo;
    private final CompraAnuncioRepositorio compraRepo;
    private final MantenimientoSistemaService mantenimiento;

    public AnuncioServiceImpl(AnuncioRepositorio repo, 
                              UsuarioService usuarioService, 
                              TipoAnuncioService tipoAnuncioService,  RevistaRepositorio revistaRepo,
                              BloqueoAnuncioRepositorio bloqueoAnuncioRepo, PerfilRepositorio perfilRepo,
                              CompraAnuncioRepositorio compraRepo, MantenimientoSistemaService mantenimiento) {
        this.repo = repo;
        this.usuarioService = usuarioService;
        this.tipoAnuncioService = tipoAnuncioService;
        this.bloqueoAnuncioRepo=bloqueoAnuncioRepo;
        this.revistaRepo=revistaRepo;
        this.perfilRepo=perfilRepo;
        this.compraRepo=compraRepo;
        this.mantenimiento=mantenimiento;
    }

    @Override
    @Transactional
    public AnuncioResponse crear(AnuncioRequest req) throws ExcepcionNoExiste {
        EntidadAnuncio anuncio = EntidadAnuncio.builder()
                .anuncianteId(req.getAnuncianteId())
                .tipoAnuncioId(req.getTipoAnuncioId())
                .texto(req.getTexto())
                .imagenUrl(req.getImagenUrl())
                .videoUrl(req.getVideoUrl())
                .urlDestino(req.getUrlDestino())
                .estado( "BORRADOR")
                .fechaCreacion(LocalDateTime.now())
                .build();
        return mapToResponse(repo.save(anuncio));
    }

    @Override
    public List<AnuncioResponse> listarPorEstado(String estado) {
        mantenimiento.desactivarAnunciosExpirados();
        List<AnuncioResponse> anunciosResponse = repo.findByEstadoRandom(estado).stream().map(this::mapToResponse).toList();
        //Collections.shuffle(anunciosResponse);

        return anunciosResponse;
    }

    @Override
    public List<AnuncioResponse> listarPorAnunciante(Integer anuncianteId) {
        mantenimiento.desactivarAnunciosExpirados();
        return repo.findByAnuncianteId(anuncianteId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public AnuncioResponse obtenerPorId(Integer id) throws ExcepcionNoExiste {
        EntidadAnuncio a = repo.findById(id).orElseThrow(() -> new ExcepcionNoExiste("Anuncio no existe"));
        return mapToResponse(a);
    }

    @Override
    @Transactional
    public void cambiarEstado(Integer id, String nuevoEstado) throws ExcepcionNoExiste {
        EntidadAnuncio a = repo.findById(id).orElseThrow(() -> new ExcepcionNoExiste("Anuncio no existe"));
        if(nuevoEstado.equalsIgnoreCase("ACTIVO")){
            List<EntidadCompraAnuncio> dto = compraRepo.findByAnuncioId(id);
            for (EntidadCompraAnuncio eca : dto) {
                if(eca.getEstado().equalsIgnoreCase("ACTIVO")){
                    a.setEstado(nuevoEstado);
                    break;
                }
            }
        }else{
            a.setEstado(nuevoEstado);
        }
        repo.save(a);
    }

    private AnuncioResponse mapToResponse(EntidadAnuncio a) {
        try {
            EntidadUsuario eu = usuarioService.getById(a.getAnuncianteId());
            UsuarioResponse ur = new UsuarioResponse(eu,perfilRepo.findByUsuarioId(eu.getId()).orElseThrow());
            TipoAnuncioResponse tr = tipoAnuncioService.obtenerPorId(a.getTipoAnuncioId());
            return new AnuncioResponse(a, ur, tr);
        } catch (Exception e) {
            return new AnuncioResponse(a, null, null);
        }
    }

@Override
    @Transactional
    public AnuncioResponse actualizar(Integer id, AnuncioRequest req) throws ExcepcionNoExiste {
        EntidadAnuncio anuncio = repo.findById(id)
                .orElseThrow(() -> new ExcepcionNoExiste("No se puede actualizar, el anuncio no existe."));

        anuncio.setTexto(req.getTexto());
        anuncio.setImagenUrl(req.getImagenUrl());
        anuncio.setVideoUrl(req.getVideoUrl());
        anuncio.setUrlDestino(req.getUrlDestino());
        anuncio.setTipoAnuncioId(req.getTipoAnuncioId());
        
        if (req.getEstado() != null) {
            anuncio.setEstado(req.getEstado().toUpperCase());
        }

        return mapToResponse(repo.save(anuncio));
    }

    @Override
    public List<AnuncioResponse> listarTodos() {
        return repo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AnuncioResponse> obtenerAnunciosParaRevista(Integer revistaId) {
        // 1. Verificar si la revista existe
        revistaRepo.findById(revistaId)
                .orElseThrow(() -> new RuntimeException("Revista no encontrada"));

        EntidadBloqueoAnuncio entidadBloqueo = bloqueoAnuncioRepo.findByRevistaIdAndEstado(revistaId, "ACTIVO")
                .stream().findFirst().orElse(null);

        if (entidadBloqueo != null) {
            return new ArrayList<>(); 
        }

        List<EntidadAnuncio> anunciosEntidad = repo.buscarAnunciosVigentes(LocalDateTime.now());

        List<AnuncioResponse> anunciosResponse = new ArrayList<>(
            anunciosEntidad.stream().map(this::mapToResponse).toList()
        );

        Collections.shuffle(anunciosResponse);

        return anunciosResponse;
    }
    
}
