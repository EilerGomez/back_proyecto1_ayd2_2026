package Servicios.Comentarios_Reacciones_Suscripciones.Test;

import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscricpionResponseByRevistaId;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscripcionRequest;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.dtoRevistasPorSuscripcionByUsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadSuscripcion;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RevistaRepositorio;
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
@MockitoSettings(strictness = Strictness.LENIENT)
public class SuscripcionServiceImplTest {

    @Mock
    private SuscripcionRepositorio repo;
    @Mock
    private RevistaRepositorio revistaRepo; 
    @Mock
    private RevistaService servicioRevista;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private PerfilRepositorio perfilRepo;

    @InjectMocks
    private SuscripcionServiceImpl service;

    private EntidadSuscripcion suscripcionEjemplo;
    private SuscripcionRequest requestEjemplo;
    private EntidadUsuario usuarioEjemplo;
    private EntidadPerfil perfilEjemplo;
    private EntidadRevista revistaEjemplo; 
    private RevistaResponse revistaResponseEjemplo;

    @BeforeEach
    void setUp() {
        // Datos de Usuario
        usuarioEjemplo = new EntidadUsuario();
        usuarioEjemplo.setId(10);
        usuarioEjemplo.setUsername("lector1");

        // Datos de Perfil
        perfilEjemplo = new EntidadPerfil();
        perfilEjemplo.setUsuarioId(10);
        perfilEjemplo.setFoto_url("http://test.com/foto.jpg");

        // Datos de Revista (Entidad)
        revistaEjemplo = new EntidadRevista();
        revistaEjemplo.setId(5);
        revistaEjemplo.setPermiteSuscripciones(true);

        // Datos de Suscripción
        suscripcionEjemplo = new EntidadSuscripcion();
        suscripcionEjemplo.setId(1);
        suscripcionEjemplo.setRevistaId(5);
        suscripcionEjemplo.setUsuarioId(10);
        suscripcionEjemplo.setFechaSuscripcion(LocalDate.now());
        suscripcionEjemplo.setActiva(true);

        // Request
        requestEjemplo = new SuscripcionRequest();
        requestEjemplo.setRevistaId(5);
        requestEjemplo.setUsuarioId(10);
        requestEjemplo.setFechaSuscripcion(LocalDate.now());
        requestEjemplo.setActiva(true);

        // Mock de RevistaResponse (DTO)
        revistaResponseEjemplo = mock(RevistaResponse.class);
        when(revistaResponseEjemplo.getTitulo()).thenReturn("Revista Científica");
        
        // Comportamiento global del Perfil
        when(perfilRepo.getByUsuarioId(10)).thenReturn(perfilEjemplo);
    }

    @Test
    void suscribir_DeberiaGuardar_CuandoNoExisteDuplicadoYPermiteSuscripcion() throws Exception {
        // Arrange
        when(repo.existsByRevistaIdAndUsuarioId(5, 10)).thenReturn(false);
        when(revistaRepo.getById(5)).thenReturn(revistaEjemplo);
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);
        when(repo.save(any(EntidadSuscripcion.class))).thenReturn(suscripcionEjemplo);

        // Act
        SuscricpionResponseByRevistaId res = service.suscribir(requestEjemplo);

        // Assert
        assertNotNull(res);
        assertEquals("lector1", res.getUsuario().getUsername());
        verify(repo).save(any(EntidadSuscripcion.class));
    }

    @Test
    void suscribir_DeberiaLanzarExcepcion_CuandoRevistaNoPermiteSuscripciones() {
        // Arrange
        revistaEjemplo.setPermiteSuscripciones(false);
        when(repo.existsByRevistaIdAndUsuarioId(5, 10)).thenReturn(false);
        when(revistaRepo.getById(5)).thenReturn(revistaEjemplo);

        // Act & Assert
        ExcepcionEntidadDuplicada ex = assertThrows(ExcepcionEntidadDuplicada.class, 
            () -> service.suscribir(requestEjemplo));
        
        assertEquals("No se permiten suscripciones.", ex.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void suscribir_DeberiaLanzarExcepcion_CuandoYaExisteSuscripcion() {
        // Arrange
        when(repo.existsByRevistaIdAndUsuarioId(5, 10)).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.suscribir(requestEjemplo));
        verify(revistaRepo, never()).getById(anyInt());
        verify(repo, never()).save(any());
    }

    @Test
    void listarPorUsuario_DeberiaRetornarListaConRevistas() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findByUsuarioId(10)).thenReturn(List.of(suscripcionEjemplo));
        when(servicioRevista.getById(5)).thenReturn(revistaResponseEjemplo);

        // Act
        List<dtoRevistasPorSuscripcionByUsuarioResponse> lista = service.listarPorUsuario(10);

        // Assert
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("Revista Científica", lista.get(0).getRevista().getTitulo());
    }

    @Test
    void listarPorRevista_DeberiaRetornarListaConUsuarios() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findByRevistaId(5)).thenReturn(List.of(suscripcionEjemplo));
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);

        // Act
        List<SuscricpionResponseByRevistaId> lista = service.listarPorRevista(5);

        // Assert
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("lector1", lista.get(0).getUsuario().getUsername());
        assertEquals("http://test.com/foto.jpg", lista.get(0).getUsuario().getPerfilUrl());
    }

    @Test
    void cambiarEstado_DeberiaModificarBooleano() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findById(1)).thenReturn(Optional.of(suscripcionEjemplo));

        // Act
        service.cambiarEstado(1, false);

        // Assert
        assertFalse(suscripcionEjemplo.isActiva());
        verify(repo).save(suscripcionEjemplo);
    }

    @Test
    void cambiarEstado_DeberiaLanzarExcepcion_SiNoExiste() {
        // Arrange
        when(repo.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.cambiarEstado(99, false));
    }

    @Test
    void cancelarSuscripcion_DeberiaLlamarDelete() {
        // Act
        service.cancelarSuscripcion(1);
        
        // Assert
        verify(repo).deleteById(1);
    }
}