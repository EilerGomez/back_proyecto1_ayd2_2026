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
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.mantenimiento.MantenimientoSistemaService;
import com.e.gomez.Practica1AyD2.modelos.EntidadBloqueoAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.repositorios.BloqueoAnuncioRepositorio;
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
class BloqueoAnuncioServiceImplTest {

    @Mock private BloqueoAnuncioRepositorio repo;
    @Mock private PrecioBloqueoService precioBloqueoService;
    @Mock private CarteraService carteraService;
    @Mock private TransaccionCarteraService transaccionService;
    @Mock private MantenimientoSistemaService mantenimiento;

    @InjectMocks
    private BloqueoAnuncioServiceImpl service;

    private BloqueoAnuncioRequest request;
    private EntidadBloqueoAnuncio entidad;

    @BeforeEach
    void setUp() {
        request = new BloqueoAnuncioRequest();
        request.setRevistaId(1);
        request.setEditorId(5);
        request.setCarteraEditorId(10);
        request.setDias(3);

        entidad = EntidadBloqueoAnuncio.builder()
                .id(100)
                .dias(3)
                .transaccionId(1)
                .revistaId(1)
                .fechaInicio(LocalDateTime.now().minusDays(1))
                .fechaFin(LocalDateTime.now().plusDays(1))
                .estado("ACTIVO")
                .build();
    }

    @Test
    void contratarBloqueo_Ok_RealizaTransaccionesYGuarda() throws ExcepcionNoExiste {
        PrecioBloqueoResponse precio = new PrecioBloqueoResponse();
        precio.setCostoPorDia(new BigDecimal("10.00"));
        precio.setAdminId(2);
        when(precioBloqueoService.obtenerPorRevista(1)).thenReturn(precio);

        EntidadCartera carteraAdmin = new EntidadCartera();
        carteraAdmin.setId(20);
        when(carteraService.obtenerPorUsuario(2)).thenReturn(carteraAdmin);

        TransaccionResponse transRes = new TransaccionResponse();
        transRes.setId(999);
        when(transaccionService.registrar(any())).thenReturn(transRes);

        when(repo.save(any())).thenReturn(entidad);

        BloqueoAnuncioResponse res = service.contratarBloqueo(request);

        assertNotNull(res);

        verify(transaccionService, times(2)).registrar(any());
        verify(repo).save(any());
    }

    @Test
    void obtenerActivoPorRevista_Vigente_RetornaResponse() throws ExcepcionNoExiste {
        when(repo.findByRevistaIdAndEstado(1, "ACTIVO")).thenReturn(List.of(entidad));

        BloqueoAnuncioResponse res = service.obtenerActivoPorRevista(1);

        assertNotNull(res);
        verify(repo).findByRevistaIdAndEstado(1, "ACTIVO");
    }

    @Test
    void obtenerActivoPorRevista_Expirado_LanzaExcepcion() {
        entidad.setFechaInicio(LocalDateTime.now().minusDays(5));
        entidad.setFechaFin(LocalDateTime.now().minusDays(2)); // Ya expiró
        
        when(repo.findByRevistaIdAndEstado(1, "ACTIVO")).thenReturn(List.of(entidad));

        assertThrows(ExcepcionNoExiste.class, () -> service.obtenerActivoPorRevista(1));
    }


    @Test
    void actualizarFechaFin_SiFechaPasada_DisparaMantenimiento() throws ExcepcionNoExiste {
        when(repo.getById(100)).thenReturn(entidad);
        LocalDateTime fechaPasada = LocalDateTime.now().minusHours(1);

        service.actualizarFechaFin(100, fechaPasada);

        verify(repo).save(entidad);
        verify(mantenimiento).desactivarBloqueosDeAnunciosExpirados();
    }

    @Test
    void listarPorRevista_DeberiaMapearLista() {
        when(repo.findByRevistaId(1)).thenReturn(List.of(entidad));
        
        List<BloqueoAnuncioResponse> res = service.listarPorRevista(1);

        assertEquals(1, res.size());
        verify(repo).findByRevistaId(1);
    }
}
