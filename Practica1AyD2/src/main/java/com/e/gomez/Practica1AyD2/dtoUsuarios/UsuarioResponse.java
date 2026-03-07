/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoUsuarios;

import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import lombok.Data;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author eiler
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UsuarioResponse {
    private Integer id;
    private String nombre;
    private String username;
    private String apellido;
    private String correo;
    private String estado;
    private String perfilUrl;
    
     
    
    public UsuarioResponse(EntidadUsuario entidadUsuario, EntidadPerfil entidadPerfil ){
        this.id=entidadUsuario.getId();
        this.nombre=entidadUsuario.getNombre();
        this.username=entidadUsuario.getUsername();
        this.apellido=entidadUsuario.getApellido();
        this.correo=entidadUsuario.getCorreo();
        this.estado=entidadUsuario.getEstado();
        this.perfilUrl=entidadPerfil.getFoto_url();
    }
            
 
}
