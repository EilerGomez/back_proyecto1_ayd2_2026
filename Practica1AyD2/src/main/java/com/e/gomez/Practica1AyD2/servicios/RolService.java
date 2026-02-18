/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;
import com.e.gomez.Practica1AyD2.modelos.EntidadRol;
import com.e.gomez.Practica1AyD2.modelos.Entidad_Usuario_Rol;

/**
 *
 * @author eiler
 */
public interface RolService {
    List<EntidadRol> findAll();
    
    EntidadRol findById(Integer id) throws ExcepcionNoExiste;
    
    Entidad_Usuario_Rol buscarIdRolByIdUsuario(Integer idUsuario) throws ExcepcionNoExiste;
    
    EntidadRol traerRolDeUsuario(Integer id) throws ExcepcionNoExiste;

}
