/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAuth;

import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioCompletoResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadRol;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class LoginResponse {
    String token;
    String tokenType;   // "Bearer"
    long expiresInMs;
    UsuarioCompletoResponse usuario;
    
}