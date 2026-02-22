/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Comentarios_Reacciones_Suscripciones.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoLikes.LikeRequest;
import com.e.gomez.Practica1AyD2.dtoLikes.LikeResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadLike;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.LikeRepositorio;
import com.e.gomez.Practica1AyD2.servicios.LikeServiceImpl;
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
public class LikeServiceImplTest {

    @Mock
    private LikeRepositorio repo;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private LikeServiceImpl service;

    private EntidadUsuario usuarioEjemplo;
    private EntidadLike likeEjemplo;
    private LikeRequest requestEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new EntidadUsuario();
        usuarioEjemplo.setId(10);
        usuarioEjemplo.setUsername("testuser");
        usuarioEjemplo.setCorreo("test@mail.com");

        likeEjemplo = new EntidadLike();
        likeEjemplo.setId(1);
        likeEjemplo.setRevistaId(5);
        likeEjemplo.setUsuarioId(10);
        likeEjemplo.setFechaCreacion(LocalDateTime.now());

        requestEjemplo = new LikeRequest(5, 10);
    }

    @Test
    void darLike_DeberiaGuardar_CuandoNoExisteDuplicado() throws ExcepcionNoExiste, ExcepcionEntidadDuplicada {
        // Arrange
        when(repo.existsByRevistaIdAndUsuarioId(5, 10)).thenReturn(false);
        when(repo.save(any(EntidadLike.class))).thenReturn(likeEjemplo);
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);

        // Act
        LikeResponse result = service.darLike(requestEjemplo);

        // Assert
        assertNotNull(result);
        assertEquals(10, result.getUsuario().getId());
        verify(repo).save(any(EntidadLike.class));
    }

    @Test
    void darLike_DeberiaLanzarExcepcion_CuandoYaExiste() {
        // Arrange
        when(repo.existsByRevistaIdAndUsuarioId(5, 10)).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.darLike(requestEjemplo));
        verify(repo, never()).save(any());
    }

    @Test
    void quitarLike_DeberiaLlamarAlRepositorio() {
        // Act
        service.quitarLike(5, 10);

        // Assert
        verify(repo).deleteByRevistaIdAndUsuarioId(5, 10);
    }

    @Test
    void contarLikesRevista_DeberiaRetornarValor() {
        // Arrange
        when(repo.countByRevistaId(5)).thenReturn(15);

        // Act
        int conteo = service.contarLikesRevista(5);

        // Assert
        assertEquals(15, conteo);
    }

    @Test
    void findByRevistaId_DeberiaRetornarListaMapeada() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findByRevistaId(5)).thenReturn(List.of(likeEjemplo));
        when(usuarioService.getById(10)).thenReturn(usuarioEjemplo);

        // Act
        List<LikeResponse> lista = service.findByRevistaId(5);

        // Assert
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals("testuser", lista.get(0).getUsuario().getUsername());
    }

    @Test
    void yaDioLike_DeberiaRetornarTrue_SiExiste() {
        // Arrange
        when(repo.existsByRevistaIdAndUsuarioId(1, 1)).thenReturn(true);

        // Act
        boolean existe = service.yaDioLike(1, 1);

        // Assert
        assertTrue(existe);
    }
}
