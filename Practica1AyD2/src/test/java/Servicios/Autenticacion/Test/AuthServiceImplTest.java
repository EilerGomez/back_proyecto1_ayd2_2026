package Servicios.Autenticacion.Test;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Date;
import java.util.Optional;

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

    private LoginRequest req;
    private EntidadUsuario userActivo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        req = new LoginRequest();
        req.setIdentificador("eiler");
        req.setPassword("123456");

        userActivo = new EntidadUsuario();
        userActivo.setId(1);
        userActivo.setUsername("eiler");
        userActivo.setPassword_hash("$2a$10$hashed");
        userActivo.setEstado("ACTIVO");
    }

    @Test
    void login_identificadorNuloOBlanco_lanzaExcepcion() {
        req.setIdentificador("");
        assertThrows(ExcepcionCredencialesInvalidas.class, () -> service.login(req));
    }

    @Test
    void login_passwordNuloOBlanco_lanzaExcepcion() {
        req.setPassword(" ");
        assertThrows(ExcepcionCredencialesInvalidas.class, () -> service.login(req));
    }


    @Test
    void login_usuarioInactivo_lanzaExcepcion() {
        userActivo.setEstado("INACTIVO");
        when(usuarioRepositorio.findByUsernameIgnoreCaseOrCorreoIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(userActivo));

        ExcepcionCredencialesInvalidas ex = assertThrows(ExcepcionCredencialesInvalidas.class, () -> service.login(req));
        assertEquals("El usuario no esta activo para el sistema", ex.getMessage());
    }

    @Test
    void login_passwordIncorrecto_lanzaExcepcion() {
        when(usuarioRepositorio.findByUsernameIgnoreCaseOrCorreoIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(userActivo));
        when(encoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(ExcepcionCredencialesInvalidas.class, () -> service.login(req));
    }

    @Test
    void login_usuarioSinRol_lanzaExcepcion() {
        when(usuarioRepositorio.findByUsernameIgnoreCaseOrCorreoIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(userActivo));
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        when(usuarioRolRepositorio.findByIdUsuarioId(1)).thenReturn(null);

        assertThrows(ExcepcionNoExiste.class, () -> service.login(req));
    }

    // --- TESTS DE INTEGRIDAD
    @Test
    void login_perfilNoEncontrado_lanzaExcepcion() throws ExcepcionNoExiste {
        configurarMocksHastaRol();
        when(perfilRepositorio.findByUsuarioId(1)).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.login(req));
    }
    
    @Test
    void login_rolNoEncontrado_lanzaNoExiste() {
        when(usuarioRepositorio.findByUsernameIgnoreCaseOrCorreoIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(userActivo));
        when(encoder.matches(anyString(), anyString())).thenReturn(true);

        Entidad_Usuario_Rol ur = new Entidad_Usuario_Rol();
        UsuarioRolId urId = new UsuarioRolId();
        urId.setRolId(2);
        ur.setId(urId);
        when(usuarioRolRepositorio.findByIdUsuarioId(1)).thenReturn(ur);

        when(rolRepositorio.findById(2)).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.login(req));

        verify(rolRepositorio).findById(2);
        verifyNoInteractions(jwtService, perfilRepositorio);
    }

    @Test
    void login_carteraNoEncontrada_lanzaExcepcion() throws ExcepcionNoExiste {
        configurarMocksHastaRol();
        when(perfilRepositorio.findByUsuarioId(1)).thenReturn(Optional.of(new EntidadPerfil()));
        when(carteraRepositorio.findByUsuarioId(1)).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.login(req));
    }

    
    @Test
    void login_ok_retornaLoginResponse() throws Exception {
        configurarMocksHastaRol();
        when(perfilRepositorio.findByUsuarioId(1)).thenReturn(Optional.of(new EntidadPerfil()));
        when(carteraRepositorio.findByUsuarioId(1)).thenReturn(Optional.of(new EntidadCartera()));
        when(jwtService.generateToken(anyInt(), anyString(), anyString())).thenReturn("token-abc");
        when(jwtService.getExpirationMs()).thenReturn(1000L);

        LoginResponse res = service.login(req);

        assertNotNull(res);
        assertEquals("token-abc", res.getToken());
        assertEquals("Bearer", res.getTokenType());
    }

    @Test
    void logout_ok_procesaToken() {
        String token = "valid-token";
        when(jwtService.getJti(token)).thenReturn("jti-123");
        when(jwtService.getExpirationEpochMs(token)).thenReturn(System.currentTimeMillis());

        assertDoesNotThrow(() -> service.logout(token));
        verify(blacklist).blacklist(eq("jti-123"), any(Date.class));
    }

    @Test
    void logout_tokenInvalido_lanzaExcepcion() {
        when(jwtService.getJti(anyString())).thenThrow(new JwtException("Error"));
        assertThrows(ExcepcionCredencialesInvalidas.class, () -> service.logout("malo"));
    }

    private void configurarMocksHastaRol() {//me sirve para no repetir codigo
        when(usuarioRepositorio.findByUsernameIgnoreCaseOrCorreoIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(userActivo));
        when(encoder.matches(anyString(), anyString())).thenReturn(true);
        
        Entidad_Usuario_Rol ur = new Entidad_Usuario_Rol();
        UsuarioRolId urId = new UsuarioRolId();
        urId.setRolId(2);
        ur.setId(urId);
        
        when(usuarioRolRepositorio.findByIdUsuarioId(1)).thenReturn(ur);
        when(rolRepositorio.findById(2)).thenReturn(Optional.of(new EntidadRol(2, "ADMIN")));
    }
}