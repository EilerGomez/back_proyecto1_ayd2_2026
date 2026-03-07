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
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPrecioAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.repositorios.PrecioAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.servicios.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PrecioAnuncioServiceImplTest {

    @Mock private PrecioAnuncioRepositorio repo;
    @Mock private TipoAnuncioService tipoService;
    @Mock private PeriodoAnuncioService periodoService;
    @Mock private UsuarioService usuarioService;
    @Mock private PerfilService perfilService;

    @InjectMocks
    private PrecioAnuncioServiceImpl service;

    private EntidadPrecioAnuncio precioEntidad;
    private PrecioAnuncioRequest request;

    @BeforeEach
    void setUp() {
        precioEntidad = EntidadPrecioAnuncio.builder()
                .id(1)
                .tipoAnuncioId(2)
                .periodoId(3)
                .adminId(10)
                .precio(new BigDecimal("100.00"))
                .activo(true)
                .build();

        request = new PrecioAnuncioRequest();
        request.setTipoAnuncioId(2);
        request.setPeriodoId(3);
        request.setPrecio(new BigDecimal("150.00"));
        request.setAdminId(10);
    }

    private void configurarMocksExitoMapeo() throws ExcepcionNoExiste {
        EntidadUsuario admin = new EntidadUsuario();
        admin.setId(10);
        when(usuarioService.getById(10)).thenReturn(admin);
        when(perfilService.findByUsuarioId(10)).thenReturn(new EntidadPerfil());
    }


    @Test
    void crear_CuandoExisteAnterior_DeberiaDesactivarloYCrearNuevo() throws ExcepcionNoExiste {
        EntidadPrecioAnuncio anterior = new EntidadPrecioAnuncio();
        anterior.setActivo(true);

        when(repo.findByTipoAnuncioIdAndPeriodoIdAndActivoTrue(2, 3)).thenReturn(Optional.of(anterior));
        when(repo.save(any(EntidadPrecioAnuncio.class))).thenReturn(precioEntidad);
        configurarMocksExitoMapeo();

        PrecioAnuncioResponse res = service.crear(request);

        assertNotNull(res);
        assertFalse(anterior.isActivo()); 
        verify(repo, times(2)).save(any(EntidadPrecioAnuncio.class));
    }


    @Test
    void obtenerPorId_SiNoExiste_LanzaExcepcion() {
        when(repo.findById(99)).thenReturn(Optional.empty());
        assertThrows(ExcepcionNoExiste.class, () -> service.obtenerPorId(99));
    }

    @Test
    void obtenerPorTipoAnuncio_DeberiaRetornarLista() throws ExcepcionNoExiste {
        when(repo.findByTipoAnuncioId(2)).thenReturn(List.of(precioEntidad));
        configurarMocksExitoMapeo();

        List<PrecioAnuncioResponse> res = service.obtenerPorTipoAnuncio(2);

        assertEquals(1, res.size());
        verify(repo).findByTipoAnuncioId(2);
    }

    // --- TESTS DE DESACTIVACIÓN ---

    @Test
    void desactivar_Ok_CambiaEstadoAFalso() throws ExcepcionNoExiste {
        when(repo.findById(1)).thenReturn(Optional.of(precioEntidad));
        
        service.desactivar(1);

        assertFalse(precioEntidad.isActivo());
        verify(repo).save(precioEntidad);
    }


    @Test
    void mapToResponse_CuandoFallaServicioInterno_RetornaResponseConNulls() throws ExcepcionNoExiste {
        when(usuarioService.getById(anyInt())).thenThrow(new ExcepcionNoExiste("Error forzado"));
        when(repo.findById(1)).thenReturn(Optional.of(precioEntidad));

        PrecioAnuncioResponse res = service.obtenerPorId(1);

        assertNotNull(res);
        assertNull(res.getTipoAnuncio()); 
        assertNull(res.getAdmin());
    }
}
