/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoUsuarios;

import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.modelos.EntidadRol;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author eiler
 */

@Data
@AllArgsConstructor
public class UsuarioCompletoResponse {

    private UsuarioResponse usuario;
    private EntidadPerfil perfil;
    private EntidadRol rol;
}
