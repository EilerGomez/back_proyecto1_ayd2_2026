/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoReportesEditor.ComentarioDetalleDTO;
import com.e.gomez.Practica1AyD2.dtoReportesEditor.*;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponseSimpleToReport;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCategoria;
import com.e.gomez.Practica1AyD2.modelos.EntidadComentario;
import com.e.gomez.Practica1AyD2.modelos.EntidadLike;
import com.e.gomez.Practica1AyD2.modelos.EntidadPagoRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.CategoriaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.ComentarioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.LikeRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PagoRevistaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RevistaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.SuscripcionRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRepositorio;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author eiler
 */
@Service
public class ReporteEditorServiceImpl implements ReporteEditorService {

    private final ComentarioRepositorio comentarioRepo;
    private final SuscripcionRepositorio suscripcionRepo;
    private final LikeRepositorio likeRepo;
    private final PagoRevistaRepositorio pagoRepo;
    private final RevistaRepositorio revistaRepo;
    private final CategoriaRepositorio categoriaRepo;
    private final UsuarioRepositorio usuarioRepo;

    public ReporteEditorServiceImpl(ComentarioRepositorio comentarioRepo, SuscripcionRepositorio suscripcionRepo, 
                                   LikeRepositorio likeRepo, PagoRevistaRepositorio pagoRepo, RevistaRepositorio revistaRepo,
                                   CategoriaRepositorio categoriaRepo,UsuarioRepositorio usuarioRepo) {
        this.comentarioRepo = comentarioRepo;
        this.suscripcionRepo = suscripcionRepo;
        this.likeRepo = likeRepo;
        this.pagoRepo = pagoRepo;
        this.revistaRepo=revistaRepo;
        this.categoriaRepo=categoriaRepo;
        this.usuarioRepo=usuarioRepo;
    }

  @Override
public List<ReporteComentariosEditorResponse> generarReporteComentarios(Integer editorId, Integer revistaId, LocalDateTime inicio, LocalDateTime fin) {
    // 1. Buscamos los comentarios (la query ya debe traer los joins si es posible)
    List<EntidadComentario> comentarios = comentarioRepo.buscarComentariosReporte(editorId, revistaId, inicio, fin);

    // 2. Agrupamos por el ID de la revista
    return comentarios.stream()
        .collect(Collectors.groupingBy(EntidadComentario::getRevistaId))
        .entrySet().stream()
        .map(entry -> {
            Integer idRevistaReal = entry.getKey(); 
            
            RevistaResponseSimpleToReport revistaDTO = this.getRevistaResponseReport(idRevistaReal);

            List<ComentarioDetalleDTO> detalles = entry.getValue().stream()
                .map(c -> new ComentarioDetalleDTO(
                    getUserNameUser(c.getUsuarioId()), 
                    c.getContenido(), 
                    c.getFechaCreacion()))
                .toList();

            return new ReporteComentariosEditorResponse(revistaDTO, detalles, (long) detalles.size());
        }).toList();
}

    @Override
    public List<ReporteSuscripcionesEditorResponse> generarReporteSuscripciones(Integer editorId, Integer revistaId, LocalDate inicio, LocalDate fin) {
        List<EntidadSuscripcion> suscripciones = suscripcionRepo.buscarSuscripcionesReporte(editorId, revistaId, inicio, fin);
        
        return suscripciones.stream()
            .collect(Collectors.groupingBy(EntidadSuscripcion::getRevistaId))
            .entrySet().stream()
            .map(entry -> {
                Integer idRevistaActual = entry.getKey(); 
                
                RevistaResponseSimpleToReport revistaDTO = this.getRevistaResponseReport(idRevistaActual);

                List<SuscripcionDetalleDTO> detalles = entry.getValue().stream()
                    .map(s -> new SuscripcionDetalleDTO(
                        getUserNameUser(s.getUsuarioId()), 
                        s.getFechaSuscripcion(), 
                        s.isActiva()
                    ))
                    .toList();

                return new ReporteSuscripcionesEditorResponse(
                    revistaDTO, 
                    detalles, 
                    (long) detalles.size()
                );
            }).toList();
    }

    @Override
    public List<ReporteLikesEditorResponse> generarReporteLikes(Integer editorId, Integer revistaId, LocalDateTime inicio, LocalDateTime fin) {
        List<EntidadLike> likes = likeRepo.buscarLikesReporte(editorId, revistaId, inicio, fin);
        
        return likes.stream()
            .collect(Collectors.groupingBy(EntidadLike::getRevistaId))
            .entrySet().stream()
            .map(entry -> {
                // Para el Top 5 solicitado, podrías ordenar esta lista resultante después
                return new ReporteLikesEditorResponse(
                    getRevistaResponseReport(entry.getKey()),
                    entry.getValue().stream()
                        .map(l -> new LikeDetalleDTO(getUserNameUser(l.getUsuarioId()), l.getFechaCreacion()))
                        .toList(),
                    (long) entry.getValue().size()
                );
            })
            .sorted((a, b) -> Long.compare(b.getTotalLikes(), a.getTotalLikes())) // Ordenar por más likes
            .limit(revistaId == null ? 5 : 100) // Si no hay filtro, aplicamos el Top 5
            .toList();
    }

    @Override
    public List<ReportePagosEditorResponse> generarReportePagos(Integer editorId, Integer revistaId, LocalDate inicio, LocalDate fin) {
        List<EntidadPagoRevista> pagos = pagoRepo.buscarPagosReporte(editorId, revistaId, inicio, fin);
        
        return pagos.stream()
            .collect(Collectors.groupingBy(EntidadPagoRevista::getRevistaId))
            .entrySet().stream()
            .map(entry -> {
                List<PagoDetalleDTO> detalles = entry.getValue().stream()
                    .map(p -> new PagoDetalleDTO(p.getMonto(), p.getFechaPago(), p.getPeriodoInicio(), p.getPeriodoFin()))
                    .toList();
                
                BigDecimal totalSuma = detalles.stream()
                    .map(PagoDetalleDTO::getMonto)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                return new ReportePagosEditorResponse(getRevistaResponseReport(entry.getKey()), detalles, totalSuma);
            }).toList();
    }
    
    private RevistaResponseSimpleToReport getRevistaResponseReport(Integer revistaId){
        EntidadRevista er = revistaRepo.findById(revistaId).orElseThrow();
        EntidadCategoria ec = categoriaRepo.getById(er.getCategoriaId());
        
        return new RevistaResponseSimpleToReport(er, ec.getNombre());
    }
    
    private String getUserNameUser(Integer userId){
        EntidadUsuario eu = usuarioRepo.getById(userId);
        return eu.getUsername();
    }
}
