/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoRoles;

import lombok.Data;
import lombok.Value;
import com.e.gomez.Practica1AyD2.modelos.EntidadRol;

/**
 *
 * @author eiler
 */

@Value
public class RolesResponse {
    Integer id;
    String nombre;
    
    public RolesResponse(EntidadRol rolentidad){
        this.id=rolentidad.getId();
        this.nombre=rolentidad.getNombre();
        
    }
}
