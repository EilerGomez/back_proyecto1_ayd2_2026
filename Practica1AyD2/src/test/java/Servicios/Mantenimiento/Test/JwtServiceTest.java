/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Mantenimiento.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.seguridad.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    
    private final String SECRET_BASE64 = Base64.getEncoder().encodeToString(
            "esta-es-una-clave-secreta-muy-larga-y-segura-de-256-bits".getBytes()
    );
    private final long EXPIRATION_MS = 3600000; 

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        
        ReflectionTestUtils.setField(jwtService, "secretBase64", SECRET_BASE64);
        ReflectionTestUtils.setField(jwtService, "expirationMs", EXPIRATION_MS);
        
        ReflectionTestUtils.invokeMethod(jwtService, "init");
    }

    @Test
    void generateToken_DeberiaCrearTokenValido() {
        String username = "eiler_test";
        Integer userId = 123;
        String rol = "ADMIN";

        String token = jwtService.generateToken(userId, username, rol);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals(username, jwtService.parseClaims(token).getSubject());
        assertEquals(rol, jwtService.getRol(token));
        assertEquals(userId, jwtService.getUserId(token));
    }

    @Test
    void getJti_DeberiaRetornarUUIDValido() {
        String token = jwtService.generateToken(1, "user", "USER");
        String jti = jwtService.getJti(token);

        assertNotNull(jti);
        assertDoesNotThrow(() -> java.util.UUID.fromString(jti));
    }

    @Test
    void getUserId_DeberiaManejarConversionesDeTipo() {
        String token = jwtService.generateToken(999, "user", "USER");
        
        Integer userId = jwtService.getUserId(token);
        
        assertEquals(999, userId);
    }

    @Test
    void getExpirationEpochMs_DeberiaRetornarTiempoEnFuturo() {
        String token = jwtService.generateToken(1, "user", "USER");
        long expiration = jwtService.getExpirationEpochMs(token);
        
        assertTrue(expiration > System.currentTimeMillis());
    }

    @Test
    void parseClaims_DeberiaLanzarExcepcionConTokenInvalido() {
        String tokenInvalido = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.invalid.payload";
        
        assertThrows(Exception.class, () -> jwtService.parseClaims(tokenInvalido));
    }
    
    @Test
    void getExpirationMs_DeberiaRetornarConfiguracion() {
        assertEquals(EXPIRATION_MS, jwtService.getExpirationMs());
    }
}
