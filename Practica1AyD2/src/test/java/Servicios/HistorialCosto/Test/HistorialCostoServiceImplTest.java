/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.HistorialCosto.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoRequest;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadHistorialCosto;
import com.e.gomez.Practica1AyD2.repositorios.HistorialCostoRepositorio;
import com.e.gomez.Practica1AyD2.servicios.HistorialCostoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HistorialCostoServiceImplTest {

    @Mock
    private HistorialCostoRepositorio repo;

    @InjectMocks
    private HistorialCostoServiceImpl service;

    private HistorialCostoRequest request;
    private EntidadHistorialCosto costoEjemplo;

    @BeforeEach
    void setUp() {
        request = new HistorialCostoRequest();
        request.setRevistaId(1);
        request.setAdminId(10);
        request.setCostoPorDia(new BigDecimal("25.00"));
        request.setFechaInicio(LocalDate.now());

        costoEjemplo = new EntidadHistorialCosto();
        costoEjemplo.setId(100);
        costoEjemplo.setRevistaId(1);
        costoEjemplo.setCostoPorDia(new BigDecimal("20.00"));
        costoEjemplo.setFechaInicio(LocalDate.now().minusMonths(1));
        costoEjemplo.setFechaFin(null);
        costoEjemplo.setAdminId(10);
    }


    @Test
    void asignarNuevoCosto_CuandoYaExisteUnoVigente_DeberiaFinalizarAnteriorYCrearNuevo() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findByRevistaIdAndFechaFinIsNull(1)).thenReturn(Optional.of(costoEjemplo));
        when(repo.save(any(EntidadHistorialCosto.class))).thenAnswer(i -> {
            EntidadHistorialCosto c = i.getArgument(0);
            if (c.getId() == null) c.setId(200);
            return c;
        });

        // Act
        HistorialCostoResponse res = service.asignarNuevoCosto(request);

        // Assert
        assertNotNull(res);
        assertEquals(200, res.getId());
        assertNotNull(costoEjemplo.getFechaFin()); 
        verify(repo, times(2)).save(any(EntidadHistorialCosto.class));
    }

    @Test
    void asignarNuevoCosto_CuandoNoHayCostoPrevio_DeberiaCrearSoloElNuevo() throws ExcepcionNoExiste {
        // Arrange
        when(repo.findByRevistaIdAndFechaFinIsNull(1)).thenReturn(Optional.empty());
        when(repo.save(any(EntidadHistorialCosto.class))).thenAnswer(i -> {
            EntidadHistorialCosto c = i.getArgument(0);
            c.setId(300);
            return c;
        });

        // Act
        HistorialCostoResponse res = service.asignarNuevoCosto(request);

        // Assert
        assertNotNull(res);
        assertEquals(300, res.getId());
        verify(repo, times(1)).save(any(EntidadHistorialCosto.class));
    }


    @Test
    void obtenerCostoVigente_SiExiste_DeberiaRetornarResponse() throws ExcepcionNoExiste {
        when(repo.findByRevistaIdAndFechaFinIsNull(1)).thenReturn(Optional.of(costoEjemplo));

        HistorialCostoResponse res = service.obtenerCostoVigente(1);

        assertNotNull(res);
        assertEquals(100, res.getId());
    }

    @Test
    void obtenerCostoVigente_SiNoExiste_DeberiaLanzarExcepcion() throws ExcepcionNoExiste {
        when(repo.findByRevistaIdAndFechaFinIsNull(anyInt())).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.obtenerCostoVigente(1));
    }

    @Test
    void obtenerHistorialPorRevista_DeberiaMapearLista() throws ExcepcionNoExiste {
        when(repo.findByRevistaId(1)).thenReturn(List.of(costoEjemplo));

        List<HistorialCostoResponse> res = service.obtenerHistorialPorRevista(1);

        assertEquals(1, res.size());
        assertEquals(100, res.get(0).getId());
        verify(repo).findByRevistaId(1);
    }
}
