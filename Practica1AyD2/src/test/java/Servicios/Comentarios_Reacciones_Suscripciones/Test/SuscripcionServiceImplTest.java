package Servicios.Comentarios_Reacciones_Suscripciones.Test;

import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscricpionResponseByRevistaId;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscripcionRequest;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.dtoRevistasPorSuscripcionByUsuarioResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.SuscripcionRepositorio;
import com.e.gomez.Practica1AyD2.servicios.RevistaService;
import com.e.gomez.Practica1AyD2.servicios.SuscripcionServiceImpl;
import com.e.gomez.Practica1AyD2.servicios.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Evita errores por mocks no usados en tests específicos
public class SuscripcionServiceImplTest {

    @Mock
    private SuscripcionRepositorio repo;
    @Mock
    private RevistaService servicioRevista;
    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private SuscripcionServiceImpl service;

    private EntidadSuscripcion suscripcionEjemplo;
    private SuscripcionRequest requestEjemplo;
    private EntidadUsuario usuarioEjemplo;
    private RevistaResponse revistaResponseEjemplo;

    @BeforeEach
    void setUp() {
        // Datos de Usuario
        usuarioEjemplo = new EntidadUsuario();
        usuarioEjemplo.setId(10);
        usuarioEjemplo.setUsername("lector1");
        usuarioEjemplo.setNombre("Juan");
        usuarioEjemplo.setApellido("Perez");

        // Datos de Suscripción (ID=1, RevistaID=5)
        suscripcionEjemplo = new EntidadSuscripcion();
        suscripcionEjemplo.setId(1);
        suscripcionEjemplo.setRevistaId(5);
        suscripcionEjemplo.setUsuarioId(10);
        suscripcionEjemplo.setFechaSuscripcion(LocalDate.now());
        suscripcionEjemplo.setActiva(true);

        // Datos de Request
        requestEjemplo = new SuscripcionRequest();
        requestEjemplo.setRevistaId(5);
        requestEjemplo.setUsuarioId(10);
        requestEjemplo.setFechaSuscripcion(LocalDate.now());
        requestEjemplo.setActiva(true);

        // Mock de RevistaResponse (usado en listados)
        revistaResponseEjemplo = mock(RevistaResponse.class);
        when(revistaResponseEjemplo.getTitulo()).thenReturn("Revista Científica");
    }

    //
    @Test
    void suscribir_DeberiaGuardar_CuandoNoExisteDuplicado() throws Exception {
        when(repo.existsByRevistaIdAndUsuarioId(5, 10)).thenReturn(false);
        when(repo.save(any(EntidadSuscripcion.class))).thenReturn(suscripcionEjemplo);
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);

        SuscricpionResponseByRevistaId res = service.suscribir(requestEjemplo);

        assertNotNull(res);
        assertEquals("lector1", res.getUsuario().getUsername());
        verify(repo).save(any(EntidadSuscripcion.class));
    }

    // 
    @Test
    void suscribir_DeberiaLanzarExcepcion_CuandoYaExiste() {
        when(repo.existsByRevistaIdAndUsuarioId(5, 10)).thenReturn(true);

        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.suscribir(requestEjemplo));
        verify(repo, never()).save(any());
    }

    // 
    @Test
    void listarPorUsuario_DeberiaRetornarListaConRevistas() throws ExcepcionNoExiste {
        when(repo.findByUsuarioId(10)).thenReturn(List.of(suscripcionEjemplo));
        // 
        when(servicioRevista.getById(5)).thenReturn(revistaResponseEjemplo);

        List<dtoRevistasPorSuscripcionByUsuarioResponse> lista = service.listarPorUsuario(10);

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("Revista Científica", lista.get(0).getRevista().getTitulo());
    }

    // 
    @Test
    void listarPorRevista_DeberiaRetornarListaConUsuarios() throws ExcepcionNoExiste {
        when(repo.findByRevistaId(5)).thenReturn(List.of(suscripcionEjemplo));
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);

        List<SuscricpionResponseByRevistaId> lista = service.listarPorRevista(5);

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("lector1", lista.get(0).getUsuario().getUsername());
    }

    // 
    @Test
    void cambiarEstado_DeberiaModificarBooleano() throws ExcepcionNoExiste {
        when(repo.findById(1)).thenReturn(Optional.of(suscripcionEjemplo));

        service.cambiarEstado(1, false);

        assertFalse(suscripcionEjemplo.isActiva());
        verify(repo).save(suscripcionEjemplo);
    }

    // 
    @Test
    void cancelarSuscripcion_DeberiaLlamarDelete() {
        service.cancelarSuscripcion(1);
        verify(repo).deleteById(1);
    }
}