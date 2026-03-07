/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Anuncios.TEst;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.TipoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadTipoAnuncio;
import com.e.gomez.Practica1AyD2.repositorios.TipoAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.servicios.TipoAnuncioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoAnuncioServiceImplTest {

    @Mock
    private TipoAnuncioRepositorio repo;

    @InjectMocks
    private TipoAnuncioServiceImpl service;

    private EntidadTipoAnuncio tipoEjemplo;

    @BeforeEach
    void setUp() {
        tipoEjemplo = new EntidadTipoAnuncio();
        tipoEjemplo.setId(1);
        tipoEjemplo.setCodigo("TEXTO");
        tipoEjemplo.setDescripcion("Anuncio de Texto");
    }

    // --- TESTS PARA listarTodos ---

    @Test
    void listarTodos_DeberiaMapearListaCorrectamente() {
        // Arrange
        when(repo.findAll()).thenReturn(List.of(tipoEjemplo));

        // Act
        List<TipoAnuncioResponse> resultado = service.listarTodos();

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("TEXTO", resultado.get(0).getCodigo());
        verify(repo).findAll();
    }

    // --- TESTS PARA obtenerPorCodigo ---

    @Test
    void obtenerPorCodigo_SiExiste_DeberiaRetornarResponse() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findByCodigo("TEXTO")).thenReturn(Optional.of(tipoEjemplo));

        // Act
        TipoAnuncioResponse response = service.obtenerPorCodigo("TEXTO");

        // Assert
        assertNotNull(response);
        assertEquals("TEXTO", response.getCodigo());
    }

    @Test
    void obtenerPorCodigo_SiNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(repo.findByCodigo(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.obtenerPorCodigo("INVALIDO"));
    }

    // --- TESTS PARA obtenerPorId ---

    @Test
    void obtenerPorId_SiExiste_DeberiaRetornarResponse() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findById(1)).thenReturn(Optional.of(tipoEjemplo));

        // Act
        TipoAnuncioResponse response = service.obtenerPorId(1);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
    }

    @Test
    void obtenerPorId_SiNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(repo.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.obtenerPorId(99));
    }
}
