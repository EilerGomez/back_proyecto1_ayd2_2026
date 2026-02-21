/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionResponse;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.RevistaEtiquetasRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevistaEtiqueta;
import com.e.gomez.Practica1AyD2.modelos.RevistaEtiquetaId;
import com.e.gomez.Practica1AyD2.repositorios.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class RevistaServiceImpl implements RevistaService {

    private final RevistaRepositorio repo;
    private final RevistaEtiquetaRepositorio revEtiqRepo;
    private final EtiquetaRepositorio etiqRepo;
    private final EdicionRepositorio edicionRepo;

    public RevistaServiceImpl(RevistaRepositorio repo, 
                              RevistaEtiquetaRepositorio revEtiqRepo, 
                              EtiquetaRepositorio etiqRepo, 
                              EdicionRepositorio edicionRepo) {
        this.repo = repo;
        this.revEtiqRepo = revEtiqRepo;
        this.etiqRepo = etiqRepo;
        this.edicionRepo = edicionRepo;
    }

    @Override
    @Transactional
    public RevistaResponse crear(RevistaRequest req) throws ExcepcionEntidadDuplicada {
        if (repo.existsByTitulo(req.getTitulo())) {
            throw new ExcepcionEntidadDuplicada("Ya existe una revista con el título: " + req.getTitulo());
        }

        EntidadRevista r = new EntidadRevista();
        r.setEditorId(req.getEditorId());
        r.setTitulo(req.getTitulo());
        r.setDescripcion(req.getDescripcion());
        r.setCategoriaId(req.getCategoriaId());
        r.setFechaCreacion(LocalDate.now()); // O usar LocalDate.parse(req.getFechaCreacion())
        r.setActiva(true);

        return mapToResponse(repo.save(r));
    }

    @Override
    @Transactional
    public RevistaResponse actualizar(Integer id, RevistaRequest req) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste {
        EntidadRevista r = repo.findById(id)
                .orElseThrow(() -> new ExcepcionNoExiste("La revista no existe"));

        // Validar si el nuevo título ya lo tiene otra revista
        if (repo.existeTituloEnOtraRevista(req.getTitulo(), id)) {
            throw new ExcepcionEntidadDuplicada("El título '" + req.getTitulo() + "' ya está siendo usado por otra revista.");
        }

        r.setTitulo(req.getTitulo());
        r.setDescripcion(req.getDescripcion());
        r.setCategoriaId(req.getCategoriaId());

        return mapToResponse(repo.save(r));
    }

    @Override
    public void eliminar(Integer id) throws ExcepcionNoExiste {
        if (!repo.existsById(id)) throw new ExcepcionNoExiste("Revista no encontrada");
        repo.deleteById(id);
    }

    @Override
    public RevistaResponse getById(Integer id) throws ExcepcionNoExiste {
        return repo.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ExcepcionNoExiste("No existe la revista con ID: " + id));
    }

    @Override
    public List<RevistaResponse> findAll() {
        return repo.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<RevistaResponse> findByActivas() {
        return repo.findByActivaTrue().stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<RevistaResponse> findByEditorId(Integer editorId) {
        return repo.findByEditorId(editorId).stream().map(this::mapToResponse).toList();
    }

    @Override
    public List<RevistaResponse> findByCategoriaId(Integer categoriaId) {
        return repo.findByCategoriaId(categoriaId).stream().map(this::mapToResponse).toList();
    }


    private RevistaResponse mapToResponse(EntidadRevista r) {
        //  Obtener etiquetas vinculadas en la tabla intermedia
        List<Integer> idsEtiquetas = revEtiqRepo.findById_RevistaId(r.getId())
                .stream()
                .map(re -> re.getId().getEtiquetaId())
                .toList();
        
        List<EtiquetaResponse> tags = etiqRepo.findAllById(idsEtiquetas)
                .stream()
                .map(EtiquetaResponse::new)
                .toList();

        //  Obtener todas las ediciones de esta revista
        List<EdicionResponse> edics = edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(r.getId())
                .stream()
                .map(EdicionResponse::new)
                .toList();

        return new RevistaResponse(r, tags, edics);
    }

    @Override
    @Transactional
    public void guardarEtiquetas(RevistaEtiquetasRequest req) throws ExcepcionNoExiste{
        
        if (!repo.existsById(req.getIdRevista())) {
            throw new ExcepcionNoExiste("No se pueden asignar etiquetas: La revista no existe.");
        }

        revEtiqRepo.eliminarEtiquetasDeRevista(req.getIdRevista());

        if (req.getEtiquetasIds() != null && !req.getEtiquetasIds().isEmpty()) {
            List<EntidadRevistaEtiqueta> nuevasRelaciones = req.getEtiquetasIds().stream()
                .map(etiquetaId -> {
                    RevistaEtiquetaId idCompuesto = new RevistaEtiquetaId(req.getIdRevista(), etiquetaId);
                    // Creamos la entidad intermedia
                    return new EntidadRevistaEtiqueta(idCompuesto);
                })
                .toList();
            revEtiqRepo.saveAll(nuevasRelaciones);
        }
    }
}
