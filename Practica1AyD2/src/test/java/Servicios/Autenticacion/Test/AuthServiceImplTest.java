/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Autenticacion.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAuth.LoginRequest;
import com.e.gomez.Practica1AyD2.dtoAuth.LoginResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionCredencialesInvalidas;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.*;
import com.e.gomez.Practica1AyD2.repositorios.*;
import com.e.gomez.Practica1AyD2.seguridad.JwtService;
import com.e.gomez.Practica1AyD2.seguridad.TokenBlacklistService;
import com.e.gomez.Practica1AyD2.servicios.AuthServiceImpl;
import io.jsonwebtoken.JwtException;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock private UsuarioRepositorio usuarioRepositorio;
    @Mock private UsuarioRolRepositorio usuarioRolRepositorio;
    @Mock private RolRepositorio rolRepositorio;
    @Mock private BCryptPasswordEncoder encoder;
    @Mock private JwtService jwtService;
    @Mock private TokenBlacklistService blacklist;
    @Mock private PerfilRepositorio perfilRepositorio;
    @Mock private CarteraRepositorio carteraRepositorio;
    @InjectMocks
    private AuthServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // login()
    

    @Test
    void login_identificadorVacio_lanzaCredencialesInvalidas() {
        LoginRequest req = new LoginRequest();
        req.setIdentificador("   ");
        req.setPassword("123");

        assertThrows(ExcepcionCredencialesInvalidas.class, () -> service.login(req));

        verifyNoInteractions(usuarioRepositorio, encoder, usuarioRolRepositorio, rolRepositorio, jwtService, perfilRepositorio);
    }

    @Test
    void login_passwordVacio_lanzaCredencialesInvalidas() {
        LoginRequest req = new LoginRequest();
        req.setIdentificador("eiler123");
        req.setPassword("   ");

        assertThrows(ExcepcionCredencialesInvalidas.class, () -> service.login(req));

        verifyNoInteractions(usuarioRepositorio, encoder, usuarioRolRepositorio, rolRepositorio, jwtService, perfilRepositorio);
    }

    @Test
    void login_usuarioNoExiste_lanzaNoExiste() {
        LoginRequest req = new LoginRequest();
        req.setIdentificador("eiler123");
        req.setPassword("123456");

        when(usuarioRepositorio.findByUsernameIgnoreCaseOrCorreoIgnoreCase("eiler123", "eiler123"))
                .thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.login(req));

        verify(usuarioRepositorio).findByUsernameIgnoreCaseOrCorreoIgnoreCase("eiler123", "eiler123");
        verifyNoMoreInteractions(usuarioRepositorio);
        verifyNoInteractions(encoder, usuarioRolRepositorio, rolRepositorio, jwtService, perfilRepositorio);
    }

    @Test
    void login_passwordIncorrecto_lanzaCredenciales_Invalidas() {
        LoginRequest req = new LoginRequest();
        req.setIdentificador("eiler123");
        req.setPassword("mala");

        EntidadUsuario user = new EntidadUsuario();
        user.setId(1);
        user.setUsername("eiler123");
        user.setPassword_hash("$2a$10$HASH");

        when(usuarioRepositorio.findByUsernameIgnoreCaseOrCorreoIgnoreCase("eiler123", "eiler123"))
                .thenReturn(Optional.of(user));
        when(encoder.matches("mala", "$2a$10$HASH"))
                .thenReturn(false);

        assertThrows(ExcepcionCredencialesInvalidas.class, () -> service.login(req));

        verify(usuarioRepositorio).findByUsernameIgnoreCaseOrCorreoIgnoreCase("eiler123", "eiler123");
        verify(encoder).matches("mala", "$2a$10$HASH");
        verifyNoInteractions(usuarioRolRepositorio, rolRepositorio, jwtService, perfilRepositorio);
    }

    @Test
    void login_usuarioSinRol_lanzaNoExiste() {
        LoginRequest req = new LoginRequest();
        req.setIdentificador("eiler123");
        req.setPassword("123456");

        EntidadUsuario user = new EntidadUsuario();
        user.setId(1);
        user.setUsername("eiler123");
        user.setPassword_hash("$2a$10$HASH");

        when(usuarioRepositorio.findByUsernameIgnoreCaseOrCorreoIgnoreCase("eiler123", "eiler123"))
                .thenReturn(Optional.of(user));
        when(encoder.matches("123456", "$2a$10$HASH"))
                .thenReturn(true);

        when(usuarioRolRepositorio.findByIdUsuarioId(1)).thenReturn(null);

        assertThrows(ExcepcionNoExiste.class, () -> service.login(req));

        verify(usuarioRolRepositorio).findByIdUsuarioId(1);
        verifyNoInteractions(rolRepositorio, jwtService, perfilRepositorio);
    }

    @Test
    void login_rolNoExiste_lanzaNoExiste() {
        LoginRequest req = new LoginRequest();
        req.setIdentificador("eiler123");
        req.setPassword("123456");

        EntidadUsuario user = new EntidadUsuario();
        user.setId(1);
        user.setUsername("eiler123");
        user.setPassword_hash("$2a$10$HASH");

        when(usuarioRepositorio.findByUsernameIgnoreCaseOrCorreoIgnoreCase("eiler123", "eiler123"))
                .thenReturn(Optional.of(user));
        when(encoder.matches("123456", "$2a$10$HASH"))
                .thenReturn(true);

        Entidad_Usuario_Rol ur = new Entidad_Usuario_Rol();
        UsuarioRolId id = new UsuarioRolId();
        id.setUsuarioId(1);
        id.setRolId(99);
        ur.setId(id);

        when(usuarioRolRepositorio.findByIdUsuarioId(1)).thenReturn(ur);
        when(rolRepositorio.findById(99)).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.login(req));

        verify(rolRepositorio).findById(99);
        verifyNoInteractions(jwtService, perfilRepositorio);
    }

    @Test
    void login_ok_retornaTokenYUsuarioCompleto() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setIdentificador("eiler123");
        req.setPassword("123456");

        EntidadUsuario user = new EntidadUsuario();
        user.setId(1);
        user.setUsername("eiler123");
        user.setPassword_hash("$2a$10$HASH");

        Entidad_Usuario_Rol ur = new Entidad_Usuario_Rol();
        UsuarioRolId id = new UsuarioRolId();
        id.setUsuarioId(1);
        id.setRolId(2);
        ur.setId(id);

        EntidadRol rol = new EntidadRol();
        rol.setId(2);
        rol.setNombre("ADMIN");

        EntidadPerfil perfil = new EntidadPerfil();
        perfil.setUsuarioId(1);
        
        EntidadCartera cartera = new EntidadCartera();
        cartera.setUsuarioId(1);

        when(usuarioRepositorio.findByUsernameIgnoreCaseOrCorreoIgnoreCase("eiler123", "eiler123"))
                .thenReturn(Optional.of(user));
        when(encoder.matches("123456", "$2a$10$HASH"))
                .thenReturn(true);

        when(usuarioRolRepositorio.findByIdUsuarioId(1)).thenReturn(ur);
        when(rolRepositorio.findById(2)).thenReturn(Optional.of(rol));

        when(jwtService.generateToken(1, "eiler123", "ADMIN")).thenReturn("TOKEN123");
        when(jwtService.getExpirationMs()).thenReturn(3600000L);

        when(perfilRepositorio.findByUsuarioId(1)).thenReturn(Optional.of(perfil));
        
        when(carteraRepositorio.findByUsuarioId(1)).thenReturn(Optional.of(cartera));
        
        LoginResponse resp = service.login(req);

        assertNotNull(resp);
        assertEquals("TOKEN123", resp.getToken());
        assertEquals("Bearer", resp.getTokenType());
        assertEquals(3600000L, resp.getExpiresInMs());

        verify(jwtService).generateToken(1, "eiler123", "ADMIN");
        verify(perfilRepositorio).findByUsuarioId(1);
    }

    // logout


    @Test
    void logout_ok_blacklisteaToken() throws Exception {
        String token = "TOKEN123";

        when(jwtService.getJti(token)).thenReturn("JTI-1");
        when(jwtService.getExpirationEpochMs(token)).thenReturn(1700000000000L);

        service.logout(token);

        ArgumentCaptor<Date> dateCaptor = ArgumentCaptor.forClass(Date.class);

        verify(blacklist).blacklist(eq("JTI-1"), dateCaptor.capture());
        assertEquals(1700000000000L, dateCaptor.getValue().getTime());
    }

    @Test
    void logout_tokenInvalido_lanzaCredencialesInvalidas() {
        String token = "MALO";

        when(jwtService.getJti(token)).thenThrow(new JwtException("bad"));

        assertThrows(ExcepcionCredencialesInvalidas.class, () -> service.logout(token));

        verify(jwtService).getJti(token);
        verifyNoInteractions(blacklist);
    }
}