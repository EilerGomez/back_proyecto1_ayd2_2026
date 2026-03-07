/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Anuncios.TEst;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.*;
import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.mantenimiento.MantenimientoSistemaService;
import com.e.gomez.Practica1AyD2.modelos.*;
import com.e.gomez.Practica1AyD2.repositorios.CompraAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import com.e.gomez.Practica1AyD2.servicios.*;
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
class CompraAnuncioServiceImplTest {

    @Mock private CompraAnuncioRepositorio repo;
    @Mock private AnuncioService anuncioService;
    @Mock private PrecioAnuncioService precioService;
    @Mock private TransaccionCarteraService transaccionService;
    @Mock private UsuarioService usuarioService;
    @Mock private CarteraService carteraService;
    @Mock private PerfilRepositorio perfilRepo;
    @Mock private MantenimientoSistemaService mantenimiento;

    @InjectMocks
    private CompraAnuncioServiceImpl service;

    private CompraAnuncioRequest request;
    private EntidadCompraAnuncio compraEntidad;
    private PrecioAnuncioResponse precioInfo;

    @BeforeEach
    void setUp() {
        request = new CompraAnuncioRequest();
        request.setAnuncioId(1);
        request.setAnuncianteId(10);
        request.setPrecioId(5);
        request.setCarteraId(100);
        request.setFechaInicio(LocalDateTime.now());

        compraEntidad = EntidadCompraAnuncio.builder()
                .id(500)
                .anuncioId(1)
                .anuncianteId(10)
                .precioId(5)
                .estado("ACTIVO")
                .build();

        PeriodoAnuncioResponse periodo = new PeriodoAnuncioResponse();
        periodo.setDias(7);
        
        UsuarioResponse admin = new UsuarioResponse();
        admin.setId(2);

        precioInfo = new PrecioAnuncioResponse();
        precioInfo.setPeriodoAnuncio(periodo);
        precioInfo.setAdmin(admin);
        precioInfo.setPrecio(new BigDecimal("150.00"));
    }

    @Test
    void comprar_Ok_RealizaTransaccionesYActivaAnuncio() throws ExcepcionNoExiste {
        when(precioService.obtenerPorId(5)).thenReturn(precioInfo);
        
        EntidadCartera carteraAdmin = new EntidadCartera();
        carteraAdmin.setId(200);
        when(carteraService.obtenerPorUsuario(2)).thenReturn(carteraAdmin);

        TransaccionResponse tRes = new TransaccionResponse();
        tRes.setId(77);
        when(transaccionService.registrar(any())).thenReturn(tRes);

        when(repo.save(any())).thenReturn(compraEntidad);

        EntidadUsuario eu = new EntidadUsuario();
        eu.setId(10);
        when(usuarioService.getById(10)).thenReturn(eu);
        when(perfilRepo.findByUsuarioId(10)).thenReturn(Optional.of(new EntidadPerfil()));

        // Act
        CompraAnuncioResponseSimple res = service.comprar(request);

        // Assert
        assertNotNull(res);
        verify(transaccionService, times(2)).registrar(any()); // Débito Anunciante y Crédito Admin
        verify(anuncioService).cambiarEstado(1, "ACTIVO");
        verify(repo).save(any(EntidadCompraAnuncio.class));
    }

    @Test
    void desactivarManualmente_Ok_CambiaEstados() throws ExcepcionNoExiste {
        when(repo.findById(500)).thenReturn(Optional.of(compraEntidad));

        service.desactivarManualmente(500, "ADMIN_TEST", LocalDateTime.now());

        assertEquals("INACTIVO", compraEntidad.getEstado());
        assertEquals("ADMIN_TEST", compraEntidad.getDesactivadoPor());
        verify(anuncioService).cambiarEstado(1, "INACTIVO");
        verify(repo).save(compraEntidad);
    }


    @Test
    void listarPorAnunciante_DeberiaRetornarListaDetallada() throws ExcepcionNoExiste {
        when(repo.findByAnuncianteId(10)).thenReturn(List.of(compraEntidad));
        
        when(precioService.obtenerPorId(anyInt())).thenReturn(precioInfo);
        EntidadUsuario eu = new EntidadUsuario();
        eu.setId(10);
        when(usuarioService.getById(10)).thenReturn(eu);
        when(perfilRepo.findByUsuarioId(10)).thenReturn(Optional.of(new EntidadPerfil()));
        when(anuncioService.obtenerPorId(1)).thenReturn(new AnuncioResponse());

        List<CompraAnuncioResponseDetallado> res = service.listarPorAnunciante(10);

        assertFalse(res.isEmpty());
        assertEquals(1, res.size());
    }

    @Test
    void mapToDetalladoResponse_CuandoFalla_RetornaNull() throws ExcepcionNoExiste {
        when(repo.findByEstado("ACTIVO")).thenReturn(List.of(compraEntidad));
        when(precioService.obtenerPorId(anyInt())).thenThrow(new RuntimeException("Error fatal"));

        List<CompraAnuncioResponseDetallado> res = service.listarPorEstado("ACTIVO");

        assertTrue(res.contains(null));
    }

    @Test
    void cambiarFechaFin_DeberiaActualizarYEjecutarMantenimiento() throws ExcepcionNoExiste {
        when(repo.getById(500)).thenReturn(compraEntidad);
        when(precioService.obtenerPorId(anyInt())).thenReturn(precioInfo);
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(10);

        service.CambiarFechaFin(500, nuevaFecha);

        assertEquals(nuevaFecha, compraEntidad.getFechaFin());
        verify(mantenimiento).desactivarAnunciosExpirados();
        verify(repo).save(compraEntidad);
    }
}
