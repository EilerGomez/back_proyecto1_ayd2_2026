/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Usuarios.Tests;

import com.e.gomez.Practica1AyD2.dtoUsuarios.NuevoUsuarioRequest;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioUpdateRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import java.util.List;
import java.util.Optional;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.modelos.Entidad_Usuario_Rol;
import com.e.gomez.Practica1AyD2.repositorios.CarteraRepositorio;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RolRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRolRepositorio;
import com.e.gomez.Practica1AyD2.servicios.UsuarioService;
import com.e.gomez.Practica1AyD2.servicios.UsuarioServiceImpl;
import com.e.gomez.Practica1AyD2.utilities.GeneratePassword;
import static net.bytebuddy.matcher.ElementMatchers.any;
import static org.mockito.ArgumentMatchers.any;


/**
 *
 * @author eiler
 */
@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {
    @Mock UsuarioRepositorio usuarioRepositorio;
    @Mock RolRepositorio rolRepositorio;
    @Mock PerfilRepositorio perfilRepositorio;
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
    private static final Integer id = 1;
    
    private final EntidadUsuario usuario = new EntidadUsuario(id, username, nombre, apellido, correo, password, estado);

    
    @Test
    void crearUsuario_lanzaExcepcion_siCorreoExiste() {
        
        NuevoUsuarioRequest req = new NuevoUsuarioRequest(nombre, username, apellido,correo,password, estado,id_rol);
        
        when(usuarioRepositorio.existsByCorreo(correo)).thenReturn(true);

        Assertions.assertThrows(ExcepcionEntidadDuplicada.class, 
                ()-> service.crearUsuario(req));

    }
    
    @Test
    void crearUsuario_lanzaExcepcion_siUsernameExiste(){
        NuevoUsuarioRequest req = new NuevoUsuarioRequest(nombre, username, apellido,correo,password, estado,id_rol);
        
        when(usuarioRepositorio.existsByUsername(username)).thenReturn(true);
        
        Assertions.assertThrows(ExcepcionEntidadDuplicada.class, 
                ()-> service.crearUsuario(req));
    }
    
    @Test
    void crearUsuario_No_existeRol(){
        NuevoUsuarioRequest req = new NuevoUsuarioRequest(nombre, username, apellido,correo,password, estado,id_rol);
        
        when(rolRepositorio.existsById(id_rol)).thenReturn(false);
        
        Assertions.assertThrows(ExcepcionNoExiste.class, 
                ()-> service.crearUsuario(req));
    }
    
    @Test
    void CrearUsuarioSinErrores() throws ExcepcionEntidadDuplicada, ExcepcionNoExiste{
        //Arrage
        NuevoUsuarioRequest req = new NuevoUsuarioRequest(nombre, username, apellido,correo,password, estado,id_rol);
        
        when(usuarioRepositorio.existsByCorreo(correo)).thenReturn(false);
        when(usuarioRepositorio.existsByUsername(username)).thenReturn(false);
        when(rolRepositorio.existsById(id_rol)).thenReturn(true);

        
        EntidadUsuario usuarioGuardado = new EntidadUsuario();
        usuarioGuardado.setId(100);
        usuarioGuardado.setUsername(username);
        usuarioGuardado.setNombre(nombre);
        usuarioGuardado.setApellido(apellido);
        usuarioGuardado.setCorreo(correo);
        usuarioGuardado.setEstado("ACTIVO");
        usuarioGuardado.setPassword_hash(gen.hashPassword(password));
       
        
        when(usuarioRepositorio.save(Mockito.any(EntidadUsuario.class)))
                .thenReturn(usuarioGuardado);
        
        //ACT
        
        EntidadUsuario resultado = service.crearUsuario(req);
        
        //Assert
        
        assertEquals(100, resultado.getId());
        assertEquals(correo, resultado.getCorreo());
        assertEquals("ACTIVO", resultado.getEstado());
        assertEquals(username,resultado.getUsername());
        
        //verificacion de crear usuario
        verify(usuarioRepositorio, Mockito.times(1))
                .save(Mockito.any(EntidadUsuario.class));
        
        //verificacion ceracion de perfil
        verify(perfilRepositorio, Mockito.times(1))
            .save(Mockito.argThat(p -> p.getUsuarioId().equals(100)));
        
        
        //verificacion creacion del usuario roles
        verify(usuarioRolRepositorio, Mockito.times(1))
            .save(Mockito.any(Entidad_Usuario_Rol.class));
        
        //verificar la creacion de la cartera
        verify(carteraRepositorio, Mockito.times(1))
                .save(Mockito.argThat(c -> c.getUsuarioId().equals(100)));
        
    }
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
    
    @Test
    void findAll_devuelveLista() {
        //Arrage
        List<EntidadUsuario> lista = List.of(usuarioBase(1), usuarioBase(2));
        when(usuarioRepositorio.findAll()).thenReturn(lista);

        //ACT
        List<EntidadUsuario> res = service.findAll();

        //Assert
        assertEquals(2, res.size());
        verify(usuarioRepositorio, times(1)).findAll();
        verifyNoMoreInteractions(usuarioRepositorio);
    }
    
    @Test
    void eliminarUsuario_eliminaSiExiste() throws ExcepcionNoExiste {
        Integer id = 10;
        EntidadUsuario u = usuarioBase(id);

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(u));

        service.eliminarUsuario(id);

        verify(usuarioRepositorio, times(1)).findById(id);
        verify(usuarioRepositorio, times(1)).deleteById(id);
    }
    
    @Test
    void eliminarUsuario_lanzaExcepcionSiNoExiste() {
        Integer id = 10;
        when(usuarioRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.eliminarUsuario(id));

        verify(usuarioRepositorio, times(1)).findById(id);
        verify(usuarioRepositorio, never()).deleteById(id);
    }
    
    @Test
    void actualizarUsuario_actualizaCuandoNoHayDuplicados() throws Exception {
        Integer id = 5;
        EntidadUsuario u = usuarioBase(id);

        // Request con datos nuevos
        UsuarioUpdateRequest req = new UsuarioUpdateRequest(
                id,
                "NuevoNombre",
                "NuevoApellido",
                "nuevoUser",
                "nuevo@mail.com",
                "SUSPENDIDO"
        );

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(u));

        when(usuarioRepositorio.existeUsuarioAActualizarPorUsername(id, req.getUsername())).thenReturn(false);
        when(usuarioRepositorio.existeUsuarioAActualizarPorCorreo(id, req.getCorreo())).thenReturn(false);

        when(usuarioRepositorio.save(any(EntidadUsuario.class))).thenAnswer(inv -> inv.getArgument(0));

        UsuarioResponse res = service.actializarUsuario(id, req);

        // Verifica que seteo nuevos datos
        assertEquals("NuevoNombre", res.getNombre());
        assertEquals("NuevoApellido", res.getApellido());
        assertEquals("nuevoUser", res.getUsername());
        assertEquals("nuevo@mail.com", res.getCorreo());
        assertEquals("SUSPENDIDO", res.getEstado());

        verify(usuarioRepositorio).save(argThat(us ->
                us.getId().equals(id) &&
                us.getNombre().equals("NuevoNombre") &&
                us.getApellido().equals("NuevoApellido") &&
                us.getUsername().equals("nuevoUser") &&
                us.getCorreo().equals("nuevo@mail.com") &&
                us.getEstado().equals("SUSPENDIDO")
        ));
    }

    @Test
    void actualizarUsuario_lanzaDuplicadaSiUsernameExiste() throws Exception {
        Integer id = 5;
        EntidadUsuario u = usuarioBase(id);

        UsuarioUpdateRequest req = new UsuarioUpdateRequest(
                id,"NuevoNombre","NuevoApellido","nuevoUser","nuevo@mail.com","ACTIVO"
        );

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(u));

        when(usuarioRepositorio.existeUsuarioAActualizarPorUsername(id, req.getUsername())).thenReturn(true);

        ExcepcionEntidadDuplicada ex = assertThrows(
                ExcepcionEntidadDuplicada.class,
                () -> service.actializarUsuario(id, req)
        );

        assertEquals("EL Username ya existe", ex.getMessage());
        verify(usuarioRepositorio, never()).save(Mockito.any(EntidadUsuario.class));

    }

    @Test
    void actualizarUsuario_lanzaDuplicadaSiCorreoExiste() throws Exception {
        Integer id = 5;
        EntidadUsuario u = usuarioBase(id);

        UsuarioUpdateRequest req = new UsuarioUpdateRequest(
                id,"NuevoNombre","NuevoApellido","nuevoUser","nuevo@mail.com","ACTIVO"
        );

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.of(u));

        when(usuarioRepositorio.existeUsuarioAActualizarPorUsername(id, req.getUsername())).thenReturn(false);
        when(usuarioRepositorio.existeUsuarioAActualizarPorCorreo(id, req.getCorreo())).thenReturn(true);

        ExcepcionEntidadDuplicada ex = assertThrows(
                ExcepcionEntidadDuplicada.class,
                () -> service.actializarUsuario(id, req)
        );

        assertEquals("EL Correo ya existe", ex.getMessage());
        verify(usuarioRepositorio, never()).save(Mockito.any(EntidadUsuario.class));
    }

    @Test
    void actualizarUsuario_lanzaNoExisteSiNoHayUsuario() {
        Integer id = 5;
        UsuarioUpdateRequest req = new UsuarioUpdateRequest(
               id, "A","B","C","D","ACTIVO"
        );

        when(usuarioRepositorio.findById(id)).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.actializarUsuario(id, req));

        verify(usuarioRepositorio, never()).save(Mockito.any(EntidadUsuario.class));
    }    

}
