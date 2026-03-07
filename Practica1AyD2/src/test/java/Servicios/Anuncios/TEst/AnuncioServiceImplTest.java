/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Anuncios.TEst;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoAnuncios.TipoAnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.mantenimiento.MantenimientoSistemaService;
import com.e.gomez.Practica1AyD2.modelos.*;
import com.e.gomez.Practica1AyD2.repositorios.*;
import com.e.gomez.Practica1AyD2.servicios.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnuncioServiceImplTest {

    @Mock private AnuncioRepositorio repo;
    @Mock private UsuarioService usuarioService;
    @Mock private TipoAnuncioService tipoAnuncioService;
    @Mock private RevistaRepositorio revistaRepo;
    @Mock private BloqueoAnuncioRepositorio bloqueoAnuncioRepo;
    @Mock private PerfilRepositorio perfilRepo;
    @Mock private CompraAnuncioRepositorio compraRepo;
    @Mock private MantenimientoSistemaService mantenimiento;

    @InjectMocks
    private AnuncioServiceImpl service;

    private EntidadAnuncio anuncioBase;
    private AnuncioRequest request;

    @BeforeEach
    void setUp() {
        anuncioBase = EntidadAnuncio.builder()
                .id(1)
                .anuncianteId(10)
                .tipoAnuncioId(5)
                .estado("BORRADOR")
                .build();

        request = new AnuncioRequest();
        request.setAnuncianteId(10);
        request.setTipoAnuncioId(5);
        request.setTexto("Promo Test");
    }

    @Test
    void crear_DeberiaGuardarAnuncio() throws ExcepcionNoExiste {
        when(repo.save(any(EntidadAnuncio.class))).thenReturn(anuncioBase);
        configurarMocksMapeoExitoso();

        AnuncioResponse res = service.crear(request);

        assertNotNull(res);
        assertEquals("BORRADOR", res.getEstado());
        verify(repo).save(any(EntidadAnuncio.class));
    }

    @Test
    void cambiarEstado_AActivoConCompraVigente_DeberiaActualizar() throws ExcepcionNoExiste {
        when(repo.findById(1)).thenReturn(Optional.of(anuncioBase));
        
        EntidadCompraAnuncio compra = new EntidadCompraAnuncio();
        compra.setEstado("ACTIVO");
        
        when(compraRepo.findByAnuncioId(1)).thenReturn(List.of(compra));

        service.cambiarEstado(1, "ACTIVO");

        assertEquals("ACTIVO", anuncioBase.getEstado());
        verify(repo).save(anuncioBase);
    }

    @Test
    void cambiarEstado_NoExiste_LanzaExcepcion() {
        when(repo.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ExcepcionNoExiste.class, () -> service.cambiarEstado(99, "ACTIVO"));
    }

    @Test
    void obtenerAnunciosParaRevista_ConBloqueoActivo_RetornaListaVacia() {
        when(revistaRepo.findById(1)).thenReturn(Optional.of(new EntidadRevista()));
        
        EntidadBloqueoAnuncio bloqueo = new EntidadBloqueoAnuncio();
        bloqueo.setEstado("ACTIVO");
        
        when(bloqueoAnuncioRepo.findByRevistaIdAndEstado(1, "ACTIVO")).thenReturn(List.of(bloqueo));

        List<AnuncioResponse> res = service.obtenerAnunciosParaRevista(1);

        assertTrue(res.isEmpty());
        verify(repo, never()).buscarAnunciosVigentes(any());
    }

    @Test
    void obtenerAnunciosParaRevista_SinBloqueo_RetornaAnunciosAleatorios() throws ExcepcionNoExiste {
        when(revistaRepo.findById(1)).thenReturn(Optional.of(new EntidadRevista()));
        when(bloqueoAnuncioRepo.findByRevistaIdAndEstado(1, "ACTIVO")).thenReturn(new ArrayList<>());
        when(repo.buscarAnunciosVigentes(any())).thenReturn(List.of(anuncioBase));
        configurarMocksMapeoExitoso();

        List<AnuncioResponse> res = service.obtenerAnunciosParaRevista(1);

        assertFalse(res.isEmpty());
        assertEquals(1, res.size());
    }

    @Test
    void listarTodos_CuandoFallaMapeo_RetornaResponseParcial() throws ExcepcionNoExiste {
        when(repo.findAll()).thenReturn(List.of(anuncioBase));
        // Forzamos error para entrar al catch de mapToResponse
        when(usuarioService.getById(anyInt())).thenThrow(new RuntimeException("Error en servicio"));

        List<AnuncioResponse> res = service.listarTodos();

        assertNotNull(res);
        assertNull(res.get(0).getAnunciante()); 
        assertNull(res.get(0).getTipoAnuncio());
    }

    private void configurarMocksMapeoExitoso() throws ExcepcionNoExiste {
        EntidadUsuario user = new EntidadUsuario();
        user.setId(10);
        when(usuarioService.getById(10)).thenReturn(user);
        when(perfilRepo.findByUsuarioId(10)).thenReturn(Optional.of(new EntidadPerfil()));
        when(tipoAnuncioService.obtenerPorId(5)).thenReturn(new TipoAnuncioResponse());
    }
    @Test
    void actualizar_DeberiaModificarCamposYRetornarResponse() throws ExcepcionNoExiste {
        request.setTexto("Texto Actualizado");
        request.setEstado("ACTIVO");

        when(repo.findById(1)).thenReturn(Optional.of(anuncioBase));
        when(repo.save(any(EntidadAnuncio.class))).thenReturn(anuncioBase);
        configurarMocksMapeoExitoso();

        AnuncioResponse res = service.actualizar(1, request);

        assertNotNull(res);
        assertEquals("Texto Actualizado", anuncioBase.getTexto());
        assertEquals("ACTIVO", anuncioBase.getEstado());
    }

    @Test
    void actualizar_NoExiste_LanzaExcepcion() {
        when(repo.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ExcepcionNoExiste.class, () -> service.actualizar(99, request));
    }
    @Test
    void cambiarEstado_ADesactivado_DeberiaActualizarDirectamente() throws ExcepcionNoExiste {
        when(repo.findById(1)).thenReturn(Optional.of(anuncioBase));

        service.cambiarEstado(1, "DESACTIVADO");

        assertEquals("DESACTIVADO", anuncioBase.getEstado());
        verify(compraRepo, never()).findByAnuncioId(anyInt()); // No debe validar compras
        verify(repo).save(anuncioBase);
    }
    @Test
    void listarPorEstado_DeberiaLlamarMantenimientoYRetornarLista() throws ExcepcionNoExiste {
        when(repo.findByEstadoRandom("ACTIVO")).thenReturn(List.of(anuncioBase));
        configurarMocksMapeoExitoso();

        List<AnuncioResponse> res = service.listarPorEstado("ACTIVO");

        assertFalse(res.isEmpty());
        verify(mantenimiento).desactivarAnunciosExpirados();
    }

    @Test
    void listarPorAnunciante_DeberiaRetornarLista() throws ExcepcionNoExiste {
        when(repo.findByAnuncianteId(10)).thenReturn(List.of(anuncioBase));
        configurarMocksMapeoExitoso();

        List<AnuncioResponse> res = service.listarPorAnunciante(10);

        assertEquals(1, res.size());
        verify(mantenimiento).desactivarAnunciosExpirados();
    }
}
