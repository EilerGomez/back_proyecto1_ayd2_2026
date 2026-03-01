/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesAdmin;

import com.e.gomez.Practica1AyD2.dtoReportesEditor.SuscripcionDetalleDTO;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author eiler
 */
@Data
public class RevistaPopularDTO {
    private Integer revistaId;
    private String titulo;
    private Long totalSuscripciones;
    private List<SuscripcionDetalleDTO> suscripciones;
    public RevistaPopularDTO(EntidadRevista er, List<SuscripcionDetalleDTO> ss){
        this.revistaId=er.getId();
        this.titulo=er.getTitulo();
        this.totalSuscripciones=(long)ss.size();
        this.suscripciones=ss;
    }
}