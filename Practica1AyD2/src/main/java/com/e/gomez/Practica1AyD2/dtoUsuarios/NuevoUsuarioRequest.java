/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoUsuarios;

import lombok.Value;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 *
 * @author eiler
 */
@AllArgsConstructor
@Getter
public class NuevoUsuarioRequest {
     String nombre;
     String username;
     String apellido;
     String correo;
     String password;
     String estado;
     Integer id_rol;
     

     
    public EntidadUsuario crearEntidad(){
        EntidadUsuario nuevaEntidad= new EntidadUsuario();
        nuevaEntidad.setNombre(getNombre());
        nuevaEntidad.setUsername(getUsername());
        nuevaEntidad.setApellido(getApellido());
        nuevaEntidad.setCorreo(getCorreo());
        nuevaEntidad.setEstado("ACTIVO");
        nuevaEntidad.setPassword_hash(getPassword());
        return nuevaEntidad;
    }
}
