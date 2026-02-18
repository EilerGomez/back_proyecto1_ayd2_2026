/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;
import com.e.gomez.Practica1AyD2.modelos.EntidadRol;
import com.e.gomez.Practica1AyD2.modelos.Entidad_Usuario_Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.e.gomez.Practica1AyD2.repositorios.RolRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRolRepositorio;
import org.springframework.data.crossstore.ChangeSetPersister;

/**
 *
 * @author eiler
 */

@Service
public class RolServiceImpl implements RolService{
       
    private final RolRepositorio rolRepository;
    private final UsuarioRolRepositorio userRorepo;

    @Autowired
    public RolServiceImpl(RolRepositorio repositorio, UsuarioRolRepositorio urr){
        this.rolRepository=repositorio;
        this.userRorepo=urr;
    }

    @Override
    public List<EntidadRol> findAll() {
        return rolRepository.findAll();
    }

    @Override
    public EntidadRol findById(Integer id) throws ExcepcionNoExiste{
        return rolRepository.findById(id)
                .orElseThrow(() -> new ExcepcionNoExiste("Rol no encontrado"));    
    }

    @Override
    public Entidad_Usuario_Rol buscarIdRolByIdUsuario(Integer idUsuario) throws ExcepcionNoExiste {
           return userRorepo.findByIdUsuarioId(idUsuario);
    }

    @Override
    public EntidadRol traerRolDeUsuario(Integer id) throws ExcepcionNoExiste {
        Entidad_Usuario_Rol roluser= buscarIdRolByIdUsuario(id);
        return findById(roluser.getId().getRolId());
    }
    
}
