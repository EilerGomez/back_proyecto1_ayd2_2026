/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoUsuarios.NuevoPerfilRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 *
 * @author eiler
 */
@Service
public class PerfilServiceImpl implements PerfilService{
    
    private final PerfilRepositorio repository;
    
    @Autowired
    public PerfilServiceImpl(PerfilRepositorio repo){
        this.repository=repo;
    }

    @Override
    public EntidadPerfil crearPerfil(Integer idUsuario) throws ExcepcionEntidadDuplicada {
        if(repository.existsByUsuarioId(idUsuario)){
            throw new ExcepcionEntidadDuplicada("El usuario ya tiene perfil");
        } 
        
        EntidadPerfil nuevoPerfil = new EntidadPerfil();
        nuevoPerfil.setUsuarioId(idUsuario);
        
        nuevoPerfil = repository.save(nuevoPerfil);
        
        return nuevoPerfil;
    }

    @Override
    public EntidadPerfil findByUsuarioId(Integer usuarioId) throws ExcepcionNoExiste{
        return repository.findByUsuarioId(usuarioId).orElseThrow(() -> new ExcepcionNoExiste("Perfil no encontrado para usuarioId: " + usuarioId));
    }

    @Override
    public EntidadPerfil updatePerfil(Integer usuarioId, NuevoPerfilRequest nuevoPerfil)throws ExcepcionNoExiste {
         EntidadPerfil perfil = repository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ExcepcionNoExiste("Perfil no encontrado para usuarioId: " + usuarioId));

        // Actualización tipo PATCH: solo pisa lo que venga no-nulo
        if (nuevoPerfil.getFoto_url() != null) perfil.setFoto_url(nuevoPerfil.getFoto_url());
        if (nuevoPerfil.getHobbies() != null) perfil.setHobbies(nuevoPerfil.getHobbies());
        if (nuevoPerfil.getIntereses() != null) perfil.setIntereses(nuevoPerfil.getIntereses());
        if (nuevoPerfil.getDescripcion() != null) perfil.setDescripcion(nuevoPerfil.getDescripcion());
        if (nuevoPerfil.getGustos() != null) perfil.setGustos(nuevoPerfil.getGustos());

        return repository.save(perfil);
        
    }
    
}
