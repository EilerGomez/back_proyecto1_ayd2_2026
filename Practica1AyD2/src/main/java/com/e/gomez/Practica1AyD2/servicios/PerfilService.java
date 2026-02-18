/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;

/**
 *
 * @author eiler
 */
public interface PerfilService {
    EntidadPerfil crearPerfil (Integer idUsuario) throws ExcepcionEntidadDuplicada;
    EntidadPerfil findByUsuarioId(Integer usuarioId);
}
