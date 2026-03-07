/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Revistas.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoResponse;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.PagoRevistaRequest;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.PagoRevistaResponse;
import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.mantenimiento.MantenimientoSistemaService;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.modelos.EntidadPagoRevista;
import com.e.gomez.Practica1AyD2.repositorios.PagoRevistaRepositorio;
import com.e.gomez.Practica1AyD2.servicios.*;
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
class PagoRevistaServiceImplTest {

    @Mock private PagoRevistaRepositorio pagoRepo;
    @Mock private TransaccionCarteraService transaccionService;
    @Mock private RevistaService revistaService;
    @Mock private HistorialCostoService historialCostoService;
    @Mock private CarteraService carteraService;
    @Mock private MantenimientoSistemaService mantenimiento;

    @InjectMocks
    private PagoRevistaServiceImpl service;

    private PagoRevistaRequest request;
    private EntidadPagoRevista pagoEntidad;

    @BeforeEach
    void setUp() {
        request = new PagoRevistaRequest();
        request.setRevistaId(1);
        request.setEditorId(10);
        request.setPeriodoInicio(LocalDate.now());
        request.setPeriodoFin(LocalDate.now().plusDays(30));
        request.setMonto(new BigDecimal("300.00"));

        pagoEntidad = new EntidadPagoRevista();
        pagoEntidad.setId(500);
        pagoEntidad.setRevistaId(1);
        pagoEntidad.setEditorId(10);
        pagoEntidad.setTransaccionId(1);
    }


    @Test
    void procesarPago_Ok_RealizaDebitoCreditoYActivaRevista() throws ExcepcionNoExiste {
        HistorialCostoResponse hcr = new HistorialCostoResponse();
        hcr.setCostoPorDia(new BigDecimal("10.00"));
        hcr.setAdminId(2);
        when(historialCostoService.obtenerCostoVigente(1)).thenReturn(hcr);

        TransaccionResponse tRes = new TransaccionResponse();
        tRes.setId(99);
        when(transaccionService.registrar(any())).thenReturn(tRes);

        EntidadCartera carteraAdmin = new EntidadCartera();
        carteraAdmin.setId(20);
        when(carteraService.obtenerPorUsuario(2)).thenReturn(carteraAdmin);

        when(pagoRepo.save(any(EntidadPagoRevista.class))).thenReturn(pagoEntidad);

        PagoRevistaResponse res = service.procesarPago(request, 5);

        // Assert
        assertNotNull(res);
        assertEquals(500, res.getId());
        verify(transaccionService, times(2)).registrar(any()); 
        verify(revistaService).cambiarEstado(1, true); 
    }

    @Test
    void procesarPago_FechaFinAnterior_LanzaIllegalArgument() {
        request.setPeriodoFin(LocalDate.now().minusDays(1));
        assertThrows(IllegalArgumentException.class, () -> service.procesarPago(request, 1));
    }


    @Test
    void listarPagosPorRevista_DeberiaRetornarLista() throws ExcepcionNoExiste {
        when(pagoRepo.findByRevistaId(1)).thenReturn(List.of(pagoEntidad));
        List<PagoRevistaResponse> res = service.listarPagosPorRevista(1);
        assertEquals(1, res.size());
    }

    @Test
    void listarPagosPorEditor_DeberiaRetornarLista() throws ExcepcionNoExiste {
        when(pagoRepo.findByEditorId(10)).thenReturn(List.of(pagoEntidad));
        List<PagoRevistaResponse> res = service.listarPagosPorEditor(10);
        assertEquals(1, res.size());
    }


    @Test
    void actualizarFechaFin_Ok_DisparaMantenimiento() throws ExcepcionNoExiste {
        when(pagoRepo.findById(500)).thenReturn(Optional.of(pagoEntidad));
        
        service.actualizarFechaFin(500, LocalDate.now().plusMonths(1));

        verify(pagoRepo).save(pagoEntidad);
        verify(mantenimiento).desactivarRevistasVencidas();
    }

    @Test
    void actualizarFechaFin_NoExiste_LanzaExcepcion() {
        when(pagoRepo.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ExcepcionNoExiste.class, () -> service.actualizarFechaFin(99, LocalDate.now()));
    }
}
