/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoUsuarios;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author eiler
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class NuevoPerfilRequest {
    private String foto_url;
    private String hobbies;
    private String intereses;
    private String descripcion;
    private String gustos;
    
}
