/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAuth;

/**
 *
 * @author eiler
 */
import lombok.Value;

@Value
public class LoginRequest {
    String identificador; // username o correo
    String password;
}
