/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoReportesEditor.*;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.ReporteEditorService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/reportes-editor")
public class ReporteEditorController {

    private final ReporteEditorService reporteService;

    public ReporteEditorController(ReporteEditorService reporteService) {
        this.reporteService = reporteService;
    }

    // Reporte de Comentarios
    @GetMapping("/comentarios/{editorId}")
    public ResponseEntity<List<ReporteComentariosEditorResponse>> getComentarios(
            @PathVariable Integer editorId,
            @RequestParam(required = false) Integer revistaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) throws ExcepcionNoExiste {
        
        return ResponseEntity.ok(reporteService.generarReporteComentarios(editorId, revistaId, inicio, fin));
    }

    // Reporte de Suscripciones
    @GetMapping("/suscripciones/{editorId}")
    public ResponseEntity<List<ReporteSuscripcionesEditorResponse>> getSuscripciones(
            @PathVariable Integer editorId,
            @RequestParam(required = false) Integer revistaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        return ResponseEntity.ok(reporteService.generarReporteSuscripciones(editorId, revistaId, inicio, fin));
    }

    // Reporte de las 5 Revistas más gustadas (Likes)
    @GetMapping("/likes-top/{editorId}")
    public ResponseEntity<List<ReporteLikesEditorResponse>> getTopLikes(
            @PathVariable Integer editorId,
            @RequestParam(required = false) Integer revistaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        
        return ResponseEntity.ok(reporteService.generarReporteLikes(editorId, revistaId, inicio, fin));
    }

    //Reporte de Pagos hechos por el editor
    @GetMapping("/pagos/{editorId}")
    public ResponseEntity<List<ReportePagosEditorResponse>> getPagos(
            @PathVariable Integer editorId,
            @RequestParam(required = false) Integer revistaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        return ResponseEntity.ok(reporteService.generarReportePagos(editorId, revistaId, inicio, fin));
    }
}
