/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.PrecioBloqueo.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioBloqueoRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioBloqueoResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPrecioBloqueo;
import com.e.gomez.Practica1AyD2.repositorios.PrecioBloqueoRepositorio;
import com.e.gomez.Practica1AyD2.servicios.PrecioBloqueoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrecioBloqueoServiceImplTest {

    @Mock
    private PrecioBloqueoRepositorio repo;

    @InjectMocks
    private PrecioBloqueoServiceImpl service;

    private PrecioBloqueoRequest request;
    private EntidadPrecioBloqueo entidadExistente;

    @BeforeEach
    void setUp() {
        request = new PrecioBloqueoRequest();
        request.setRevistaId(1);
        request.setCostoPorDia(new BigDecimal("15.50"));
        request.setAdminId(10);

        entidadExistente = new EntidadPrecioBloqueo();
        entidadExistente.setId(100);
        entidadExistente.setAdminId(10);
        entidadExistente.setRevistaId(1);
        entidadExistente.setCostoPorDia(new BigDecimal("10.00"));
    }


    @Test
    void asignarOActualizar_CuandoNoExiste_DeberiaCrearNuevo() {
       // Arrange
        when(repo.findByRevistaId(1)).thenReturn(Optional.empty());
        
        when(repo.save(any(EntidadPrecioBloqueo.class))).thenAnswer(i -> {
            EntidadPrecioBloqueo nueva = i.getArgument(0);
            nueva.setId(500);
            return nueva;
        });

        // Act
        PrecioBloqueoResponse res = service.asignarOActualizar(request);

        // Assert
        assertNotNull(res);
        assertEquals(500, res.getId());
        assertEquals(new BigDecimal("15.50"), res.getCostoPorDia());
        verify(repo).save(any(EntidadPrecioBloqueo.class));
    }

    @Test
    void asignarOActualizar_CuandoYaExiste_DeberiaActualizarExistente() {
        // Arrange: 
        when(repo.findByRevistaId(1)).thenReturn(Optional.of(entidadExistente));
        when(repo.save(any(EntidadPrecioBloqueo.class))).thenReturn(entidadExistente);

        // Act
        PrecioBloqueoResponse res = service.asignarOActualizar(request);

        // Assert
        assertEquals(new BigDecimal("15.50"), entidadExistente.getCostoPorDia());
        verify(repo).save(entidadExistente);
    }


    @Test
    void obtenerPorRevista_SiExiste_DeberiaRetornarResponse() throws ExcepcionNoExiste {
        when(repo.findByRevistaId(1)).thenReturn(Optional.of(entidadExistente));

        PrecioBloqueoResponse res = service.obtenerPorRevista(1);

        assertNotNull(res);
        assertEquals(new BigDecimal("10.00"), res.getCostoPorDia());
    }

    @Test
    void obtenerPorRevista_SiNoExiste_DeberiaLanzarExcepcion() {
        when(repo.findByRevistaId(anyInt())).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.obtenerPorRevista(99));
    }


    @Test
    void eliminarPrecio_SiExiste_DeberiaLlamarDelete() throws ExcepcionNoExiste {
        when(repo.existsById(100)).thenReturn(true);

        service.eliminarPrecio(100);

        verify(repo).deleteById(100);
    }

    @Test
    void eliminarPrecio_SiNoExiste_DeberiaLanzarExcepcion() {
        when(repo.existsById(anyInt())).thenReturn(false);

        assertThrows(ExcepcionNoExiste.class, () -> service.eliminarPrecio(999));
        verify(repo, never()).deleteById(anyInt());
    }
}
