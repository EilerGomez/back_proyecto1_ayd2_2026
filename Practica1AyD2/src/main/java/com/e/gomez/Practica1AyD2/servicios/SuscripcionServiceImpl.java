/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscricpionResponseByRevistaId;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscripcionRequest;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.dtoRevistasPorSuscripcionByUsuarioResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
import com.e.gomez.Practica1AyD2.repositorios.RevistaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.SuscripcionRepositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Service
public class SuscripcionServiceImpl implements SuscripcionService {
    private final SuscripcionRepositorio repo;
    private final RevistaRepositorio revistaRepo;
    private final RevistaService servicioRevista;
    private final UsuarioService usuarioService;
    public SuscripcionServiceImpl(SuscripcionRepositorio repo, RevistaRepositorio revistaRepo,
            RevistaService servicioRevista,UsuarioService usuarioService) {
        this.repo = repo;
        this.revistaRepo = revistaRepo;
        this.servicioRevista=servicioRevista;
        this.usuarioService=usuarioService;
    }

    @Override
    @Transactional
    public SuscricpionResponseByRevistaId suscribir(SuscripcionRequest sr) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste{
        if (repo.existsByRevistaIdAndUsuarioId(sr.getRevistaId(), sr.getUsuarioId())) {
            throw new ExcepcionEntidadDuplicada("Ya existe una suscripción para este usuario en esta revista.");
        }
        
        EntidadSuscripcion s = new EntidadSuscripcion();
        s.setRevistaId(sr.getRevistaId());
        s.setUsuarioId(sr.getUsuarioId());
        s.setFechaSuscripcion(sr.getFechaSuscripcion());
        s.setActiva(sr.isActiva());
        
        return new SuscricpionResponseByRevistaId(repo.save(s),new UsuarioResponse(usuarioService.getById(sr.getUsuarioId())));
    }

    @Override
    public void cancelarSuscripcion(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public List<dtoRevistasPorSuscripcionByUsuarioResponse> listarPorUsuario(Integer usuarioId)throws ExcepcionNoExiste {
        List<EntidadSuscripcion> dtos = repo.findByUsuarioId(usuarioId);
        List<dtoRevistasPorSuscripcionByUsuarioResponse> respo = new ArrayList<>();
        for (EntidadSuscripcion dto : dtos) {
            RevistaResponse rr = servicioRevista.getById(dto.getRevistaId());
            dtoRevistasPorSuscripcionByUsuarioResponse dtoR= new dtoRevistasPorSuscripcionByUsuarioResponse(dto, rr);
            respo.add(dtoR);
        }
        return respo;
    }

    @Override
    public List<SuscricpionResponseByRevistaId> listarPorRevista(Integer revistaId)throws ExcepcionNoExiste {
        List<EntidadSuscripcion> dtos = repo.findByRevistaId(revistaId);
        List<SuscricpionResponseByRevistaId> respo=new ArrayList<>();
        for (EntidadSuscripcion es : dtos) {
            UsuarioResponse ur = new UsuarioResponse(usuarioService.getById(es.getUsuarioId()));
            SuscricpionResponseByRevistaId srr = new SuscricpionResponseByRevistaId(es,ur);
            respo.add(srr);
        }
        return respo;
    }

    @Override
    @Transactional
    public void cambiarEstado(Integer id, boolean activa)  throws ExcepcionNoExiste{
        EntidadSuscripcion s = repo.findById(id).orElseThrow(() -> new ExcepcionNoExiste("No existe"));
        s.setActiva(activa);
        repo.save(s);
    }


   
}