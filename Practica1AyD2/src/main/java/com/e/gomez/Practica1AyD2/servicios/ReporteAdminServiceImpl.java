/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoAnuncios.TipoAnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionResponse;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoResponse;
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
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadCategoria;
import com.e.gomez.Practica1AyD2.modelos.EntidadComentario;
import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadHistorialCosto;
import com.e.gomez.Practica1AyD2.modelos.EntidadPagoRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.modelos.EntidadPrecioAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.AnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.CategoriaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.ComentarioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.CompraAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.EdicionRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.EtiquetaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.HistorialCostoRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.ImpresionAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.LikeRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PagoRevistaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
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
import java.util.ArrayList;
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
    private final PerfilRepositorio perfilRepo;
    private final HistorialCostoRepositorio historialRepo;
    
    @Autowired
    public ReporteAdminServiceImpl(PagoRevistaRepositorio pagoRepo,AnuncioRepositorio anuncioRepo, ImpresionAnuncioRepositorio impresionRepo,
             RevistaRepositorio revistaRepo,CompraAnuncioRepositorio compraRepo,PrecioAnuncioRepositorio precioAnuncioRepo, UsuarioRepositorio usuarioRepo,
             TipoAnuncioRepositorio tipoAnuncioRepo,SuscripcionRepositorio suscripcinRepo,ComentarioRepositorio comentarioRepo,
               RevistaEtiquetaRepositorio revEtiqRepo, EtiquetaRepositorio etiqRepo,EdicionRepositorio edicionRepo,LikeRepositorio likeRepo,
               CategoriaRepositorio cartegoriaRepo,PerfilRepositorio perfilService,HistorialCostoRepositorio historialRepo){
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
        this.perfilRepo=perfilService;
        this.historialRepo=historialRepo;
    }

    @Override
    public ReporteGananciasMaestroDTO reporteGanancias(LocalDate inicio, LocalDate fin) throws ExcepcionNoExiste{

        LocalDateTime inicioDT = (inicio != null) ? inicio.atStartOfDay() : null;
        LocalDateTime finDT = (fin != null) ? fin.atTime(23, 59, 59) : null;

        List<EntidadRevista> revistas = revistaRepo.findAll();
        
        List<ReporteGananciaRevistaDTO> reporteRevistas = revistas.stream().map(r -> {

            
            BigDecimal ingresosEditor = pagoRepo.sumMontoByRevista(r.getId(), inicio, fin);
            ingresosEditor = (ingresosEditor != null) ? ingresosEditor : BigDecimal.ZERO;


            

            // 5. historial de costos de esa revista
            List<HistorialCostoResponse> costos = new ArrayList<>();
            try {
                costos = historialRepo.findByRevistaId(r.getId())
                        .stream()
                        .map(c->{
                            return new HistorialCostoResponse(c);
                        })
                        .toList();
            } catch (ExcepcionNoExiste ex) {
                System.getLogger(ReporteAdminServiceImpl.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            

            return new ReporteGananciaRevistaDTO(
                    r.getId(),
                    r.getTitulo(),
                    ingresosEditor,
                    costos
            );
        }).toList();

            List<ReporteAnunciosCompradosDTO> anunciosComprados =
                    reporteAnunciosComprados(null,inicioDT, finDT);
            
            BigDecimal globalIngresosEditores = reporteRevistas.stream()
            .map(ReporteGananciaRevistaDTO::getIngresosPagosEditor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            
            BigDecimal totalMontoAnuncios = anunciosComprados.stream()
            .map(ReporteAnunciosCompradosDTO::getMontoPagado)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
       
            BigDecimal globalGanancias = globalIngresosEditores.add(totalMontoAnuncios);
            
            BigDecimal globalCostos = reporteRevistas.stream()
            .flatMap(r -> r.getCostos().stream())
            .map(HistorialCostoResponse::getCostoPorDia)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal ingresosAds = anunciosComprados
                    .stream()
                    .map(ReporteAnunciosCompradosDTO::getMontoPagado)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            ingresosAds = (ingresosAds != null) ? ingresosAds : BigDecimal.ZERO;


        return new ReporteGananciasMaestroDTO(
                reporteRevistas,
                anunciosComprados,
                globalCostos,
                globalIngresosEditores,
                globalGanancias,
                totalMontoAnuncios
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
    public List<ReporteAnunciosCompradosDTO> reporteAnunciosComprados(String tipo, LocalDateTime inicio, LocalDateTime fin) throws ExcepcionNoExiste{
        // 1. Obtener las entidades de compra filtradas
        List<EntidadCompraAnuncio> compras = compraRepo.buscarComprasConFiltros(tipo, inicio, fin);

        // 2. Transformar a DTO
        return compras.stream().map(ca -> {
            // Buscamos el anuncio
            EntidadAnuncio anuncio = anuncioRepo.findById(ca.getAnuncioId()).orElseThrow();
            EntidadPrecioAnuncio p = precioAnuncioRepo.getById(ca.getPrecioId());
            BigDecimal precio =p.getPrecio();

            EntidadUsuario eu = usuarioRepo.getById(anuncio.getAnuncianteId());
            EntidadPerfil ep = perfilRepo.getByUsuarioId(eu.getId());
            UsuarioResponse ur = new UsuarioResponse(eu,ep);
            TipoAnuncioResponse tar = new TipoAnuncioResponse(tipoAnuncioRepo.findById(anuncio.getTipoAnuncioId()).orElseThrow());
            // Construimos tu AnuncioResponse 
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
        // 1. Obtener la lista plana de anuncios desde el repositorio
        List<AnuncioCompradoDetalleDTO> todosLosAnuncios = compraRepo.listarAnunciosComprados(inicio, fin);

        // 2. Agrupar por nombre de anunciante
        Map<String, List<AnuncioCompradoDetalleDTO>> agrupado = todosLosAnuncios.stream()
                .collect(Collectors.groupingBy(AnuncioCompradoDetalleDTO::getAnunciante));

        // 3. Transformar el mapa: Aquí es donde "ya debe sumar" el monto por cada grupo
        List<DetalleAnuncianteDTO> anunciantes = agrupado.entrySet().stream().map(entry -> {
            String nombreAnunciante = entry.getKey();
            List<AnuncioCompradoDetalleDTO> listaAnuncios = entry.getValue();

            // SUMA DE MONTOS: Sumamos el montoPagado de todos los anuncios de este anunciante
            BigDecimal totalAnunciante = listaAnuncios.stream()
                    .map(AnuncioCompradoDetalleDTO::getMontoPagado)
                    .reduce(BigDecimal.ZERO, BigDecimal::add); //

            return new DetalleAnuncianteDTO(nombreAnunciante, listaAnuncios, totalAnunciante);
        }).toList();

        // 4. Calcular el Gran Total global sumando los totales de cada anunciante
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
        // Uso de Number para evitar ClassCastException en IDs
        Integer anuncioId = ((Number) row[0]).intValue();
        Integer revistaId = ((Number) row[1]).intValue();
        
        // CORRECCIÓN ArrayIndexOutOfBounds: 
        // Si row tiene solo 3 elementos, el conteo está en row[2]. 
        // Si tiene 4, el conteo está en row[3] y la URL en row[2].
        String urlMostrada = "N/A";
        Long vistas = 0L;

        if (row.length >= 4) {
            urlMostrada = String.valueOf(row[2]); 
            vistas = ((Number) row[3]).longValue();
        } else if (row.length == 3) {
            // Caso probable: row[0]=anuncioId, row[1]=revistaId, row[2]=conteo
            vistas = ((Number) row[2]).longValue();
        }

        EntidadAnuncio anuncio = anuncioRepo.findById(anuncioId).orElseThrow();
        RevistaResponse revistaResp = construirRevistaResponseCompleta(revistaId);

        return new AnuncioEfectividadDetalleDTO(
            anuncioId, 
            anuncio.getTexto(), 
            vistas, 
            urlMostrada, 
            revistaResp
        );
    }).toList();

    // Agrupación (Igual que antes)
    Map<String, List<AnuncioEfectividadDetalleDTO>> agrupado = listaPlana.stream()
        .collect(Collectors.groupingBy(dto -> {
            EntidadAnuncio a = anuncioRepo.findById(dto.getAnuncioId()).get();
            return usuarioRepo.findById(a.getAnuncianteId()).get().getUsername();
        }));

    List<AnuncianteEfectividadDTO> anunciantesDTO = agrupado.entrySet().stream()
        .map(entry -> new AnuncianteEfectividadDTO(entry.getKey(), entry.getValue()))
        .toList();

    return new ReporteEfectividadMaestroDTO(anunciantesDTO);
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

        List<EdicionResponse> edics = edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(r.getId())
                .stream()
                .map(EdicionResponse::new)
                .toList();
        
        int cantidadSuscripciones = suscripcinRepo.countByRevistaId(r.getId());
        int cantidadComentarios = comentarioRepo.countByRevistaId(r.getId());
        int cantidadLikes = likeRepo.countByRevistaId(r.getId());
        EntidadCategoria categoria = cartegoriaRepo.getById(r.getCategoriaId());
        EntidadUsuario editor = usuarioRepo.findById(r.getEditorId()).orElseThrow();
        EntidadPerfil perfil = perfilRepo.getByUsuarioId(editor.getId());
        return new RevistaResponse(r, tags, edics,cantidadComentarios,cantidadLikes,cantidadSuscripciones,categoria,editor,perfil);
    }


}
