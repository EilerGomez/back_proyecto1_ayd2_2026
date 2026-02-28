/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesEditor;

import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponseSimpleToReport;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author eiler
 */
@Data
@AllArgsConstructor
public class ReporteSuscripcionesEditorResponse {
    private RevistaResponseSimpleToReport revista;
    private List<SuscripcionDetalleDTO> detalles;
    private long totalSuscripciones;
}
