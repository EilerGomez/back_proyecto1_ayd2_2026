/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Carteras.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.repositorios.CarteraRepositorio;
import com.e.gomez.Practica1AyD2.servicios.CarteraServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class CarteraServiceImplTest {

    @Mock
    private CarteraRepositorio carteraRepositorio;

    @InjectMocks
    private CarteraServiceImpl carteraService;



    // -------- crearCartera --------

    @Test
    void crearCartera_cuandoUsuarioNoTieneCartera_laCreaConSaldoCeroYMonedaGTQ() {
        Integer usuarioId = 10;

        when(carteraRepositorio.existsByUsuarioId(usuarioId)).thenReturn(false);

        // Simulamos que save devuelve la misma entidad pero ya guardada
        when(carteraRepositorio.save(any(EntidadCartera.class)))
                .thenAnswer(inv -> inv.getArgument(0, EntidadCartera.class));

        EntidadCartera creada = carteraService.crearCartera(usuarioId);

        assertNotNull(creada);
        assertEquals(usuarioId, creada.getUsuarioId());
        assertEquals(new BigDecimal("0.0"), creada.getSaldo()); 
        assertEquals("GTQ", creada.getMoneda());

        ArgumentCaptor<EntidadCartera> captor = ArgumentCaptor.forClass(EntidadCartera.class);
        verify(carteraRepositorio).save(captor.capture());
        assertEquals(usuarioId, captor.getValue().getUsuarioId());
        assertEquals("GTQ", captor.getValue().getMoneda());
    }

    @Test
    void crearCartera_cuandoUsuarioYaTieneCartera_lanzaRuntimeException() {
        Integer usuarioId = 10;
        when(carteraRepositorio.existsByUsuarioId(usuarioId)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> carteraService.crearCartera(usuarioId));

        assertTrue(ex.getMessage().toLowerCase().contains("ya tiene una cartera"));
        verify(carteraRepositorio, never()).save(any());
    }

    // -------- obtenerPorUsuario --------

    @Test
    void obtenerPorUsuario_cuandoExiste_laDevuelve() throws Exception {
        Integer usuarioId = 7;
        EntidadCartera cartera = new EntidadCartera();
        cartera.setUsuarioId(usuarioId);

        when(carteraRepositorio.findByUsuarioId(usuarioId)).thenReturn(Optional.of(cartera));

        EntidadCartera res = carteraService.obtenerPorUsuario(usuarioId);

        assertNotNull(res);
        assertEquals(usuarioId, res.getUsuarioId());
        verify(carteraRepositorio).findByUsuarioId(usuarioId);
    }

    @Test
    void obtenerPorUsuario_cuandoNoExiste_lanzaExcepcionNoExiste() throws ExcepcionNoExiste {
        Integer usuarioId = 7;
        when(carteraRepositorio.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());

        ExcepcionNoExiste ex = assertThrows(ExcepcionNoExiste.class,
                () -> carteraService.obtenerPorUsuario(usuarioId));

        assertTrue(ex.getMessage().contains("No existe la cartera"));
        verify(carteraRepositorio).findByUsuarioId(usuarioId);
    }

    // -------- sumarSaldo --------

    @Test
    void sumarSaldo_cuandoRepoActualiza_ok() {
        Integer carteraId = 1;
        BigDecimal delta = new BigDecimal("25.50");

        when(carteraRepositorio.sumarSaldo(carteraId, delta)).thenReturn(1);

        assertDoesNotThrow(() -> carteraService.sumarSaldo(carteraId, delta));
        verify(carteraRepositorio).sumarSaldo(carteraId, delta);
    }

    @Test
    void sumarSaldo_cuandoRepoDevuelve0_lanzaRuntimeException() {
        Integer carteraId = 1;
        BigDecimal delta = new BigDecimal("25.50");

        when(carteraRepositorio.sumarSaldo(carteraId, delta)).thenReturn(0);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> carteraService.sumarSaldo(carteraId, delta));

        assertTrue(ex.getMessage().toLowerCase().contains("no se pudo actualizar"));
        verify(carteraRepositorio).sumarSaldo(carteraId, delta);
    }

    // -------- debitar --------

    @Test
    void debitar_cuandoAlcanza_ok() {
        Integer carteraId = 2;
        BigDecimal monto = new BigDecimal("10.00");

        when(carteraRepositorio.debitarSiAlcanza(carteraId, monto)).thenReturn(1);

        assertDoesNotThrow(() -> carteraService.debitar(carteraId, monto));
        verify(carteraRepositorio).debitarSiAlcanza(carteraId, monto);
    }

    @Test
    void debitar_cuandoNoAlcanzaOnoExiste_lanzaRuntimeException() {
        Integer carteraId = 2;
        BigDecimal monto = new BigDecimal("999.99");

        when(carteraRepositorio.debitarSiAlcanza(carteraId, monto)).thenReturn(0);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> carteraService.debitar(carteraId, monto));

        assertTrue(ex.getMessage().toLowerCase().contains("fondos insuficientes"));
        verify(carteraRepositorio).debitarSiAlcanza(carteraId, monto);
    }
}
