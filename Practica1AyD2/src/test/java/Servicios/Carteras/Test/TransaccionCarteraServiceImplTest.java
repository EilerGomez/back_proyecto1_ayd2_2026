/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Carteras.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionRequest;
import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadTransaccionCartera;
import com.e.gomez.Practica1AyD2.repositorios.TransaccionCarteraRepositorio;
import com.e.gomez.Practica1AyD2.servicios.CarteraService;
import com.e.gomez.Practica1AyD2.servicios.TransaccionCarteraServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionCarteraServiceImplTest {

    @Mock
    private TransaccionCarteraRepositorio repo;

    @Mock
    private CarteraService carteraService;

    @InjectMocks
    private TransaccionCarteraServiceImpl service;

    private TransaccionRequest request;

    @BeforeEach
    void setUp() {
        request = new TransaccionRequest();
        request.setCarteraId(1);
        request.setMonto(new BigDecimal("100.00"));
        request.setTipo("RECARGA");
        request.setNota("Prueba");
    }


    @Test
    void registrar_Credito_DeberiaSumarSaldoYGuardar() throws ExcepcionNoExiste {
        // Arrange
        request.setDireccion("CREDITO");
        when(repo.save(any(EntidadTransaccionCartera.class))).thenAnswer(i -> {
            EntidadTransaccionCartera t = i.getArgument(0);
            t.setId(500); // Evita NPE en el Response
            return t;
        });

        // Act
        TransaccionResponse res = service.registrar(request);

        // Assert
        assertNotNull(res);
        assertEquals(500, res.getId());
        verify(carteraService).sumarSaldo(eq(1), eq(new BigDecimal("100.00")));
        verify(repo).save(any(EntidadTransaccionCartera.class));
    }

    @Test
    void registrar_Debito_DeberiaDebitarYGuardar() throws ExcepcionNoExiste {
        // Arrange
        request.setDireccion("DEBITO");
        when(repo.save(any(EntidadTransaccionCartera.class))).thenAnswer(i -> {
            EntidadTransaccionCartera t = i.getArgument(0);
            t.setId(600);
            return t;
        });

        // Act
        TransaccionResponse res = service.registrar(request);

        // Assert
        assertNotNull(res);
        verify(carteraService).debitar(eq(1), eq(new BigDecimal("100.00")));
        verify(repo).save(any(EntidadTransaccionCartera.class));
    }

    @Test
    void registrar_DireccionInvalida_LanzaIllegalArgumentException() {
        // Arrange
        request.setDireccion("INVALIDO");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> service.registrar(request));
        verify(repo, never()).save(any());
    }


    @Test
    void listarPorCartera_DeberiaRetornarListaMapeada() {
        // Arrange
        EntidadTransaccionCartera t = new EntidadTransaccionCartera();
        t.setId(1);
        t.setMonto(BigDecimal.TEN);
        t.setDireccion("CREDITO");
        
        when(repo.findByCarteraIdOrderByFechaCreacionDesc(1)).thenReturn(List.of(t));

        // Act
        List<TransaccionResponse> lista = service.listarPorCartera(1);

        // Assert
        assertNotNull(lista);
        assertEquals(1, lista.size());
        assertEquals(1, lista.get(0).getId());
    }

    @Test
    void obtenerPorId_SiExiste_RetornaResponse() throws ExcepcionNoExiste {
        // Arrange
        EntidadTransaccionCartera t = new EntidadTransaccionCartera();
        t.setId(99);
        t.setMonto(BigDecimal.ONE);
        t.setDireccion("DEBITO");
        
        when(repo.findById(99)).thenReturn(Optional.of(t));

        // Act
        TransaccionResponse res = service.obtenerPorId(99);

        // Assert
        assertNotNull(res);
        assertEquals(99, res.getId());
    }

    @Test
    void obtenerPorId_SiNoExiste_LanzaExcepcionNoExiste() {
        // Arrange
        when(repo.findById(anyInt())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.obtenerPorId(77));
    }
}
