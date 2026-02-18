/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

import com.e.gomez.Practica1AyD2.modelos.Entidad_Usuario_Rol;
import com.e.gomez.Practica1AyD2.modelos.UsuarioRolId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author eiler
 */
public interface UsuarioRolRepositorio extends JpaRepository<Entidad_Usuario_Rol, UsuarioRolId> {
    Entidad_Usuario_Rol findByIdUsuarioId(Integer usuarioId);
}
