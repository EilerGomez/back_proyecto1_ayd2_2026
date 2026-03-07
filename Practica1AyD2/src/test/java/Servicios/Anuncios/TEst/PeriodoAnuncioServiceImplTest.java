/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Anuncios.TEst;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.PeriodoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPeriodoAnuncio;
import com.e.gomez.Practica1AyD2.repositorios.PeriodoAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.servicios.PeriodoAnuncioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeriodoAnuncioServiceImplTest {

    @Mock
    private PeriodoAnuncioRepositorio repo;

    @InjectMocks
    private PeriodoAnuncioServiceImpl service;

    private EntidadPeriodoAnuncio periodoEjemplo;

    @BeforeEach
    void setUp() {
        periodoEjemplo = new EntidadPeriodoAnuncio();
        periodoEjemplo.setId(1);
        periodoEjemplo.setCodigo("3_DIAS");
        periodoEjemplo.setDias(3);
    }


    @Test
    void listarTodos_DeberiaMapearListaCorrectamente() {
        // Arrange
        when(repo.findAll()).thenReturn(List.of(periodoEjemplo));

        // Act
        List<PeriodoAnuncioResponse> resultado = service.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("3_DIAS", resultado.get(0).getCodigo());
        verify(repo).findAll();
    }


    @Test
    void obtenerPorCodigo_SiExiste_DeberiaRetornarResponse() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findByCodigo("3_DIAS")).thenReturn(Optional.of(periodoEjemplo));

        // Act
        PeriodoAnuncioResponse response = service.obtenerPorCodigo("3_DIAS");

        // Assert
        assertNotNull(response);
        assertEquals("3_DIAS", response.getCodigo());
        assertEquals(1, response.getId());
    }

    @Test
    void obtenerPorCodigo_SiNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(repo.findByCodigo(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.obtenerPorCodigo("CODIGO_INVALIDO"));
    }


    @Test
    void obtenerPorId_SiExiste_DeberiaRetornarResponse() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findById(1)).thenReturn(Optional.of(periodoEjemplo));

        // Act
        PeriodoAnuncioResponse response = service.obtenerPorId(1);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(3, response.getDias());
    }

    @Test
    void obtenerPorId_SiNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(repo.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.obtenerPorId(99));
    }
}
