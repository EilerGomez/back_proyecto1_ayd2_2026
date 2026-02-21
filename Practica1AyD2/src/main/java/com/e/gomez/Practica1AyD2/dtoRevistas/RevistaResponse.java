/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoRevistas;

import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionResponse;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
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
    int categoriaId;
    int editorId;
    List<EtiquetaResponse> etiquetas;
    List<EdicionResponse> ediciones;

    public RevistaResponse(EntidadRevista r, List<EtiquetaResponse> tags, List<EdicionResponse> edics) {
        this.id = r.getId();
        this.titulo = r.getTitulo();
        this.descripcion = r.getDescripcion();
        this.categoriaId = r.getCategoriaId();
        this.etiquetas = tags;
        this.ediciones = edics;
        this.editorId=r.getEditorId();
    }
}