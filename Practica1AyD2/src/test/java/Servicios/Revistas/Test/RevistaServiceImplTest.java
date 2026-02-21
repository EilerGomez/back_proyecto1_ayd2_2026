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

    @InjectMocks
    private RevistaServiceImpl service;

    private EntidadRevista revistaEjemplo;
    private RevistaRequest requestEjemplo;

    @BeforeEach
    void setUp() {
        revistaEjemplo = new EntidadRevista();
        revistaEjemplo.setId(1);
        revistaEjemplo.setTitulo("Revista Test");
        revistaEjemplo.setEditorId(10);
        // FIX 1: Asignar categoriaId para evitar el NPE en el DTO RevistaResponse
        revistaEjemplo.setCategoriaId(1); 
        revistaEjemplo.setDescripcion("Descripción de prueba");

        requestEjemplo = new RevistaRequest();
        requestEjemplo.setTitulo("Revista Test");
        requestEjemplo.setEditorId(10);
        requestEjemplo.setCategoriaId(1);
    }

    @Test
    void crear_DeberiaGuardar_CuandoTituloNoExiste() throws ExcepcionEntidadDuplicada {
        // Arrange
        when(repo.existsByTitulo(anyString())).thenReturn(false);
        when(repo.save(any(EntidadRevista.class))).thenReturn(revistaEjemplo);
        // Mocks para el mapToResponse
        when(revEtiqRepo.findById_RevistaId(anyInt())).thenReturn(new ArrayList<>());
        when(edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(anyInt())).thenReturn(new ArrayList<>());

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
        // Arrange
        EntidadEtiqueta etiq = new EntidadEtiqueta(5, "Java");
        EntidadRevistaEtiqueta rel = new EntidadRevistaEtiqueta(new RevistaEtiquetaId(1, 5));

        EntidadEdicion edicion = new EntidadEdicion();
        edicion.setId(100);
        edicion.setTitulo("Edicion 1");
        // FIX 2: Asignar revistaId para evitar el NPE en el DTO EdicionResponse
        edicion.setRevistaId(1); 
        edicion.setNumeroEdicion("1era Edición");
        edicion.setPdfUrl("http://test.com");
        edicion.setFechaPublicacion(LocalDateTime.now());

        when(revEtiqRepo.findById_RevistaId(1)).thenReturn(List.of(rel));
        when(etiqRepo.findAllById(anyList())).thenReturn(List.of(etiq));
        when(edicionRepo.findByRevistaIdOrderByFechaPublicacionDesc(1)).thenReturn(List.of(edicion));
        when(repo.findById(1)).thenReturn(Optional.of(revistaEjemplo));

        // Act
        RevistaResponse res = assertDoesNotThrow(() -> service.getById(1));

        // Assert
        assertNotNull(res);
        assertEquals(1, res.getEtiquetas().size());
        assertEquals("Java", res.getEtiquetas().get(0).getNombre());
        assertEquals(1, res.getEdiciones().size());
    }
}