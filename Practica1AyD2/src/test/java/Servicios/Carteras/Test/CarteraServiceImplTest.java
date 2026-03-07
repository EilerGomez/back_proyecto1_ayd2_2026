package Servicios.Carteras.Test;

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

    // CREACION

    @Test
    void crearCartera_cuandoUsuarioNoTieneCartera_laCreaConSaldoCeroYMonedaGTQ() {
        Integer usuarioId = 10;
        when(carteraRepositorio.existsByUsuarioId(usuarioId)).thenReturn(false);
        when(carteraRepositorio.save(any(EntidadCartera.class)))
                .thenAnswer(inv -> inv.getArgument(0, EntidadCartera.class));

        EntidadCartera creada = carteraService.crearCartera(usuarioId);

        assertNotNull(creada);
        assertEquals(usuarioId, creada.getUsuarioId());
        assertEquals(0, BigDecimal.valueOf(0.0).compareTo(creada.getSaldo())); 
        assertEquals("GTQ", creada.getMoneda());
        verify(carteraRepositorio).save(any(EntidadCartera.class));
    }

    @Test
    void crearCartera_cuandoUsuarioYaTieneCartera_lanzaRuntimeException() {
        Integer usuarioId = 10;
        when(carteraRepositorio.existsByUsuarioId(usuarioId)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> carteraService.crearCartera(usuarioId));
        verify(carteraRepositorio, never()).save(any());
    }

    //OBTENCION

    @Test
    void obtenerPorUsuario_cuandoExiste_laDevuelve() throws Exception {
        Integer usuarioId = 7;
        EntidadCartera cartera = new EntidadCartera();
        cartera.setUsuarioId(usuarioId);

        when(carteraRepositorio.findByUsuarioId(usuarioId)).thenReturn(Optional.of(cartera));

        EntidadCartera res = carteraService.obtenerPorUsuario(usuarioId);

        assertEquals(usuarioId, res.getUsuarioId());
    }

    @Test
    void obtenerPorUsuario_cuandoNoExiste_lanzaExcepcionNoExiste() throws ExcepcionNoExiste {
        Integer usuarioId = 7;
        when(carteraRepositorio.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> carteraService.obtenerPorUsuario(usuarioId));
    }

    // SUMAR

    @Test
    void sumarSaldo_cuandoRepoActualiza_noLanzaExcepcion() {
        Integer carteraId = 1;
        BigDecimal delta = new BigDecimal("25.50");
        when(carteraRepositorio.sumarSaldo(carteraId, delta)).thenReturn(1);

        assertDoesNotThrow(() -> carteraService.sumarSaldo(carteraId, delta));
    }

    @Test
    void sumarSaldo_cuandoNoEncuentraCartera_lanzaRuntimeException() {
        Integer carteraId = 1;
        when(carteraRepositorio.sumarSaldo(anyInt(), any(BigDecimal.class))).thenReturn(0);

        assertThrows(RuntimeException.class, () -> carteraService.sumarSaldo(carteraId, BigDecimal.TEN));
    }

    //DEBITAR

    @Test
    void debitar_cuandoSiAlcanza_noLanzaExcepcion() {
        Integer carteraId = 2;
        BigDecimal monto = new BigDecimal("10.00");
        when(carteraRepositorio.debitarSiAlcanza(carteraId, monto)).thenReturn(1);

        assertDoesNotThrow(() -> carteraService.debitar(carteraId, monto));
    }

    @Test
    void debitar_cuandoNoAlcanza_lanzaRuntimeException() {
        Integer carteraId = 2;
        when(carteraRepositorio.debitarSiAlcanza(anyInt(), any(BigDecimal.class))).thenReturn(0);

        assertThrows(RuntimeException.class, () -> carteraService.debitar(carteraId, BigDecimal.valueOf(999)));
    }
}