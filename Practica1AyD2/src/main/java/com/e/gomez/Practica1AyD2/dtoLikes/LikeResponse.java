/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoLikes;

import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadLike;
import java.time.LocalDateTime;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class LikeResponse {
    int id;
    int revistaId;
    UsuarioResponse usuario;
    LocalDateTime fechaCreacion;

    public LikeResponse(EntidadLike l, UsuarioResponse u) {
        this.id = l.getId();
        this.revistaId = l.getRevistaId();
        this.usuario = u;
        this.fechaCreacion = l.getFechaCreacion();
    }
}
