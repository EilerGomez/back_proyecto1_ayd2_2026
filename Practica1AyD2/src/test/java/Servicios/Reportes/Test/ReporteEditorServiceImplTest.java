package Servicios.Reportes.Test;

import com.e.gomez.Practica1AyD2.dtoReportesEditor.*;
import com.e.gomez.Practica1AyD2.modelos.*;
import com.e.gomez.Practica1AyD2.repositorios.*;
import com.e.gomez.Practica1AyD2.servicios.ReporteEditorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReporteEditorServiceImplTest {

    @Mock private ComentarioRepositorio comentarioRepo;
    @Mock private SuscripcionRepositorio suscripcionRepo;
    @Mock private LikeRepositorio likeRepo;
    @Mock private PagoRevistaRepositorio pagoRepo;
    @Mock private RevistaRepositorio revistaRepo;
    @Mock private CategoriaRepositorio categoriaRepo;
    @Mock private UsuarioRepositorio usuarioRepo;

    @InjectMocks
    private ReporteEditorServiceImpl service;

    private EntidadRevista revista;
    private EntidadCategoria categoria;
    private EntidadUsuario usuario;

    @BeforeEach
    void setUp() {
        revista = new EntidadRevista();
        revista.setId(1);
        revista.setTitulo("Revista de Ciencia");
        revista.setCategoriaId(10);

        categoria = new EntidadCategoria();
        categoria.setId(10);
        categoria.setNombre("Ciencia");

        usuario = new EntidadUsuario();
        usuario.setId(5);
        usuario.setUsername("usuario_test");

        lenient().when(revistaRepo.findById(anyInt())).thenReturn(Optional.of(revista));
        lenient().when(categoriaRepo.getById(anyInt())).thenReturn(categoria);
        lenient().when(usuarioRepo.getById(anyInt())).thenReturn(usuario);
    }

    @Test
    void generarReporteComentarios_DeberiaAgruparPorRevista() {
        EntidadComentario c = new EntidadComentario();
        c.setRevistaId(1);
        c.setUsuarioId(5);
        c.setContenido("Excelente");
        c.setFechaCreacion(LocalDateTime.now());

        when(comentarioRepo.buscarComentariosReporte(anyInt(), any(), any(), any()))
                .thenReturn(List.of(c));

        List<ReporteComentariosEditorResponse> reporte = service.generarReporteComentarios(1, null, null, null);

        assertFalse(reporte.isEmpty());
        assertEquals(1, reporte.get(0).getTotalComentarios());
    }

    @Test
    void generarReporteSuscripciones_DeberiaMapearDetalles() {
        EntidadSuscripcion s = new EntidadSuscripcion();
        s.setRevistaId(1);
        s.setUsuarioId(5);
        s.setActiva(true);
        s.setFechaSuscripcion(LocalDate.now());

        when(suscripcionRepo.buscarSuscripcionesReporte(anyInt(), any(), any(), any()))
                .thenReturn(List.of(s));

        List<ReporteSuscripcionesEditorResponse> reporte = service.generarReporteSuscripciones(1, null, null, null);

        assertNotNull(reporte);
        assertEquals(1, reporte.size());
    }

    @Test
    void generarReporteLikes_DeberiaOrdenarPorTop5() {
        EntidadLike l1 = new EntidadLike(); l1.setRevistaId(1); l1.setUsuarioId(5);
        EntidadLike l2 = new EntidadLike(); l2.setRevistaId(2); l2.setUsuarioId(5);
        EntidadLike l3 = new EntidadLike(); l3.setRevistaId(2); l3.setUsuarioId(5); 

        when(likeRepo.buscarLikesReporte(anyInt(), isNull(), any(), any()))
                .thenReturn(List.of(l1, l2, l3));

        List<ReporteLikesEditorResponse> reporte = service.generarReporteLikes(1, null, null, null);

        assertEquals(2, reporte.size());
        assertEquals(2, reporte.get(0).getTotalLikes()); 
    }

    @Test
    void generarReportePagos_DeberiaSumarTotalBigDecimal() {
        EntidadPagoRevista p1 = new EntidadPagoRevista();
        p1.setRevistaId(1);
        p1.setMonto(new BigDecimal("100.00"));
        p1.setFechaPago(LocalDate.now());

        when(pagoRepo.buscarPagosReporte(anyInt(), any(), any(), any()))
                .thenReturn(List.of(p1));

        List<ReportePagosEditorResponse> reporte = service.generarReportePagos(1, null, null, null);

        assertEquals(new BigDecimal("100.00"), reporte.get(0).getSumaMontoTotal());
    }
}