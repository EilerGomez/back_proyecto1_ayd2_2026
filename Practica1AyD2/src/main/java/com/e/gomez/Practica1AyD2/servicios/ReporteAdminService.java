/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteAnunciosCompradosDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteEfectividadMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteGananciasAnuncianteMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteGananciasMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteTopComentadasMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteTopRevistasMaestroDTO;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author eiler
 */
public interface ReporteAdminService {
    // 1. Ganancias (Revista, Costo, Anuncios, Pagos, Totales)
    ReporteGananciasMaestroDTO reporteGanancias(LocalDate inicio, LocalDate fin)throws ExcepcionNoExiste;

    List<ReporteAnunciosCompradosDTO> reporteAnunciosComprados(String tipo, LocalDateTime inicio, LocalDateTime fin) throws ExcepcionNoExiste;
    ReporteGananciasAnuncianteMaestroDTO reporteGananciasPorAnunciante(Integer anuncianteId, LocalDateTime inicio, LocalDateTime fin);
    ReporteTopRevistasMaestroDTO reporteTopRevistas(LocalDate inicio, LocalDate fin);
    ReporteTopComentadasMaestroDTO reporteTopComentadas(LocalDateTime inicio, LocalDateTime fin);
    ReporteEfectividadMaestroDTO reporteEfectividadAnuncios(LocalDateTime inicio, LocalDateTime fin);
}
