package Servicios.Usuarios.Tests;

import com.e.gomez.Practica1AyD2.dtoUsuarios.NuevoUsuarioRequest;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioUpdateRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.modelos.Entidad_Usuario_Rol;
import com.e.gomez.Practica1AyD2.repositorios.CarteraRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RolRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRolRepositorio;
import com.e.gomez.Practica1AyD2.servicios.UsuarioServiceImpl;
import com.e.gomez.Practica1AyD2.utilities.GeneratePassword;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock UsuarioRepositorio usuarioRepositorio;
    @Mock RolRepositorio rolRepositorio;
    @Mock PerfilRepositorio perfilRepositorio; // Mock necesario
    @Mock UsuarioRolRepositorio usuarioRolRepositorio;
    @Mock GeneratePassword gen;
    @Mock CarteraRepositorio carteraRepositorio;

    @InjectMocks UsuarioServiceImpl service;

    private static final String nombre = "Juan";
    private static final String username = "username1";
    private static final String apellido = "apellido";
    private static final String correo = "test@mail.com";
    private static final String password = "Passw";
    private static final String estado = "ACTIVO";
    private static final Integer id_rol = 1;

    private EntidadUsuario usuarioBase(Integer id) {
        EntidadUsuario u = new EntidadUsuario();
        u.setId(id);
        u.setNombre("Juan");
        u.setApellido("Perez");
        u.setUsername("juan1");
        u.setCorreo("juan@mail.com");
        u.setEstado("ACTIVO");
        return u;
    }

    private EntidadPerfil perfilBase(Integer usuarioId) {
        EntidadPerfil p = new EntidadPerfil();
        p.setUsuarioId(usuarioId);
        p.setFoto_url("http://test.com/foto.jpg");
        return p;
    }

    @Test
    void crearUsuario_lanzaExcepcion_siCorreoExiste() {
        NuevoUsuarioRequest req = new NuevoUsuarioRequest(nombre, username, apellido, correo, password, estado, id_rol);
        when(usuarioRepositorio.existsByCorreo(correo)).thenReturn(true);

        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.crearUsuario(req));
    }

    @Test
    void crearUsuario_lanzaExcepcion_siUsernameExiste() {
        NuevoUsuarioRequest req = new NuevoUsuarioRequest(nombre, username, apellido, correo, password, estado, id_rol);
        when(usuarioRepositorio.existsByUsername(username)).thenReturn(true);

        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.crearUsuario(req));
    }

    @Test
    void crearUsuario_No_existeRol() {
        NuevoUsuarioRequest req = new NuevoUsuarioRequest(nombre, username, apellido, correo, password, estado, id_rol);
        when(rolRepositorio.existsById(id_rol)).thenReturn(false);

        assertThrows(ExcepcionNoExiste.class, () -> service.crearUsuario(req));
    }

    @Test
    void CrearUsuarioSinErrores() throws ExcepcionEntidadDuplicada, ExcepcionNoExiste {
        NuevoUsuarioRequest req = new NuevoUsuarioRequest(nombre, username, apellido, correo, password, estado, id_rol);

        when(usuarioRepositorio.existsByCorreo(correo)).thenReturn(false);
        when(usuarioRepositorio.existsByUsername(username)).thenReturn(false);
        when(rolRepositorio.existsById(id_rol)).thenReturn(true);

        EntidadUsuario usuarioGuardado = usuarioBase(100);
        usuarioGuardado.setCorreo(correo);
        usuarioGuardado.setUsername(username);

        when(usuarioRepositorio.save(any(EntidadUsuario.class))).thenReturn(usuarioGuardado);

        EntidadUsuario resultado = service.crearUsuario(req);

        assertEquals(100, resultado.getId());
        assertEquals(correo, resultado.getCorreo());
        verify(usuarioRepositorio).save(any(EntidadUsuario.class));
        verify(perfilRepositorio).save(any(EntidadPerfil.class));
        verify(carteraRepositorio).save(any(EntidadCartera.class));
    }

    @Test
    void findAll_devuelveLista() {
        List<EntidadUsuario> lista = List.of(usuarioBase(1), usuarioBase(2));
        when(usuarioRepositorio.findAll()).thenReturn(lista);

        List<EntidadUsuario> res = service.findAll();

        assertEquals(2, res.size());
        verify(usuarioRepositorio, times(1)).findAll();
    }

    @Test
    void eliminarUsuario_eliminaSiExiste() throws ExcepcionNoExiste {
        Integer id = 10;
        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(usuarioBase(id)));

        service.eliminarUsuario(id);

        verify(usuarioRepositorio).deleteById(id);
    }

    @Test
    void eliminarUsuario_lanzaExcepcionSiNoExiste() {
        Integer id = 10;
        when(usuarioRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.eliminarUsuario(id));
        verify(usuarioRepositorio, never()).deleteById(id);
    }

    @Test
    void actualizarUsuario_actualizaCuandoNoHayDuplicados() throws Exception {
        Integer id = 5;
        EntidadUsuario u = usuarioBase(id);
        EntidadPerfil p = perfilBase(id);

        UsuarioUpdateRequest req = new UsuarioUpdateRequest(
                id, "NuevoNombre", "NuevoApellido", "nuevoUser", "nuevo@mail.com", "SUSPENDIDO"
        );

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(u));
        when(usuarioRepositorio.existeUsuarioAActualizarPorUsername(id, req.getUsername())).thenReturn(false);
        when(usuarioRepositorio.existeUsuarioAActualizarPorCorreo(id, req.getCorreo())).thenReturn(false);
        when(usuarioRepositorio.save(any(EntidadUsuario.class))).thenAnswer(inv -> inv.getArgument(0));
        
        when(perfilRepositorio.getByUsuarioId(id)).thenReturn(p);

        UsuarioResponse res = service.actializarUsuario(id, req);

        assertEquals("NuevoNombre", res.getNombre());
        assertEquals("nuevoUser", res.getUsername());
        assertEquals("http://test.com/foto.jpg", res.getPerfilUrl());

        verify(usuarioRepositorio).save(any(EntidadUsuario.class));
    }

    @Test
    void actualizarUsuario_lanzaDuplicadaSiUsernameExiste() {
        Integer id = 5;
        UsuarioUpdateRequest req = new UsuarioUpdateRequest(id, "N", "A", "nuevoUser", "n@m.com", "ACTIVO");

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(usuarioBase(id)));
        when(usuarioRepositorio.existeUsuarioAActualizarPorUsername(id, req.getUsername())).thenReturn(true);

        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.actializarUsuario(id, req));
        verify(usuarioRepositorio, never()).save(any(EntidadUsuario.class));
    }

    @Test
    void actualizarUsuario_lanzaDuplicadaSiCorreoExiste() {
        Integer id = 5;
        UsuarioUpdateRequest req = new UsuarioUpdateRequest(id, "N", "A", "nuevoUser", "n@m.com", "ACTIVO");

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(usuarioBase(id)));
        when(usuarioRepositorio.existeUsuarioAActualizarPorUsername(id, req.getUsername())).thenReturn(false);
        when(usuarioRepositorio.existeUsuarioAActualizarPorCorreo(id, req.getCorreo())).thenReturn(true);

        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.actializarUsuario(id, req));
    }

    @Test
    void actualizarUsuario_lanzaNoExisteSiNoHayUsuario() {
        Integer id = 5;
        UsuarioUpdateRequest req = new UsuarioUpdateRequest(id, "A", "B", "C", "D", "ACTIVO");

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.actializarUsuario(id, req));
    }
}