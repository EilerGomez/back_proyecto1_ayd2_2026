/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;

public interface RecuperacionService {


    void solicitarRecuperacion(String identificador) throws ExcepcionNoExiste;


    boolean validarCodigo(String correo, String codigo) throws ExcepcionNoExiste;


    void cambiarContrasenia(String correo, String codigo, String nuevaPassword) throws ExcepcionNoExiste;
}