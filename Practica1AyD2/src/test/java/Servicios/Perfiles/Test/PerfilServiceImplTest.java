/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Perfiles.Test;

import com.e.gomez.Practica1AyD2.dtoUsuarios.NuevoPerfilRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import com.e.gomez.Practica1AyD2.servicios.PerfilServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfilServiceImplTest {

    @Mock
    private PerfilRepositorio repository;

    @InjectMocks
    private PerfilServiceImpl service;


    // crearPerfil

    @Test
    void crearPerfil_ok_creaYGuardaPerfil() throws Exception {
        Integer usuarioId = 10;

        when(repository.existsByUsuarioId(usuarioId)).thenReturn(false);

        when(repository.save(any(EntidadPerfil.class))).thenAnswer(inv -> inv.getArgument(0));

        EntidadPerfil resp = service.crearPerfil(usuarioId);

        assertNotNull(resp);
        assertEquals(usuarioId, resp.getUsuarioId());

        verify(repository).existsByUsuarioId(usuarioId);
        verify(repository).save(any(EntidadPerfil.class));
    }

    @Test
    void crearPerfil_usuarioYaTienePerfil_lanzaDuplicada() {
        Integer usuarioId = 10;

        when(repository.existsByUsuarioId(usuarioId)).thenReturn(true);

        ExcepcionEntidadDuplicada ex = assertThrows(
                ExcepcionEntidadDuplicada.class,
                () -> service.crearPerfil(usuarioId)
        );

        assertTrue(ex.getMessage().contains("ya tiene perfil"));

        verify(repository).existsByUsuarioId(usuarioId);
        verify(repository, never()).save(any());
    }

    // findByUsuarioId

    @Test
    void findByUsuarioId_ok_retornaPerfil() throws Exception {
        Integer usuarioId = 10;
        EntidadPerfil perfil = new EntidadPerfil();
        perfil.setUsuarioId(usuarioId);

        when(repository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(perfil));

        EntidadPerfil resp = service.findByUsuarioId(usuarioId);

        assertNotNull(resp);
        assertEquals(usuarioId, resp.getUsuarioId());

        verify(repository).findByUsuarioId(usuarioId);
    }

    @Test
    void findByUsuarioId_noExiste_lanzaExcepcion() throws ExcepcionNoExiste {
        Integer usuarioId = 99;

        when(repository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());

        ExcepcionNoExiste ex = assertThrows(
                ExcepcionNoExiste.class,
                () -> service.findByUsuarioId(usuarioId)
        );

        assertTrue(ex.getMessage().contains("Perfil no encontrado"));

        verify(repository).findByUsuarioId(usuarioId);
    }

    // updatePerfil (PATCH)

    @Test
    void updatePerfil_ok_actualizaSoloCamposNoNulos() throws Exception {
        Integer usuarioId = 10;

        // Perfil actual (en BD)
        EntidadPerfil perfilActual = new EntidadPerfil();
        perfilActual.setUsuarioId(usuarioId);
        perfilActual.setFoto_url("fotoVieja");
        perfilActual.setHobbies("hobbiesViejos");
        perfilActual.setIntereses("interesesViejos");
        perfilActual.setDescripcion("descVieja");
        perfilActual.setGustos("gustosViejos");

        when(repository.findByUsuarioId(usuarioId)).thenReturn(Optional.of(perfilActual));
        when(repository.save(any(EntidadPerfil.class))).thenAnswer(inv -> inv.getArgument(0));

        // Request PATCH: solo cambian 2 campos, lo demás null
        NuevoPerfilRequest req = new NuevoPerfilRequest();
        req.setFoto_url("fotoNueva");
        req.setDescripcion("descNueva");
        // hobbies/intereses/gustos quedan null

        EntidadPerfil resp = service.updatePerfil(usuarioId, req);

        assertNotNull(resp);
        assertEquals("fotoNueva", resp.getFoto_url());
        assertEquals("descNueva", resp.getDescripcion());

        // Se deben mantener los viejos porque venían null
        assertEquals("hobbiesViejos", resp.getHobbies());
        assertEquals("interesesViejos", resp.getIntereses());
        assertEquals("gustosViejos", resp.getGustos());

        verify(repository).findByUsuarioId(usuarioId);

        // Validar lo que se mando a almacenar
        ArgumentCaptor<EntidadPerfil> captor = ArgumentCaptor.forClass(EntidadPerfil.class);
        verify(repository).save(captor.capture());

        EntidadPerfil guardado = captor.getValue();
        assertEquals("fotoNueva", guardado.getFoto_url());
        assertEquals("descNueva", guardado.getDescripcion());
        assertEquals("hobbiesViejos", guardado.getHobbies());
    }

    @Test
    void updatePerfil_perfilNoExiste_lanzaExcepcion() throws ExcepcionNoExiste {
        Integer usuarioId = 999;

        when(repository.findByUsuarioId(usuarioId)).thenReturn(Optional.empty());

        NuevoPerfilRequest req = new NuevoPerfilRequest();
        req.setDescripcion("algo");

        ExcepcionNoExiste ex = assertThrows(
                ExcepcionNoExiste.class,
                () -> service.updatePerfil(usuarioId, req)
        );

        assertTrue(ex.getMessage().contains("Perfil no encontrado"));

        verify(repository).findByUsuarioId(usuarioId);
        verify(repository, never()).save(any());
    }
}
