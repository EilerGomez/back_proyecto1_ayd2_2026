package Servicios.Comentarios_Reacciones_Suscripciones.Test;

import com.e.gomez.Practica1AyD2.dtoComentarios.ComentarioRequest;
import com.e.gomez.Practica1AyD2.dtoComentarios.ComentarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadComentario;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.ComentarioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RevistaRepositorio;
import com.e.gomez.Practica1AyD2.servicios.ComentarioServiceImpl;
import com.e.gomez.Practica1AyD2.servicios.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComentarioServiceImplTest {

    @Mock
    private ComentarioRepositorio repo;
    
    @Mock
    private PerfilRepositorio perfilRepo;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private RevistaRepositorio revistaRepo; 

    @InjectMocks
    private ComentarioServiceImpl service;

    private EntidadUsuario usuarioEjemplo;
    private EntidadPerfil perfilEjemplo;
    private EntidadRevista revistaEjemplo;
    private EntidadComentario comentarioEjemplo;
    private ComentarioRequest requestEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new EntidadUsuario();
        usuarioEjemplo.setId(10);
        usuarioEjemplo.setUsername("comentador");
        usuarioEjemplo.setNombre("Nombre");
        usuarioEjemplo.setApellido("Apellido");
        usuarioEjemplo.setCorreo("user@test.com");
        usuarioEjemplo.setEstado("ACTIVO");

        perfilEjemplo = new EntidadPerfil();
        perfilEjemplo.setUsuarioId(10);
        perfilEjemplo.setFoto_url("http://test.com/foto.jpg");
        perfilEjemplo.setDescripcion("Bio de prueba");

        revistaEjemplo = new EntidadRevista();
        revistaEjemplo.setId(5);
        revistaEjemplo.setPermiteComentarios(true); 

        comentarioEjemplo = new EntidadComentario();
        comentarioEjemplo.setId(1);
        comentarioEjemplo.setRevistaId(5);
        comentarioEjemplo.setUsuarioId(10);
        comentarioEjemplo.setContenido("Contenido original");
        comentarioEjemplo.setFechaCreacion(LocalDateTime.now());

        requestEjemplo = new ComentarioRequest(5, 10, "Este es un comentario");
    }

    @Test
    void crear_DeberiaGuardarCorrectamente_CuandoRevistaPermiteComentarios() throws ExcepcionNoExiste {
        // Arrange
        when(revistaRepo.getById(5)).thenReturn(revistaEjemplo);
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);
        when(perfilRepo.findByUsuarioId(10)).thenReturn(Optional.of(perfilEjemplo));
        when(repo.save(any(EntidadComentario.class))).thenReturn(comentarioEjemplo);

        // Act
        ComentarioResponse res = service.crear(requestEjemplo);

        // Assert
        assertNotNull(res);
        assertEquals("Contenido original", res.getContenido());
        assertEquals("comentador", res.getUsuario().getUsername());
        verify(repo).save(any(EntidadComentario.class));
    }

    @Test
    void crear_DeberiaLanzarExcepcion_CuandoRevistaNoPermiteComentarios() {
        // Arrange
        revistaEjemplo.setPermiteComentarios(false);
        when(revistaRepo.getById(5)).thenReturn(revistaEjemplo);

        // Act & Assert
        ExcepcionNoExiste ex = assertThrows(ExcepcionNoExiste.class, () -> service.crear(requestEjemplo));
        assertEquals("No se permiten comentarios a esta revista", ex.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void actualizar_DeberiaCambiarContenido_SiExiste() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findById(1)).thenReturn(Optional.of(comentarioEjemplo));
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);
        when(perfilRepo.findByUsuarioId(10)).thenReturn(Optional.of(perfilEjemplo));
        when(repo.save(any(EntidadComentario.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        ComentarioResponse res = service.actualizar(1, "Contenido editado");

        // Assert
        assertEquals("Contenido editado", res.getContenido());
        verify(repo).save(comentarioEjemplo);
    }

    @Test
    void actualizar_DeberiaLanzarExcepcion_SiNoExiste() {
        // Arrange
        when(repo.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.actualizar(99, "Texto"));
    }

    @Test
    void eliminar_DeberiaLlamarDelete_SiExiste() throws ExcepcionNoExiste {
        // Arrange
        when(repo.existsById(1)).thenReturn(true);

        // Act
        service.eliminar(1);

        // Assert
        verify(repo).deleteById(1);
    }

    @Test
    void eliminar_DeberiaLanzarExcepcion_SiNoExiste() {
        // Arrange
        when(repo.existsById(1)).thenReturn(false);

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.eliminar(1));
    }

    @Test
    void listarPorRevista_DeberiaRetornarListaConUsuarios() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findByRevistaIdOrderByFechaCreacionDesc(5)).thenReturn(List.of(comentarioEjemplo));
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);
        when(perfilRepo.findByUsuarioId(10)).thenReturn(Optional.of(perfilEjemplo));

        // Act
        List<ComentarioResponse> lista = service.listarPorRevista(5);

        // Assert
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("comentador", lista.get(0).getUsuario().getUsername());
    }

    @Test
    void listarPorUsuario_DeberiaRetornarListaMapeada() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findByUsuarioIdOrderByFechaCreacionDesc(10)).thenReturn(List.of(comentarioEjemplo));
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);
        when(perfilRepo.findByUsuarioId(10)).thenReturn(Optional.of(perfilEjemplo));

        // Act
        List<ComentarioResponse> lista = service.listarPorUsuario(10);

        // Assert
        assertEquals(1, lista.size());
        assertNotNull(lista.get(0).getUsuario());
        assertEquals("http://test.com/foto.jpg", lista.get(0).getUsuario().getPerfilUrl());
    }
}