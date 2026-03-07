package Servicios.Revistas.Test;

import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.RevistaEtiquetasRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.mantenimiento.MantenimientoSistemaService;
import com.e.gomez.Practica1AyD2.modelos.*;
import com.e.gomez.Practica1AyD2.repositorios.*;
import com.e.gomez.Practica1AyD2.servicios.RevistaServiceImpl;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RevistaServiceImplTest {

    @Mock
    private RevistaRepositorio repo;
    @Mock
    private RevistaEtiquetaRepositorio revEtiqRepo;
    @Mock
    private EtiquetaRepositorio etiqRepo;
    @Mock
    private EdicionRepositorio edicionRepo;
    @Mock
    private SuscripcionRepositorio suscripcionRepo;
    @Mock
    private LikeRepositorio likeRepo;
    @Mock
    private ComentarioRepositorio comentarioRepo;
    @Mock
    private CategoriaRepositorio categoriaRepo;
    @Mock
    private UsuarioRepositorio usuarioRepo;
    @Mock
    private PerfilRepositorio perfilRepo; // Mock añadido

    @InjectMocks
    private RevistaServiceImpl service;
    
    @Mock
    private MantenimientoSistemaService mantenimiento;

    private EntidadRevista revistaEjemplo;
    private RevistaRequest requestEjemplo;
    private EntidadCategoria categoriaEjemplo;
    private EntidadUsuario usuarioEjemplo;
    private EntidadPerfil perfilEjemplo; // Objeto para el mock

    @BeforeEach
    void setUp() {
        revistaEjemplo = new EntidadRevista();
        revistaEjemplo.setId(1);
        revistaEjemplo.setTitulo("Revista Test");
        revistaEjemplo.setEditorId(10);
        revistaEjemplo.setCategoriaId(1);
        revistaEjemplo.setDescripcion("Descripción de prueba");

        requestEjemplo = new RevistaRequest();
        requestEjemplo.setTitulo("Revista Test");
        requestEjemplo.setEditorId(10);
        requestEjemplo.setCategoriaId(1);

        categoriaEjemplo = new EntidadCategoria();
        categoriaEjemplo.setId(1);
        categoriaEjemplo.setNombre("Tecnología");

        usuarioEjemplo = new EntidadUsuario();
        usuarioEjemplo.setId(10);
        usuarioEjemplo.setUsername("username");

        perfilEjemplo = new EntidadPerfil();
        perfilEjemplo.setUsuarioId(10);
        perfilEjemplo.setFoto_url("http://test.com/foto.jpg");
    }

    private void configurarMocksMapeo() {
        // Mapeo básico
        when(revEtiqRepo.findById_RevistaId(anyInt())).thenReturn(new ArrayList<>());
        when(edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(anyInt())).thenReturn(new ArrayList<>());
        
        // Contadores
        when(suscripcionRepo.countByRevistaId(anyInt())).thenReturn(0);
        when(likeRepo.countByRevistaId(anyInt())).thenReturn(0);
        when(comentarioRepo.countByRevistaId(anyInt())).thenReturn(0);

        // Relaciones
        when(categoriaRepo.getById(anyInt())).thenReturn(categoriaEjemplo);
        when(usuarioRepo.findById(anyInt())).thenReturn(Optional.of(usuarioEjemplo));
        
        // Perfil (Vital para evitar NullPointerException)
        when(perfilRepo.getByUsuarioId(anyInt())).thenReturn(perfilEjemplo);
    }

    @Test
    void crear_DeberiaGuardar_CuandoTituloNoExiste() throws ExcepcionEntidadDuplicada {
        when(repo.existsByTitulo(anyString())).thenReturn(false);
        when(repo.save(any(EntidadRevista.class))).thenReturn(revistaEjemplo);
        configurarMocksMapeo();

        RevistaResponse resultado = service.crear(requestEjemplo);

        assertNotNull(resultado);
        assertEquals("Revista Test", resultado.getTitulo());
        verify(repo).save(any(EntidadRevista.class));
    }

    @Test
    void crear_DeberiaLanzarExcepcion_CuandoTituloYaExiste() {
        when(repo.existsByTitulo(requestEjemplo.getTitulo())).thenReturn(true);

        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.crear(requestEjemplo));
    }

    @Test
    void getById_DeberiaRetornarRevista_SiExiste() throws ExcepcionNoExiste {
        when(repo.findById(1)).thenReturn(Optional.of(revistaEjemplo));
        configurarMocksMapeo();

        RevistaResponse resultado = service.getById(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void actualizar_DeberiaModificar_SiEsValido() throws Exception {
        when(repo.findById(1)).thenReturn(Optional.of(revistaEjemplo));
        when(repo.existeTituloEnOtraRevista(anyString(), anyInt())).thenReturn(false);
        when(repo.save(any(EntidadRevista.class))).thenReturn(revistaEjemplo);
        configurarMocksMapeo();

        RevistaResponse resultado = service.actualizar(1, requestEjemplo);

        assertNotNull(resultado);
        verify(repo).save(revistaEjemplo);
    }

    @Test
    void eliminar_DeberiaLlamarDelete_SiExiste() throws ExcepcionNoExiste {
        when(repo.existsById(1)).thenReturn(true);
        service.eliminar(1);
        verify(repo).deleteById(1);
    }

    @Test
    void guardarEtiquetas_DeberiaGuardar_SiRevistaExiste() throws ExcepcionNoExiste {
        RevistaEtiquetasRequest etiqReq = new RevistaEtiquetasRequest();
        etiqReq.setIdRevista(1);
        etiqReq.setEtiquetasIds(List.of(1, 2));

        when(repo.existsById(1)).thenReturn(true);

        service.guardarEtiquetas(etiqReq);

        verify(revEtiqRepo).eliminarEtiquetasDeRevista(1);
        verify(revEtiqRepo).saveAll(anyList());
    }

    @Test
    void mapToResponse_DeberiaArmarObjetoCompleto() {
        configurarMocksMapeo();

        // Datos específicos para este test
        EntidadEtiqueta etiq = new EntidadEtiqueta(5, "Java");
        EntidadRevistaEtiqueta rel = new EntidadRevistaEtiqueta(new RevistaEtiquetaId(1, 5));
        EntidadEdicion edicion = new EntidadEdicion();
        edicion.setId(100);
        edicion.setNumeroEdicion("1era Edición");
        edicion.setRevistaId(1);

        when(repo.findById(1)).thenReturn(Optional.of(revistaEjemplo));
        when(revEtiqRepo.findById_RevistaId(1)).thenReturn(List.of(rel));
        when(etiqRepo.findAllById(anyList())).thenReturn(List.of(etiq));
        when(edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(1)).thenReturn(List.of(edicion));
        
        when(suscripcionRepo.countByRevistaId(1)).thenReturn(10);
        when(likeRepo.countByRevistaId(1)).thenReturn(5);
        when(comentarioRepo.countByRevistaId(1)).thenReturn(3);

        RevistaResponse res = assertDoesNotThrow(() -> service.getById(1));

        assertNotNull(res);
        assertEquals("Java", res.getEtiquetas().get(0).getNombre());
        assertEquals(10, res.getCantidadSuscripciones());
        assertEquals("http://test.com/foto.jpg", res.getEditor().getPerfilUrl()); // Verifica que el perfil se mapeó
        assertEquals("Tecnología", res.getCategoria().getNombre());
    }
    
    @Test
    void findByActivas_DeberiaLlamarMantenimientoYRetornarLista() throws ExcepcionNoExiste {
        doNothing().when(mantenimiento).desactivarBloqueosDeAnunciosExpirados();
        
        when(repo.findByActivaTrue()).thenReturn(List.of(revistaEjemplo));
        configurarMocksMapeo();

        List<RevistaResponse> resultados = service.findByActivas();

        assertNotNull(resultados);
        verify(mantenimiento).desactivarBloqueosDeAnunciosExpirados();
    }

    @Test
    void findByEditorId_DeberiaRetornarLista() {
        when(repo.findByEditorId(10)).thenReturn(List.of(revistaEjemplo));
        configurarMocksMapeo();

        List<RevistaResponse> resultados = service.findByEditorId(10);

        assertEquals(1, resultados.size());
        verify(repo).findByEditorId(10);
    }
    
    @Mock private PagoRevistaRepositorio pagoRepo; 

    @Test
    void cambiarEstado_AFalso_DeberiaDesactivarSinValidarPagos() throws ExcepcionNoExiste {
        when(repo.getById(1)).thenReturn(revistaEjemplo);
        when(repo.save(any())).thenReturn(revistaEjemplo);
        configurarMocksMapeo();

        service.cambiarEstado(1, false);

        verify(pagoRepo).findByRevistaId(1);
        verify(repo).save(revistaEjemplo);
    }

    @Test
    void cambiarEstado_ATrue_ConPagoVigente_DeberiaActivar() throws ExcepcionNoExiste {
        when(repo.getById(1)).thenReturn(revistaEjemplo);
        
        EntidadPagoRevista pago = new EntidadPagoRevista();
        pago.setPeriodoFin(LocalDate.now().plusDays(10)); // Pago vigente en el futuro

        when(pagoRepo.findByRevistaId(1)).thenReturn(List.of(pago));
        when(repo.save(any())).thenReturn(revistaEjemplo);
        configurarMocksMapeo();

        RevistaResponse res = service.cambiarEstado(1, true);

        assertTrue(revistaEjemplo.isActiva());
        verify(repo).save(revistaEjemplo);
    }

    @Test
    void cambiarEstado_ATrue_SinPagosVigentes_NoDeberiaActivar() throws ExcepcionNoExiste {
       when(repo.getById(1)).thenReturn(revistaEjemplo);
        revistaEjemplo.setActiva(false);

        EntidadPagoRevista pagoExpirado = new EntidadPagoRevista();
        pagoExpirado.setPeriodoFin(LocalDate.now().minusDays(1)); 

        when(pagoRepo.findByRevistaId(1)).thenReturn(List.of(pagoExpirado));
        when(repo.save(any())).thenReturn(revistaEjemplo);
        configurarMocksMapeo();
        
        service.cambiarEstado(1, true);

        assertFalse(revistaEjemplo.isActiva());
    }
    @Test
    void actualizar_DeberiaLanzarExcepcion_SiTituloDuplicado() {
        when(repo.findById(1)).thenReturn(Optional.of(revistaEjemplo));
        when(repo.existeTituloEnOtraRevista(anyString(), anyInt())).thenReturn(true);

        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.actualizar(1, requestEjemplo));
    }

    @Test
    void eliminar_DeberiaLanzarExcepcion_SiNoExiste() {
        when(repo.existsById(99)).thenReturn(false);
        assertThrows(ExcepcionNoExiste.class, () -> service.eliminar(99));
    }

    @Test
    void guardarEtiquetas_DeberiaLanzarExcepcion_SiRevistaNoExiste() {
        RevistaEtiquetasRequest req = new RevistaEtiquetasRequest();
        req.setIdRevista(99);
        when(repo.existsById(99)).thenReturn(false);

        assertThrows(ExcepcionNoExiste.class, () -> service.guardarEtiquetas(req));
    }
}