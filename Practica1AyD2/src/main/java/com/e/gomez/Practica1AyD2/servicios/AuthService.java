/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionCredencialesInvalidas;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.dtoAuth.LoginRequest;
import com.e.gomez.Practica1AyD2.dtoAuth.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest req) throws ExcepcionNoExiste, ExcepcionCredencialesInvalidas;
    void logout(String token) throws ExcepcionCredencialesInvalidas;
}
