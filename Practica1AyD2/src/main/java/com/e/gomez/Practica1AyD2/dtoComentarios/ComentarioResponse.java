/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoComentarios;

import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadComentario;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

/**
 *
 * @author eiler
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ComentarioResponse {
    int id;
    int revistaId;
    UsuarioResponse usuario;
    String contenido;
    LocalDateTime fechaCreacion;

    public ComentarioResponse(EntidadComentario c, UsuarioResponse u) {
        this.id = c.getId();
        this.revistaId = c.getRevistaId();
        this.usuario = u;
        this.contenido = c.getContenido();
        this.fechaCreacion = c.getFechaCreacion();
    }
}
