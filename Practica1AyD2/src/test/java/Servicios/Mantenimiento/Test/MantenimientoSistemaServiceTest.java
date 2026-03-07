/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Mantenimiento.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.mantenimiento.MantenimientoSistemaService;
import com.e.gomez.Practica1AyD2.modelos.EntidadPagoRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.repositorios.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MantenimientoSistemaServiceTest {

    @Mock
    private RevistaRepositorio revistaRepo;
    @Mock
    private PagoRevistaRepositorio pagoRepo;
    @Mock
    private BloqueoAnuncioRepositorio bloqueoRepo;
    @Mock
    private CompraAnuncioRepositorio repoCompraAnuncio;
    @Mock
    private AnuncioRepositorio anuncioRepo;

    @InjectMocks
    private MantenimientoSistemaService mantenimientoService;

    private EntidadRevista revistaVencida;
    private EntidadRevista revistaVigente;

    @BeforeEach
    void setUp() {
        revistaVencida = new EntidadRevista();
        revistaVencida.setId(1);
        revistaVencida.setActiva(true);

        revistaVigente = new EntidadRevista();
        revistaVigente.setId(2);
        revistaVigente.setActiva(true);
    }

    @Test
    void desactivarRevistasVencidas_DeberiaInactivarSoloRevistasSinPagosVigentes() throws Exception {
        when(revistaRepo.findByActivaTrue()).thenReturn(Arrays.asList(revistaVencida, revistaVigente));

        EntidadPagoRevista pagoExpirado = new EntidadPagoRevista();
        pagoExpirado.setPeriodoInicio(LocalDate.now().minusDays(10));
        pagoExpirado.setPeriodoFin(LocalDate.now().minusDays(1));

        EntidadPagoRevista pagoActivo = new EntidadPagoRevista();
        pagoActivo.setPeriodoInicio(LocalDate.now().minusDays(5));
        pagoActivo.setPeriodoFin(LocalDate.now().plusDays(5));

        when(pagoRepo.findByRevistaId(1)).thenReturn(Collections.singletonList(pagoExpirado));
        when(pagoRepo.findByRevistaId(2)).thenReturn(Collections.singletonList(pagoActivo));

        mantenimientoService.desactivarRevistasVencidas();

        assertFalse(revistaVencida.isActiva(), "La revista 1 debería estar inactiva");
        assertTrue(revistaVigente.isActiva(), "La revista 2 debería seguir activa");

        verify(revistaRepo, times(1)).save(revistaVencida);
        verify(revistaRepo, never()).save(revistaVigente);
    }

    @Test
    void desactivarBloqueosDeAnunciosExpirados_DeberiaLlamarAlRepositorio() {
        when(bloqueoRepo.desactivarBloqueosExpirados(any(LocalDateTime.class))).thenReturn(5);

        mantenimientoService.desactivarBloqueosDeAnunciosExpirados();

        verify(bloqueoRepo, times(1)).desactivarBloqueosExpirados(any(LocalDateTime.class));
    }

    @Test
    void desactivarAnunciosExpirados_DeberiaLlamarARepositoriosDeAnunciosYCompras() {
        when(anuncioRepo.expirarAnunciosVencidos(any(LocalDateTime.class))).thenReturn(10);
        when(repoCompraAnuncio.expirarComprasVencidas(any(LocalDateTime.class))).thenReturn(3);

        mantenimientoService.desactivarAnunciosExpirados();

        verify(anuncioRepo, times(1)).expirarAnunciosVencidos(any(LocalDateTime.class));
        verify(repoCompraAnuncio, times(1)).expirarComprasVencidas(any(LocalDateTime.class));
    }
}