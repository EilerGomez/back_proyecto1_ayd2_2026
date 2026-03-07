/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Autenticacion.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.modelos.PasswordResetToken;
import com.e.gomez.Practica1AyD2.repositorios.TokenRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRepositorio;
import com.e.gomez.Practica1AyD2.servicios.EmailService;
import com.e.gomez.Practica1AyD2.servicios.RecuperacionServiceImpl;
import com.e.gomez.Practica1AyD2.servicios.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecuperacionServiceImplTest {

    @Mock private UsuarioRepositorio usuarioRepo;
    @Mock private TokenRepositorio tokenRepo;
    @Mock private EmailService emailService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private UsuarioService serviceUsuario;

    @InjectMocks
    private RecuperacionServiceImpl service;

    private EntidadUsuario usuario;
    private PasswordResetToken token;

    @BeforeEach
    void setUp() {
        usuario = new EntidadUsuario();
        usuario.setId(1);
        usuario.setCorreo("eiler@test.com");
        usuario.setUsername("eiler");

        token = new PasswordResetToken();
        token.setUsuario(usuario);
        token.setToken("123456");
        token.setFechaExpiracion(LocalDateTime.now().plusMinutes(15));
    }

    // --- solicitarRecuperacion ---

    @Test
    void solicitarRecuperacion_UsuarioExiste_GeneraTokenYEnviaCorreo() throws ExcepcionNoExiste {
        when(usuarioRepo.findByUsernameIgnoreCaseOrCorreoIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.of(usuario));
        when(serviceUsuario.generarCodigoRecuperacion()).thenReturn("123456");

        service.solicitarRecuperacion("eiler");

        verify(tokenRepo).deleteByUsuario(usuario);
        verify(tokenRepo).save(any(PasswordResetToken.class));
        verify(emailService).enviarCorreoCodigo(eq("eiler@test.com"), eq("123456"));
    }

    @Test
    void solicitarRecuperacion_UsuarioNoExiste_LanzaExcepcion() {
        when(usuarioRepo.findByUsernameIgnoreCaseOrCorreoIgnoreCase(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.solicitarRecuperacion("desconocido"));
    }

    // --- validarCodigo ---

    @Test
    void validarCodigo_CodigoValidoYNoExpirado_RetornaTrue() throws ExcepcionNoExiste {
        when(tokenRepo.findByTokenAndUsuarioCorreo("123456", "eiler@test.com"))
                .thenReturn(Optional.of(token));

        boolean valido = service.validarCodigo("eiler@test.com", " 123456 "); // con trim

        assertTrue(valido);
    }

    @Test
    void validarCodigo_TokenNoEncontrado_RetornaFalse() throws ExcepcionNoExiste {
        when(tokenRepo.findByTokenAndUsuarioCorreo(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertFalse(service.validarCodigo("eiler@test.com", "000000"));
    }

    @Test
    void validarCodigo_TokenExpirado_RetornaFalse() throws ExcepcionNoExiste {
        token.setFechaExpiracion(LocalDateTime.now().minusMinutes(1)); // Expiró hace un minuto
        when(tokenRepo.findByTokenAndUsuarioCorreo("123456", "eiler@test.com"))
                .thenReturn(Optional.of(token));

        assertFalse(service.validarCodigo("eiler@test.com", "123456"));
    }

    // --- cambiarContrasenia ---

    @Test
    void cambiarContrasenia_CodigoValido_ActualizaPassword() throws ExcepcionNoExiste {
        // Mock de validarCodigo interno
        when(tokenRepo.findByTokenAndUsuarioCorreo("123456", "eiler@test.com"))
                .thenReturn(Optional.of(token));
        when(usuarioRepo.findByCorreo("eiler@test.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("nueva123")).thenReturn("hashed_pass");

        service.cambiarContrasenia("eiler@test.com", "123456", "nueva123");

        assertEquals("hashed_pass", usuario.getPassword_hash());
        verify(usuarioRepo).save(usuario);
        verify(tokenRepo).deleteByUsuario(usuario);
    }

    @Test
    void cambiarContrasenia_CodigoInvalido_LanzaIllegalArgumentException() {
        when(tokenRepo.findByTokenAndUsuarioCorreo(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, 
            () -> service.cambiarContrasenia("eiler@test.com", "000", "pass"));
    }

    @Test
    void cambiarContrasenia_UsuarioNoEncontradoTrasValidar_LanzaExcepcionNoExiste() throws ExcepcionNoExiste {
        when(tokenRepo.findByTokenAndUsuarioCorreo(anyString(), anyString()))
                .thenReturn(Optional.of(token));
        when(usuarioRepo.findByCorreo(anyString())).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, 
            () -> service.cambiarContrasenia("eiler@test.com", "123456", "pass"));
    }
}