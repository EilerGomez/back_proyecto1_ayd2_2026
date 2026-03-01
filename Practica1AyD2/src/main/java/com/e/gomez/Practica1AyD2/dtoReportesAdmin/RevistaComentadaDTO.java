/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesAdmin;

import com.e.gomez.Practica1AyD2.dtoReportesEditor.ComentarioDetalleDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author eiler
 */
@Data
@AllArgsConstructor
public class RevistaComentadaDTO {
    private Integer revistaId;
    private String titulo;
    private Long totalComentarios;
    private List<ComentarioDetalleDTO> comentarios;
}