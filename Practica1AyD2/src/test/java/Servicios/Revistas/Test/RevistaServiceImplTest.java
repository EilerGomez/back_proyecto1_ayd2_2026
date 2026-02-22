/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Revistas.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.RevistaEtiquetasRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.*;
import com.e.gomez.Practica1AyD2.repositorios.*;
import com.e.gomez.Practica1AyD2.servicios.RevistaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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

    @InjectMocks
    private RevistaServiceImpl service;

    private EntidadRevista revistaEjemplo;
    private RevistaRequest requestEjemplo;
    private EntidadCategoria categoriaEjemplo;
    private EntidadUsuario usuarioEjemplo;

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
        usuarioEjemplo.setCorreo("CorreoUser");
        usuarioEjemplo.setUsername("username");
        usuarioEjemplo.setNombre("nombre");
        usuarioEjemplo.setApellido("Apellido");
    }
    private void configurarMocksMapeo() {
        when(revEtiqRepo.findById_RevistaId(anyInt())).thenReturn(new ArrayList<>());
        when(edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(anyInt())).thenReturn(new ArrayList<>());
        // Mocks para los nuevos contadores
        when(suscripcionRepo.countByRevistaId(anyInt())).thenReturn(0);
        when(likeRepo.countByRevistaId(anyInt())).thenReturn(0);
        when(comentarioRepo.countByRevistaId(anyInt())).thenReturn(0);
        
        when(categoriaRepo.getById(anyInt())).thenReturn(categoriaEjemplo);
        when(usuarioRepo.findById(anyInt())).thenReturn(Optional.of(usuarioEjemplo));
    }

    @Test
    void crear_DeberiaGuardar_CuandoTituloNoExiste() throws ExcepcionEntidadDuplicada {
        // Arrange
        when(repo.existsByTitulo(anyString())).thenReturn(false);
        when(repo.save(any(EntidadRevista.class))).thenReturn(revistaEjemplo);
        // Mocks para el mapToResponse
        when(revEtiqRepo.findById_RevistaId(anyInt())).thenReturn(new ArrayList<>());
        when(edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(anyInt())).thenReturn(new ArrayList<>());
        when(categoriaRepo.getById(anyInt())).thenReturn(categoriaEjemplo);

        configurarMocksMapeo();
        // Act
        RevistaResponse resultado = service.crear(requestEjemplo);

        // Assert
        assertNotNull(resultado);
        assertEquals("Revista Test", resultado.getTitulo());
        verify(repo, times(1)).save(any(EntidadRevista.class));
    }

    @Test
    void crear_DeberiaLanzarExcepcion_CuandoTituloYaExiste() {
        // Arrange
        when(repo.existsByTitulo(requestEjemplo.getTitulo())).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.crear(requestEjemplo));
        verify(repo, never()).save(any());
    }

    @Test
    void getById_DeberiaRetornarRevista_SiExiste() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findById(1)).thenReturn(Optional.of(revistaEjemplo));
        configurarMocksMapeo();
        when(revEtiqRepo.findById_RevistaId(1)).thenReturn(new ArrayList<>());
        when(edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(1)).thenReturn(new ArrayList<>());

        // Act
        RevistaResponse resultado = service.getById(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void actualizar_DeberiaModificar_SiEsValido() throws Exception {
        // Arrange
        when(repo.findById(1)).thenReturn(Optional.of(revistaEjemplo));
        when(repo.existeTituloEnOtraRevista(anyString(), anyInt())).thenReturn(false);
        when(repo.save(any(EntidadRevista.class))).thenReturn(revistaEjemplo);
        configurarMocksMapeo();

        // Act
        RevistaResponse resultado = service.actualizar(1, requestEjemplo);

        // Assert
        assertNotNull(resultado);
        verify(repo).save(revistaEjemplo);
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
    void guardarEtiquetas_DeberiaGuardar_SiRevistaExiste() throws ExcepcionNoExiste {
        // Arrange
        RevistaEtiquetasRequest etiqReq = new RevistaEtiquetasRequest();
        etiqReq.setIdRevista(1);
        etiqReq.setEtiquetasIds(List.of(1, 2));

        when(repo.existsById(1)).thenReturn(true);

        // Act
        service.guardarEtiquetas(etiqReq);

        // Assert
        verify(revEtiqRepo).eliminarEtiquetasDeRevista(1);
        verify(revEtiqRepo).saveAll(anyList());
    }

   @Test
    void mapToResponse_DeberiaArmarObjetoCompleto() {
        
        configurarMocksMapeo();

        // Arrange 
        EntidadEtiqueta etiq = new EntidadEtiqueta(5, "Java");
        EntidadRevistaEtiqueta rel = new EntidadRevistaEtiqueta(new RevistaEtiquetaId(1, 5));

        EntidadEdicion edicion = new EntidadEdicion();
        edicion.setId(100);
        edicion.setRevistaId(1); 
        edicion.setNumeroEdicion("1era Edición");
        edicion.setPdfUrl("http://test.com");
        edicion.setFechaPublicacion(LocalDateTime.now());

        // 
        when(repo.findById(1)).thenReturn(Optional.of(revistaEjemplo));
        when(revEtiqRepo.findById_RevistaId(1)).thenReturn(List.of(rel));
        when(etiqRepo.findAllById(anyList())).thenReturn(List.of(etiq));
        when(edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(1)).thenReturn(List.of(edicion));
        
        // Seteamos valores específicos para los contadores
        when(suscripcionRepo.countByRevistaId(1)).thenReturn(10);
        when(likeRepo.countByRevistaId(1)).thenReturn(5);
        when(comentarioRepo.countByRevistaId(1)).thenReturn(3);

        // Act
        RevistaResponse res = assertDoesNotThrow(() -> service.getById(1));

        // Assert
        assertNotNull(res);
        assertEquals(1, res.getEtiquetas().size()); 
        assertEquals("Java", res.getEtiquetas().get(0).getNombre());
        assertEquals(1, res.getEdiciones().size()); 
        
        assertEquals(10, res.getCantidadSuscripciones());
        assertEquals(5, res.getCantidadLikes());
        assertEquals(3, res.getCantidadComentarios());
        
        assertNotNull(res.getCategoria()); 
        assertEquals("Tecnología", res.getCategoria().getNombre()); 
        
        assertNotNull(res.getEditor());
        assertEquals(10, res.getEditor().getId());
    }
}