/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoRevistas;

import lombok.Data;

/**
 *
 * @author eiler
 */
@Data
public class RevistaRequest {
    private Integer editorId;
    private String titulo;
    private String descripcion;
    private Integer categoriaId;
    private String fechaCreacion; // Formato YYYY-MM-DD
}