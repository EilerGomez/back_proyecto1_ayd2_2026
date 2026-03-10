package Servicios.Comentarios_Reacciones_Suscripciones.Test;

import com.e.gomez.Practica1AyD2.dtoLikes.LikeRequest;
import com.e.gomez.Practica1AyD2.dtoLikes.LikeResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadLike;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.LikeRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RevistaRepositorio;
import com.e.gomez.Practica1AyD2.servicios.LikeServiceImpl;
import com.e.gomez.Practica1AyD2.servicios.PerfilService;
import com.e.gomez.Practica1AyD2.servicios.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceImplTest {

    @Mock
    private LikeRepositorio repo;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PerfilService perfilService;

    @Mock
    private RevistaRepositorio revistaRepo; 

    @InjectMocks
    private LikeServiceImpl service;

    private EntidadUsuario usuarioEjemplo;
    private EntidadPerfil perfilEjemplo;
    private EntidadLike likeEjemplo;
    private EntidadRevista revistaEjemplo;
    private LikeRequest requestEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new EntidadUsuario();
        usuarioEjemplo.setId(10);
        usuarioEjemplo.setUsername("testuser");
        usuarioEjemplo.setCorreo("test@mail.com");

        perfilEjemplo = new EntidadPerfil();
        perfilEjemplo.setUsuarioId(10);
        perfilEjemplo.setFoto_url("http://test.com/foto.jpg");

        revistaEjemplo = new EntidadRevista();
        revistaEjemplo.setId(5);
        revistaEjemplo.setPermiteLikes(true);

        likeEjemplo = new EntidadLike();
        likeEjemplo.setId(1);
        likeEjemplo.setRevistaId(5);
        likeEjemplo.setUsuarioId(10);
        likeEjemplo.setFechaCreacion(LocalDateTime.now());

        requestEjemplo = new LikeRequest(5, 10);
    }

    @Test
    void darLike_DeberiaGuardar_CuandoTodoEsValido() throws ExcepcionNoExiste, ExcepcionEntidadDuplicada {
        // Arrange
        when(repo.existsByRevistaIdAndUsuarioId(5, 10)).thenReturn(false);
        when(revistaRepo.getById(5)).thenReturn(revistaEjemplo);
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);
        when(perfilService.findByUsuarioId(10)).thenReturn(perfilEjemplo);
        when(repo.save(any(EntidadLike.class))).thenReturn(likeEjemplo);

        // Act
        LikeResponse result = service.darLike(requestEjemplo);

        // Assert
        assertNotNull(result);
        assertEquals(10, result.getUsuario().getId());
        verify(repo).save(any(EntidadLike.class));
    }

    @Test
    void darLike_DeberiaLanzarExcepcion_CuandoRevistaNoPermiteLikes() {
        // Arrange
        revistaEjemplo.setPermiteLikes(false); 
        when(repo.existsByRevistaIdAndUsuarioId(5, 10)).thenReturn(false);
        when(revistaRepo.getById(5)).thenReturn(revistaEjemplo);

        // Act & Assert
        ExcepcionEntidadDuplicada ex = assertThrows(ExcepcionEntidadDuplicada.class, 
            () -> service.darLike(requestEjemplo));
        
        assertEquals("NO se permiten likes a esta revista", ex.getMessage());
        verify(repo, never()).save(any());
    }

    @Test
    void darLike_DeberiaLanzarExcepcion_CuandoYaExisteLike() {
        // Arrange
        when(repo.existsByRevistaIdAndUsuarioId(5, 10)).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.darLike(requestEjemplo));
        verify(revistaRepo, never()).getById(anyInt());
        verify(repo, never()).save(any());
    }

    @Test
    void quitarLike_DeberiaLlamarAlRepositorio() {
        service.quitarLike(5, 10);
        verify(repo).deleteByRevistaIdAndUsuarioId(5, 10);
    }

    @Test
    void contarLikesRevista_DeberiaRetornarValor() {
        when(repo.countByRevistaId(5)).thenReturn(15);
        assertEquals(15, service.contarLikesRevista(5));
    }

    @Test
    void findByRevistaId_DeberiaRetornarListaMapeada() throws ExcepcionNoExiste {
        when(repo.findByRevistaId(5)).thenReturn(List.of(likeEjemplo));
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);
        when(perfilService.findByUsuarioId(10)).thenReturn(perfilEjemplo);

        List<LikeResponse> lista = service.findByRevistaId(5);

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("testuser", lista.get(0).getUsuario().getUsername());
    }

    @Test
    void yaDioLike_DeberiaRetornarTrue_SiExiste() {
        when(repo.existsByRevistaIdAndUsuarioId(1, 1)).thenReturn(true);
        assertTrue(service.yaDioLike(1, 1));
    }
}