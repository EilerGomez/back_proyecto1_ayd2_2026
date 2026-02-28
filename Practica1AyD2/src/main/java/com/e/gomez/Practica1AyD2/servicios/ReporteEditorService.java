/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoReportesEditor.ReporteComentariosEditorResponse;
import com.e.gomez.Practica1AyD2.dtoReportesEditor.ReporteLikesEditorResponse;
import com.e.gomez.Practica1AyD2.dtoReportesEditor.ReportePagosEditorResponse;
import com.e.gomez.Practica1AyD2.dtoReportesEditor.ReporteSuscripcionesEditorResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReporteEditorService {
    List<ReporteComentariosEditorResponse> generarReporteComentarios(Integer editorId, Integer revistaId, LocalDateTime inicio, LocalDateTime fin) throws ExcepcionNoExiste;
    List<ReporteSuscripcionesEditorResponse> generarReporteSuscripciones(Integer editorId, Integer revistaId, LocalDate inicio, LocalDate fin);
    List<ReporteLikesEditorResponse> generarReporteLikes(Integer editorId, Integer revistaId, LocalDateTime inicio, LocalDateTime fin);
    List<ReportePagosEditorResponse> generarReportePagos(Integer editorId, Integer revistaId, LocalDate inicio, LocalDate fin);
}