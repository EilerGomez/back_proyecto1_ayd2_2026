/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoComentarios.ComentarioRequest;
import com.e.gomez.Practica1AyD2.dtoComentarios.ComentarioResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadComentario;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.ComentarioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RevistaRepositorio;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Service
public class ComentarioServiceImpl implements ComentarioService {
    private final ComentarioRepositorio repo;
    private final UsuarioService usuarioServicio;
    private final PerfilRepositorio perfilRepo;
    private final RevistaRepositorio revistaRepo;

    public ComentarioServiceImpl(ComentarioRepositorio repo,UsuarioService usuarioServicio,PerfilRepositorio perfilRepo,RevistaRepositorio revistaRepo){ 
        this.repo = repo; 
        this.usuarioServicio=usuarioServicio;
        this.perfilRepo=perfilRepo;
        this.revistaRepo=revistaRepo;
    }

    @Override
    @Transactional
    public ComentarioResponse crear(ComentarioRequest req)  throws ExcepcionNoExiste{
        EntidadRevista revista = revistaRepo.getById(req.getRevistaId());
        if(!revista.isPermiteComentarios()){
            throw new ExcepcionNoExiste("No se permiten comentarios a esta revista");
        }
        EntidadComentario c = new EntidadComentario();
        c.setRevistaId(req.getRevistaId());
        c.setUsuarioId(req.getUsuarioId());
        c.setContenido(req.getContenido());
        c.setFechaCreacion(LocalDateTime.now());
        
        EntidadUsuario u = usuarioServicio.getById(req.getUsuarioId());
        return new ComentarioResponse(repo.save(c), new UsuarioResponse(u, perfilRepo.findByUsuarioId(u.getId()).orElseThrow()));
    }

    @Override
    @Transactional
    public ComentarioResponse actualizar(Integer id, String nuevoContenido) throws ExcepcionNoExiste{
        EntidadComentario c = repo.findById(id)
                .orElseThrow(() -> new ExcepcionNoExiste("El comentario no existe"));
        c.setContenido(nuevoContenido);
        EntidadUsuario u = usuarioServicio.getById(c.getUsuarioId());
        
        return new ComentarioResponse(repo.save(c), new UsuarioResponse(u, perfilRepo.findByUsuarioId(u.getId()).orElseThrow()));
    }

    @Override
    public void eliminar(Integer id) throws ExcepcionNoExiste{
        if (!repo.existsById(id)) throw new ExcepcionNoExiste("Comentario no encontrado");
        repo.deleteById(id);
    }

    @Override
    public List<ComentarioResponse> listarPorRevista(Integer revistaId) throws ExcepcionNoExiste {
        List<EntidadComentario> entidades = repo.findByRevistaIdOrderByFechaCreacionDesc(revistaId);
        List<ComentarioResponse> dtos = new ArrayList<>();
        
        for (EntidadComentario ec : entidades) {
            EntidadUsuario u = usuarioServicio.getById(ec.getUsuarioId());
            ComentarioResponse cr = new ComentarioResponse(ec, new UsuarioResponse(u, perfilRepo.findByUsuarioId(u.getId()).orElseThrow()));
            dtos.add(cr);
        }
        
        return dtos;
    }

    @Override
    public List<ComentarioResponse> listarPorUsuario(Integer usuarioId)throws ExcepcionNoExiste {
        List<EntidadComentario> entidades = repo.findByUsuarioIdOrderByFechaCreacionDesc(usuarioId);
        List<ComentarioResponse> dtos = new ArrayList<>();
        
        for (EntidadComentario ec : entidades) {
            EntidadUsuario u = usuarioServicio.getById(ec.getUsuarioId());
            ComentarioResponse cr = new ComentarioResponse(ec, new UsuarioResponse(u, perfilRepo.findByUsuarioId(u.getId()).orElseThrow()));
            dtos.add(cr);
        }
        
        return dtos;
    }
}