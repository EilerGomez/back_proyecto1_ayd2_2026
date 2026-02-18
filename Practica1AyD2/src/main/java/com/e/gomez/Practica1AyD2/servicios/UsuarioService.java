/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoUsuarios.NuevoUsuarioRequest;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioUpdateRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;

/**
 *
 * @author eiler
 */
public interface UsuarioService {
    EntidadUsuario crearUsuario(NuevoUsuarioRequest nuevoU) throws ExcepcionEntidadDuplicada,ExcepcionNoExiste;
    EntidadUsuario getById(Integer id) throws ExcepcionNoExiste;
    List<EntidadUsuario> findAll();
    
    void eliminarUsuario (Integer id) throws ExcepcionNoExiste;
    
    UsuarioResponse actializarUsuario (Integer id, UsuarioUpdateRequest dataAActualizar) throws ExcepcionEntidadDuplicada,ExcepcionNoExiste;
    
}
