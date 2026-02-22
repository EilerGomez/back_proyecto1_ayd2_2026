/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoLikes.LikeRequest;
import com.e.gomez.Practica1AyD2.dtoLikes.LikeResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadLike;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.LikeRepositorio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Service
public class LikeServiceImpl implements LikeService {
    private final LikeRepositorio repo;
    private final UsuarioService usuarioService;

    @Autowired
    public LikeServiceImpl(LikeRepositorio repo, UsuarioService usuarioService) {
        this.repo = repo; 
        this.usuarioService=usuarioService;
    }

    @Override
    @Transactional
    public LikeResponse darLike(LikeRequest req) throws ExcepcionNoExiste,ExcepcionEntidadDuplicada{
        if (this.yaDioLike(req.getRevistaId(), req.getUsuarioId())) {
        
            throw new ExcepcionEntidadDuplicada("Ya diste like a esta revista");
        }
        EntidadLike l = new EntidadLike();
        l.setRevistaId(req.getRevistaId());
        l.setUsuarioId(req.getUsuarioId());
        return new LikeResponse(repo.save(l),new UsuarioResponse(usuarioService.getById(req.getUsuarioId())));
        
    }

    @Override
    @Transactional
    public void quitarLike(Integer revistaId, Integer usuarioId) {
        repo.deleteByRevistaIdAndUsuarioId(revistaId, usuarioId);
    }

    @Override
    public int contarLikesRevista(Integer revistaId) {
        return repo.countByRevistaId(revistaId);
    }

    @Override
    public boolean yaDioLike(Integer revistaId, Integer usuarioId) {
        return repo.existsByRevistaIdAndUsuarioId(revistaId, usuarioId);
    }

    @Override
    public List<LikeResponse> findByRevistaId(Integer revistaId) throws ExcepcionNoExiste {
        List<EntidadLike> entidades = repo.findByRevistaId(revistaId);
        List<LikeResponse> dtos = new ArrayList<>();
        for (EntidadLike el : entidades) {
            EntidadUsuario u = usuarioService.getById(el.getUsuarioId());
            LikeResponse lr = new LikeResponse(el, new UsuarioResponse(u));
            dtos.add(lr);
        }
        
        return dtos;
    }
}
