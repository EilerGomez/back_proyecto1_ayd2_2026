/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoRevistas;

import com.e.gomez.Practica1AyD2.dtoCategorias.CategoriaResponse;
import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionResponse;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadCategoria;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import java.util.List;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Value
public class RevistaResponse {
    int id;
    String titulo;
    String descripcion;
    CategoriaResponse categoria;
    UsuarioResponse editor;
    List<EtiquetaResponse> etiquetas;
    List<EdicionResponse> ediciones;
    
    int cantidadComentarios;
    int cantidadLikes;
    int cantidadSuscripciones;

    public RevistaResponse(EntidadRevista r, List<EtiquetaResponse> tags, List<EdicionResponse> edics, int cantidadComents, int cantidadLikes,
            int cantidadSucripciones, EntidadCategoria ec, EntidadUsuario usuarioEditor) {
        this.id = r.getId();
        this.titulo = r.getTitulo();
        this.descripcion = r.getDescripcion();
        this.categoria = new CategoriaResponse(ec);
        this.etiquetas = tags;
        this.ediciones = edics;
        this.cantidadComentarios=cantidadComents;
        this.cantidadLikes=cantidadLikes;
        this.cantidadSuscripciones=cantidadSucripciones;
        this.editor= new UsuarioResponse(usuarioEditor);
    }
}