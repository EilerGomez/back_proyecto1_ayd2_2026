/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteAnunciosCompradosDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteEfectividadMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteGananciasAnuncianteMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteGananciasMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteTopComentadasMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteTopRevistasMaestroDTO;
import com.e.gomez.Practica1AyD2.servicios.ReporteAdminService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin("*")
@RestController
@RequestMapping("/v1/reportes-admin")
public class ReporteAdminController {

    private final ReporteAdminService reporteAdminService;

    public ReporteAdminController(ReporteAdminService reporteAdminService) {
        this.reporteAdminService = reporteAdminService;
    }

    /**
     * Reporte 1: Ganancias Globales
     * @param inicio
     * @param fin
     * @return 
     */
    @GetMapping("/ganancias")
    public ResponseEntity<ReporteGananciasMaestroDTO> getReporteGanancias(
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        ReporteGananciasMaestroDTO reporte = reporteAdminService.reporteGanancias(inicio, fin);
        return ResponseEntity.ok(reporte);
    }
    /**
     * Reporte 2: Anuncios Comprados
     * @param tipo (Opcional: TEXTO, VIDEO, IMAGEN)
     * @param inicio
     * @param fin
     * @return 
     */
    @GetMapping("/anuncios-comprados")
    public ResponseEntity<List<ReporteAnunciosCompradosDTO>> getAnunciosComprados(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        
        return ResponseEntity.ok(reporteAdminService.reporteAnunciosComprados(tipo, inicio, fin));
    }
    /**
     * Reporte 3: Ganancias por Anunciante
     * @param anuncianteId
     * @param inicio
     * @param fin
     * @return 
     */
    @GetMapping("/ganancias-anunciantes")
    public ResponseEntity<ReporteGananciasAnuncianteMaestroDTO> getGananciasAnunciantes(
            @RequestParam(required = false) Integer anuncianteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        
        return ResponseEntity.ok(reporteAdminService.reporteGananciasPorAnunciante(anuncianteId, inicio, fin));
    }
    /**
     * Reporte 4: Top 5 Revistas más populares
     * @param inicio
     * @param fin
     * @return 
     */
    @GetMapping("/top-revistas")
    public ResponseEntity<ReporteTopRevistasMaestroDTO> getTopRevistas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        
        LocalDate fechaInicio = (inicio != null) ? inicio : LocalDate.of(2000, 1, 1);
        LocalDate fechaFin = (fin != null) ? fin : LocalDate.now();

        return ResponseEntity.ok(reporteAdminService.reporteTopRevistas(fechaInicio, fechaFin));
    }
    
    /**
     * Reporte 5: Top 5 Revistas más comentadas
     * @param inicio
     * @param fin
     * @return 
     */
    @GetMapping("/top-comentadas")
    public ResponseEntity<ReporteTopComentadasMaestroDTO> getTopComentadas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        
        // Si no hay fechas, tomamos un rango muy amplio (todos los registros)
        LocalDateTime fechaInicio = (inicio != null) ? inicio : LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime fechaFin = (fin != null) ? fin : LocalDateTime.now();

        return ResponseEntity.ok(reporteAdminService.reporteTopComentadas(fechaInicio, fechaFin));
    }
    
    /**
     * Reporte 6: Efectividad de Anuncios por Anunciante
     * @param inicio
     * @param fin
     * @return 
     */
    @GetMapping("/efectividad-anuncios")
    public ResponseEntity<ReporteEfectividadMaestroDTO> getEfectividad(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        
        return ResponseEntity.ok(reporteAdminService.reporteEfectividadAnuncios(inicio, fin));
    }
}