package Servicios.Reportes.Test;

import com.e.gomez.Practica1AyD2.dtoAnuncios.*;
import com.e.gomez.Practica1AyD2.dtoReportesAdmin.*;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.*;
import com.e.gomez.Practica1AyD2.repositorios.*;
import com.e.gomez.Practica1AyD2.servicios.ReporteAdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReporteAdminServiceImplTest {

    @Mock private PagoRevistaRepositorio pagoRepo;
    @Mock private AnuncioRepositorio anuncioRepo;
    @Mock private ImpresionAnuncioRepositorio impresionRepo;
    @Mock private RevistaRepositorio revistaRepo;
    @Mock private CompraAnuncioRepositorio compraRepo;
    @Mock private PrecioAnuncioRepositorio precioAnuncioRepo;
    @Mock private UsuarioRepositorio usuarioRepo;
    @Mock private TipoAnuncioRepositorio tipoAnuncioRepo;
    @Mock private SuscripcionRepositorio suscripcinRepo;
    @Mock private ComentarioRepositorio comentarioRepo;
    @Mock private RevistaEtiquetaRepositorio revEtiqRepo;
    @Mock private EtiquetaRepositorio etiqRepo;
    @Mock private EdicionRepositorio edicionRepo;
    @Mock private LikeRepositorio likeRepo;
    @Mock private CategoriaRepositorio cartegoriaRepo;
    @Mock private PerfilRepositorio perfilRepo;
    @Mock private HistorialCostoRepositorio historialRepo;

    @InjectMocks
    private ReporteAdminServiceImpl service;

    @BeforeEach
    void setUp() {
        EntidadCategoria categoria = new EntidadCategoria();
        categoria.setId(1);
        categoria.setNombre("Ciencia");

        EntidadRevista revista = new EntidadRevista();
        revista.setId(1);
        revista.setTitulo("Revista Pro");
        revista.setEditorId(10);
        revista.setCategoriaId(1);
        revista.setFechaCreacion(LocalDate.now().minusMonths(1));

        EntidadUsuario usuario = new EntidadUsuario();
        usuario.setId(10);
        usuario.setUsername("eiler_dev");

        lenient().when(revistaRepo.findById(anyInt())).thenReturn(Optional.of(revista));
        lenient().when(usuarioRepo.findById(anyInt())).thenReturn(Optional.of(usuario));
        lenient().when(usuarioRepo.getById(anyInt())).thenReturn(usuario);
        lenient().when(perfilRepo.getByUsuarioId(anyInt())).thenReturn(new EntidadPerfil());
        lenient().when(cartegoriaRepo.getById(anyInt())).thenReturn(categoria);
        
        lenient().when(suscripcinRepo.countByRevistaId(anyInt())).thenReturn(5);
        lenient().when(comentarioRepo.countByRevistaId(anyInt())).thenReturn(3);
        lenient().when(likeRepo.countByRevistaId(anyInt())).thenReturn(10);
    }

    @Test
    void reporteGanancias_DeberiaCalcularTotalesCorrectamente() throws Exception {
        EntidadRevista r = new EntidadRevista();
        r.setId(1);
        r.setCategoriaId(1);
        
        when(revistaRepo.findAll()).thenReturn(List.of(r));
        when(pagoRepo.sumMontoByRevista(anyInt(), any(), any())).thenReturn(new BigDecimal("500.00"));
        
        EntidadCompraAnuncio ca = new EntidadCompraAnuncio();
        ca.setAnuncioId(1);
        ca.setPrecioId(1);
        when(compraRepo.buscarComprasConFiltros(any(), any(), any())).thenReturn(List.of(ca));
        
        EntidadTipoAnuncio tipoAnuncio = new EntidadTipoAnuncio(1, "CODIGO", "descripcion");
        
        EntidadAnuncio anuncio = new EntidadAnuncio();
        anuncio.setId(1);
        anuncio.setAnuncianteId(10);
        anuncio.setTipoAnuncioId(1);
        when(anuncioRepo.findById(anyInt())).thenReturn(Optional.of(anuncio));
        
        EntidadPrecioAnuncio precio = new EntidadPrecioAnuncio();
        precio.setPrecio(new BigDecimal("100.00"));
        when(precioAnuncioRepo.getById(anyInt())).thenReturn(precio);
        when(tipoAnuncioRepo.findById(anyInt())).thenReturn(Optional.of(tipoAnuncio));

        ReporteGananciasMaestroDTO reporte = service.reporteGanancias(LocalDate.now(), LocalDate.now());

        assertNotNull(reporte);
        assertNotNull(reporte.getTotalGanancias());
    }

    @Test
    void reporteEfectividadAnuncios_DeberiaManejarResultadosNativos() {
        Object[] row = {1, 1, "www.portal.com", 150L};
        when(impresionRepo.contarVistasPorAnuncioYRevista(any(), any()))
            .thenReturn(List.<Object[]>of(row));        

        EntidadAnuncio a = new EntidadAnuncio();
        a.setAnuncianteId(10);
        a.setTexto("Promo de Verano");
        when(anuncioRepo.findById(anyInt())).thenReturn(Optional.of(a));

        ReporteEfectividadMaestroDTO reporte = service.reporteEfectividadAnuncios(null, null);

        assertNotNull(reporte);
        assertFalse(reporte.getAnunciantes().isEmpty());
    }

    @Test
    void reporteEfectividadAnuncios_CasoBordeIndex3OutOfBounds() {
        Object[] row = {1, 1, 300L};
        when(impresionRepo.contarVistasPorAnuncioYRevista(any(), any()))
            .thenReturn(List.<Object[]>of(row)); 
        
        EntidadAnuncio a = new EntidadAnuncio();
        a.setAnuncianteId(10);
        when(anuncioRepo.findById(anyInt())).thenReturn(Optional.of(a));

        ReporteEfectividadMaestroDTO reporte = service.reporteEfectividadAnuncios(null, null);

        assertNotNull(reporte);
        
        assertEquals(300L, reporte.getAnunciantes().get(0).getDetalleAnuncios().get(0).getCantidadVistas());
    }

    @Test
    void reporteGananciasPorAnunciante_DeberiaAgruparYSumar() {
        AnuncioCompradoDetalleDTO d1 = new AnuncioCompradoDetalleDTO(1, "VIDEO", "eiler_dev", new BigDecimal("100.00"), LocalDateTime.now());
        AnuncioCompradoDetalleDTO d2 = new AnuncioCompradoDetalleDTO(2, "TEXTO", "eiler_dev", new BigDecimal("50.00"), LocalDateTime.now());
        
        when(compraRepo.listarAnunciosComprados(any(), any())).thenReturn(List.of(d1, d2));

        ReporteGananciasAnuncianteMaestroDTO reporte = service.reporteGananciasPorAnunciante(null, null, null);

        assertEquals(1, reporte.getAnunciantes().size());
        assertEquals(new BigDecimal("150.00"), reporte.getAnunciantes().get(0).getTotalInvertido());
    }

    @Test
    void calcularCostoOperativo_DeberiaRetornarMontoCorrecto() {
        EntidadPagoRevista pago = new EntidadPagoRevista();
        pago.setMonto(new BigDecimal("10.00"));
        
        when(pagoRepo.buscarPorRevistaYPeriodo(anyInt(), any(), any())).thenReturn(List.of(pago));
        
        BigDecimal costo = (BigDecimal) ReflectionTestUtils.invokeMethod(service, "calcularCostoOperativo", 
                1, LocalDate.of(2026, 3, 1), LocalDate.of(2026, 3, 5));

        assertEquals(0, new BigDecimal("50.00").compareTo(costo));
    }
    
    @Test
    void reporteTopRevistas_DeberiaMapearRevistasPopulares() {
        when(suscripcinRepo.findTop5RevistasIds(any(), any(), any(Pageable.class)))
                .thenReturn(List.of(1));
        
        EntidadSuscripcion suscripcion = new EntidadSuscripcion();
        suscripcion.setUsuarioId(10);
        when(suscripcinRepo.findByRevistaIdAndFechaSuscripcionBetween(eq(1), any(), any()))
                .thenReturn(List.of(suscripcion));

        ReporteTopRevistasMaestroDTO reporte = service.reporteTopRevistas(LocalDate.now(), LocalDate.now());

        assertNotNull(reporte);
        assertEquals(1, reporte.getTopRevistas().size());
        assertEquals("Revista Pro", reporte.getTopRevistas().get(0).getTitulo());
    }

    @Test
    void reporteTopComentadas_DeberiaMapearRevistasYComentarios() {
        when(comentarioRepo.findTop5RevistasMasComentadas(any(), any(), any(Pageable.class)))
                .thenReturn(List.of(1));
        
        EntidadComentario comentario = new EntidadComentario();
        comentario.setUsuarioId(10);
        comentario.setContenido("Excelente revista");
        comentario.setFechaCreacion(LocalDateTime.now());
        
        when(comentarioRepo.findByRevistaIdAndFechaCreacionBetween(eq(1), any(), any()))
                .thenReturn(List.of(comentario));

        ReporteTopComentadasMaestroDTO reporte = service.reporteTopComentadas(LocalDateTime.now(), LocalDateTime.now());

        assertNotNull(reporte);
        assertEquals(1, reporte.getTopRevistas().size());
        assertEquals("eiler_dev", reporte.getTopRevistas().get(0).getComentarios().get(0).getUsername());
    }
    
    @Test
    void construirRevistaResponseCompleta_DeberiaCubrirTodasLasLineas() {
        EntidadRevistaEtiqueta re = new EntidadRevistaEtiqueta();
        RevistaEtiquetaId id = new RevistaEtiquetaId(1, 1);
        re.setId(id);
        when(revEtiqRepo.findById_RevistaId(anyInt())).thenReturn(List.of(re));
        when(etiqRepo.findAllById(any())).thenReturn(List.of(new EntidadEtiqueta(1, "Java")));

        // Mock de ediciones
        EntidadEdicion edicion = new EntidadEdicion();
        edicion.setRevistaId(1);
        edicion.setId(1);
        edicion.setFechaPublicacion(LocalDateTime.now());
        when(edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(anyInt()))
                .thenReturn(List.of(edicion));

        Object[] row = {1, 1, 10L};
        when(impresionRepo.contarVistasPorAnuncioYRevista(any(), any())).thenReturn(List.<Object[]>of(row));
        
        EntidadAnuncio anuncio = new EntidadAnuncio();
        anuncio.setId(1);
        anuncio.setAnuncianteId(10);
        when(anuncioRepo.findById(anyInt())).thenReturn(Optional.of(anuncio));

        ReporteEfectividadMaestroDTO reporte = service.reporteEfectividadAnuncios(LocalDateTime.now(), LocalDateTime.now());

        assertNotNull(reporte);
    }
    
    @Test
    void reporteGanancias_DeberiaManejarExcepcionEnHistorial() throws ExcepcionNoExiste {
        EntidadRevista r = new EntidadRevista();
        r.setId(1);
        when(revistaRepo.findAll()).thenReturn(List.of(r));
        
        when(historialRepo.findByRevistaId(anyInt())).thenThrow(new ExcepcionNoExiste("No hay historial"));

        assertDoesNotThrow(() -> {
            service.reporteGanancias(LocalDate.now(), LocalDate.now());
        });
    }
}