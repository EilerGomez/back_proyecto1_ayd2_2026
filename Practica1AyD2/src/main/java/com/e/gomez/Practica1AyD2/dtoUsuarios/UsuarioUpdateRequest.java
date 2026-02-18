/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoUsuarios;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class UsuarioUpdateRequest {
    private int id;
    private String nombre;
    private String username;
    private String apellido;
    private String correo;
    private String estado;

    public UsuarioUpdateRequest(int id,String nuevoNombre, String nuevoApellido, String nuevoUser, String nuevomailcom, String activo) {
        this.apellido=nuevoApellido;
        this.nombre=nuevoNombre;
        this.username=nuevoUser;
        this.correo=nuevomailcom;
        this.estado=activo;
        this.id=id;
    }
    
}
