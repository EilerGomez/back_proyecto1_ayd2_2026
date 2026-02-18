/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.excepciones;

/**
 *
 * @author eiler
 */
public class ExcepcionNoExiste extends ServiceException{
    public ExcepcionNoExiste(){}
    
    public ExcepcionNoExiste(String mensaje){
        super(mensaje);
    }
}
