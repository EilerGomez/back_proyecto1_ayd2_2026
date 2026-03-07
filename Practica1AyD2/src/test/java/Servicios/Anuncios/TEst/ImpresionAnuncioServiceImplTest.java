/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Anuncios.TEst;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.ImpresionAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.ImpresionAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadImpresionAnuncio;
import com.e.gomez.Practica1AyD2.repositorios.CompraAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.ImpresionAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.servicios.ImpresionAnuncioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImpresionAnuncioServiceImplTest {

    @Mock
    private ImpresionAnuncioRepositorio repo;

    @Mock
    private CompraAnuncioRepositorio compraService;

    @InjectMocks
    private ImpresionAnuncioServiceImpl service;

    private ImpresionAnuncioRequest request;

    @BeforeEach
    void setUp() {
        request = new ImpresionAnuncioRequest();
        request.setAnuncioId(1);
        request.setRevistaId(10);
        request.setUrlPagina("/revista/10");
    }


    @Test
    void registrarImpresion_ConCompraActiva_DeberiaGuardarCorrectamente() throws ExcepcionNoExiste {
        // Arrange
        EntidadCompraAnuncio compraActiva = new EntidadCompraAnuncio();
        compraActiva.setId(100);
        compraActiva.setEstado("ACTIVO");

        when(compraService.findByAnuncioId(1)).thenReturn(List.of(compraActiva));

        // Act
        assertDoesNotThrow(() -> service.registrarImpresion(request));

        // Assert
        verify(repo).save(any(EntidadImpresionAnuncio.class));
    }

    @Test
    void registrarImpresion_SinCompraActiva_DeberiaLanzarNullPointerException() throws ExcepcionNoExiste {
        // Arrange
        EntidadCompraAnuncio compraInactiva = new EntidadCompraAnuncio();
        compraInactiva.setEstado("EXPIRADO");

        when(compraService.findByAnuncioId(1)).thenReturn(List.of(compraInactiva));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> service.registrarImpresion(request));
        verify(repo, never()).save(any());
    }


    @Test
    void obtenerTotalVistasPorAnuncio_DeberiaRetornarConteo() {
        // Arrange
        when(repo.countByAnuncioId(1)).thenReturn(50L);

        // Act
        long total = service.obtenerTotalVistasPorAnuncio(1);

        // Assert
        assertEquals(50L, total);
        verify(repo).countByAnuncioId(1);
    }

    @Test
    void listarPorRevista_DeberiaRetornarListaMapeada() {
        // Arrange
        EntidadImpresionAnuncio impresion = new EntidadImpresionAnuncio();
        impresion.setId(1);
        impresion.setAnuncioId(1);
        
        when(repo.findByRevistaId(10)).thenReturn(List.of(impresion));

        // Act
        List<ImpresionAnuncioResponse> lista = service.listarPorRevista(10);

        // Assert
        assertNotNull(lista);
        assertEquals(1, lista.size());
        verify(repo).findByRevistaId(10);
    }
}
