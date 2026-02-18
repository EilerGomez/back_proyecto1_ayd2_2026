/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
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
    public EntidadPerfil findByUsuarioId(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
    
}
