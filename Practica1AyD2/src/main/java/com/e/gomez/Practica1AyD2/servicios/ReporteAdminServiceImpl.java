/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoAnuncios.TipoAnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionResponse;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.AnuncianteEfectividadDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.AnuncioCompradoDetalleDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.AnuncioEfectividadDetalleDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteAnunciosCompradosDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteGananciaRevistaDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteGananciasAnuncianteMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteGananciasMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.DetalleAnuncianteDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteEfectividadMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteTopComentadasMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.ReporteTopRevistasMaestroDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.RevistaComentadaDTO;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.RevistaPopularDTO;

import com.e.gomez.Practica1AyD2.dtoReportesEditor.ComentarioDetalleDTO;
import com.e.gomez.Practica1AyD2.dtoReportesEditor.SuscripcionDetalleDTO;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;

import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadCategoria;
import com.e.gomez.Practica1AyD2.modelos.EntidadComentario;
import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadPagoRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.AnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.CategoriaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.ComentarioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.CompraAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.EdicionRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.EtiquetaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.ImpresionAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.LikeRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PagoRevistaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PrecioAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RevistaEtiquetaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RevistaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.SuscripcionRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.TipoAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRepositorio;
import org.springframework.data.domain.Pageable; // El correcto
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author eiler
 */
@Service
public class ReporteAdminServiceImpl implements ReporteAdminService{
    private final PagoRevistaRepositorio pagoRepo;
    private final AnuncioRepositorio anuncioRepo;
    private final ImpresionAnuncioRepositorio impresionRepo;
    private final RevistaRepositorio revistaRepo;
    private final CompraAnuncioRepositorio compraRepo;
    private final PrecioAnuncioRepositorio precioAnuncioRepo;
    private final UsuarioRepositorio usuarioRepo;
    private final TipoAnuncioRepositorio tipoAnuncioRepo;
    private final SuscripcionRepositorio suscripcinRepo;
    private final ComentarioRepositorio comentarioRepo;
    private final RevistaEtiquetaRepositorio revEtiqRepo;
    private final EtiquetaRepositorio etiqRepo;
    private final EdicionRepositorio edicionRepo;
    private final LikeRepositorio likeRepo;
    private final CategoriaRepositorio cartegoriaRepo;
    
    @Autowired
    public ReporteAdminServiceImpl(PagoRevistaRepositorio pagoRepo,AnuncioRepositorio anuncioRepo, ImpresionAnuncioRepositorio impresionRepo,
             RevistaRepositorio revistaRepo,CompraAnuncioRepositorio compraRepo,PrecioAnuncioRepositorio precioAnuncioRepo, UsuarioRepositorio usuarioRepo,
             TipoAnuncioRepositorio tipoAnuncioRepo,SuscripcionRepositorio suscripcinRepo,ComentarioRepositorio comentarioRepo,
               RevistaEtiquetaRepositorio revEtiqRepo, EtiquetaRepositorio etiqRepo,EdicionRepositorio edicionRepo,LikeRepositorio likeRepo,
               CategoriaRepositorio cartegoriaRepo){
        this.pagoRepo=pagoRepo;
        this.anuncioRepo=anuncioRepo;
        this.impresionRepo=impresionRepo;
        this.revistaRepo=revistaRepo;
        this.compraRepo=compraRepo;
        this.precioAnuncioRepo=precioAnuncioRepo;
        this.usuarioRepo=usuarioRepo;
        this.tipoAnuncioRepo=tipoAnuncioRepo;
        this.suscripcinRepo=suscripcinRepo;
        this.comentarioRepo=comentarioRepo;
        this.revEtiqRepo=revEtiqRepo;
        this.etiqRepo=etiqRepo;
        this.edicionRepo=edicionRepo;
        this.likeRepo=likeRepo;
        this.cartegoriaRepo=cartegoriaRepo;
    }

    @Override
    public ReporteGananciasMaestroDTO reporteGanancias(LocalDate inicio, LocalDate fin) {
       
        LocalDateTime inicioDT = (inicio != null) ? inicio.atStartOfDay() : null;
        LocalDateTime finDT = (fin != null) ? fin.atTime(23, 59, 59) : null;

        
        List<AnuncioCompradoDetalleDTO> anuncios = compraRepo.listarAnunciosComprados(inicioDT, finDT);

        
        List<EntidadRevista> revistas = revistaRepo.findAll();
        List<ReporteGananciaRevistaDTO> reporteRevistas = revistas.stream().map(r -> {
            // Ingresos de Editores
            BigDecimal ingresosEditor = pagoRepo.sumMontoByRevista(r.getId(), inicio, fin);
            ingresosEditor = (ingresosEditor != null) ? ingresosEditor : BigDecimal.ZERO;

            
            BigDecimal ingresosAds = impresionRepo.sumIngresosAnunciosPorRevista(r.getId(), inicioDT, finDT);
            ingresosAds = (ingresosAds != null) ? ingresosAds : BigDecimal.ZERO;

            
            BigDecimal costo = calcularCostoOperativo(r.getId(), inicio, fin);

            BigDecimal totalIngreso = ingresosEditor.add(ingresosAds);
            return new ReporteGananciaRevistaDTO(
                r.getId(), r.getTitulo(), ingresosEditor, ingresosAds, costo, 
                totalIngreso, totalIngreso.subtract(costo)
            );
        }).toList();

        
        BigDecimal globalIngresos = reporteRevistas.stream().map(ReporteGananciaRevistaDTO::getTotalIngreso).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal globalCostos = reporteRevistas.stream().map(ReporteGananciaRevistaDTO::getCostoMantenimiento).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReporteGananciasMaestroDTO(
            reporteRevistas, 
            anuncios, 
            globalIngresos, 
            globalCostos, 
            globalIngresos.subtract(globalCostos)
        );
    }
    
    private BigDecimal calcularCostoOperativo(Integer revistaId, LocalDate inicio, LocalDate fin) {
        LocalDate fechaDesde = (inicio != null) ? inicio : 
            revistaRepo.findById(revistaId).map(r -> r.getFechaCreacion()).orElse(LocalDate.now());
        LocalDate fechaHasta = (fin != null) ? fin : LocalDate.now();

       
        List<EntidadPagoRevista> pagos = pagoRepo.buscarPorRevistaYPeriodo(revistaId, inicio, fin);

        BigDecimal costoDiario;
        if (pagos == null || pagos.isEmpty()) {
            costoDiario = BigDecimal.ZERO; 
        } else {
            costoDiario = pagos.get(0).getMonto();
        }
        
        long dias = java.time.temporal.ChronoUnit.DAYS.between(fechaDesde, fechaHasta) + 1;
        if (dias < 0) dias = 0;

        return costoDiario.multiply(new BigDecimal(dias));
    }

    @Override
    public List<ReporteAnunciosCompradosDTO> reporteAnunciosComprados(String tipo, LocalDateTime inicio, LocalDateTime fin) {
        // 1. Obtener las entidades de compra filtradas
        List<EntidadCompraAnuncio> compras = compraRepo.buscarComprasConFiltros(tipo, inicio, fin);

        // 2. Transformar a DTO
        return compras.stream().map(ca -> {
            // Buscamos el anuncio
            EntidadAnuncio anuncio = anuncioRepo.findById(ca.getAnuncioId()).orElseThrow();
            
            // Obtenemos el precio (monto pagado) de la tabla de precios
            BigDecimal precio = precioAnuncioRepo.findByTipoAnuncioIdAndActivoTrue(anuncio.getTipoAnuncioId())
                    .map(p -> p.getPrecio()).orElse(BigDecimal.ZERO);

            EntidadUsuario eu = usuarioRepo.getById(anuncio.getAnuncianteId());
            UsuarioResponse ur = new UsuarioResponse(eu);
            TipoAnuncioResponse tar = new TipoAnuncioResponse(tipoAnuncioRepo.findById(anuncio.getTipoAnuncioId()).orElseThrow());
            // Construimos tu AnuncioResponse (Necesitas tus metodos helpers para Usuario y Tipo)
            AnuncioResponse anuncioResp = new AnuncioResponse(
                anuncio,
                ur, 
                tar
            );

            return new ReporteAnunciosCompradosDTO(
                anuncioResp,
                ca.getFechaInicio(),
                ca.getFechaFin(),
                precio,
                ca.getEstado()
            );
        }).toList();
    }
    @Override
    public ReporteGananciasAnuncianteMaestroDTO reporteGananciasPorAnunciante(Integer anuncianteId, LocalDateTime inicio, LocalDateTime fin) {
        List<AnuncioCompradoDetalleDTO> todosLosAnuncios = compraRepo.listarAnunciosComprados(inicio, fin);

        Map<String, List<AnuncioCompradoDetalleDTO>> agrupado = todosLosAnuncios.stream()
                .collect(Collectors.groupingBy(AnuncioCompradoDetalleDTO::getAnunciante));

        List<DetalleAnuncianteDTO> anunciantes = agrupado.entrySet().stream().map(entry -> {
            BigDecimal totalAnunciante = entry.getValue().stream()
                    .map(AnuncioCompradoDetalleDTO::getMontoPagado)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return new DetalleAnuncianteDTO(entry.getKey(), entry.getValue(), totalAnunciante);
        }).toList();

        
        BigDecimal granTotal = anunciantes.stream()
                .map(DetalleAnuncianteDTO::getTotalInvertido)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ReporteGananciasAnuncianteMaestroDTO(anunciantes, granTotal);
    }
    
    @Override // cuarto reporte
    public ReporteTopRevistasMaestroDTO reporteTopRevistas(LocalDate inicio, LocalDate fin) {
        List<Integer> topIds = suscripcinRepo.findTop5RevistasIds(inicio, fin, (Pageable) PageRequest.of(0, 5));

        List<RevistaPopularDTO> detalles = topIds.stream().map(id -> {
            EntidadRevista revista = revistaRepo.findById(id).orElseThrow();

            List<EntidadSuscripcion> suscripcionesEntidad = 
                suscripcinRepo.findByRevistaIdAndFechaSuscripcionBetween(id, inicio, fin);

            List<SuscripcionDetalleDTO> detallesSuscrip = suscripcionesEntidad.stream()
                .map(s -> {
                    EntidadUsuario lector = usuarioRepo.findById(s.getUsuarioId()).orElseThrow();
                    return new SuscripcionDetalleDTO(lector, s);
                }).toList();

            return new RevistaPopularDTO(revista,detallesSuscrip);
        }).toList();

        return new ReporteTopRevistasMaestroDTO(detalles);
    }
    
    @Override
    public ReporteTopComentadasMaestroDTO reporteTopComentadas(LocalDateTime inicio, LocalDateTime fin) {
        List<Integer> topIds = comentarioRepo.findTop5RevistasMasComentadas(
            inicio, fin, (Pageable)PageRequest.of(0, 5)
        );

        List<RevistaComentadaDTO> detalles = topIds.stream().map(id -> {
            EntidadRevista revista = revistaRepo.findById(id).orElseThrow();
            
            List<EntidadComentario> comentariosEntidad = 
                comentarioRepo.findByRevistaIdAndFechaCreacionBetween(id, inicio, fin);

            List<ComentarioDetalleDTO> detalleComentarios = comentariosEntidad.stream()
                .map(c -> {
                    String lector = usuarioRepo.findById(c.getUsuarioId())
                        .map(u -> u.getUsername()).orElse("Anónimo");
                    return new ComentarioDetalleDTO(lector, c.getContenido(), c.getFechaCreacion());
                }).toList();

            return new RevistaComentadaDTO(
                id, 
                revista.getTitulo(), 
                (long) detalleComentarios.size(), 
                detalleComentarios
            );
        }).toList();

        return new ReporteTopComentadasMaestroDTO(detalles);
    }
    
    @Override
    public ReporteEfectividadMaestroDTO reporteEfectividadAnuncios(LocalDateTime inicio, LocalDateTime fin) {
        List<Object[]> resultados = impresionRepo.contarVistasPorAnuncioYRevista(inicio, fin);

        List<AnuncioEfectividadDetalleDTO> listaPlana = resultados.stream().map(row -> {
            Integer anuncioId = (Integer) row[0];
            Integer revistaId = (Integer) row[1];
            Long vistas = (Long) row[2];

            EntidadAnuncio anuncio = anuncioRepo.findById(anuncioId).orElseThrow();

            RevistaResponse revistaResp = construirRevistaResponseCompleta(revistaId);

            return new AnuncioEfectividadDetalleDTO(anuncioId, anuncio.getTexto(), vistas, revistaResp);
        }).toList();

        Map<String, List<AnuncioEfectividadDetalleDTO>> agrupado = listaPlana.stream()
            .collect(Collectors.groupingBy(dto -> {
                Integer anuncianteId = anuncioRepo.findById(dto.getAnuncioId()).get().getAnuncianteId();
                return usuarioRepo.findById(anuncianteId).get().getUsername();
            }));

        List<AnuncianteEfectividadDTO> anunciantes = agrupado.entrySet().stream()
            .map(entry -> new AnuncianteEfectividadDTO(entry.getKey(), entry.getValue()))
            .toList();

        return new ReporteEfectividadMaestroDTO(anunciantes);
    }

    private RevistaResponse construirRevistaResponseCompleta(Integer revistaId) {
        EntidadRevista r = revistaRepo.findById(revistaId).orElseThrow();
        List<Integer> idsEtiquetas = revEtiqRepo.findById_RevistaId(r.getId())
                .stream()
                .map(re -> re.getId().getEtiquetaId())
                .toList();
        
        List<EtiquetaResponse> tags = etiqRepo.findAllById(idsEtiquetas)
                .stream()
                .map(EtiquetaResponse::new)
                .toList();

        //  Obtener todas las ediciones de esta revista
        List<EdicionResponse> edics = edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(r.getId())
                .stream()
                .map(EdicionResponse::new)
                .toList();
        
        int cantidadSuscripciones = suscripcinRepo.countByRevistaId(r.getId());
        int cantidadComentarios = comentarioRepo.countByRevistaId(r.getId());
        int cantidadLikes = likeRepo.countByRevistaId(r.getId());
        EntidadCategoria categoria = cartegoriaRepo.getById(r.getCategoriaId());
        EntidadUsuario editor = usuarioRepo.findById(r.getEditorId()).orElseThrow();
        return new RevistaResponse(r, tags, edics,cantidadComentarios,cantidadLikes,cantidadSuscripciones,categoria,editor);
    }
}
