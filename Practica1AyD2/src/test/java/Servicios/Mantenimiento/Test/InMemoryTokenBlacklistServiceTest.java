/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Mantenimiento.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.seguridad.InMemoryTokenBlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTokenBlacklistServiceTest {

    private InMemoryTokenBlacklistService blacklistService;

    @BeforeEach
    void setUp() {
        blacklistService = new InMemoryTokenBlacklistService();
    }

    @Test
    void blacklist_DeberiaAgregarTokenCorrectamente() {
        String jti = "test-jti-123";
        Date fechaExpiracion = new Date(System.currentTimeMillis() + 10000); // 10 segundos a futuro

        blacklistService.blacklist(jti, fechaExpiracion);

        assertTrue(blacklistService.isBlacklisted(jti), "El token debería estar en la lista negra");
    }

    @Test
    void isBlacklisted_DeberiaRetornarFalseSiNoExiste() {
        assertFalse(blacklistService.isBlacklisted("token-inexistente"));
    }

    @Test
    void isBlacklisted_DeberiaEliminarYRetornarFalseSiYaExpiro() throws InterruptedException {
        String jti = "token-efimero";
        Date fechaExpiracion = new Date(System.currentTimeMillis() + 100);

        blacklistService.blacklist(jti, fechaExpiracion);
        
        Thread.sleep(150);

        assertFalse(blacklistService.isBlacklisted(jti), "El token expirado no debería estar en la lista negra");
    }

    @Test
    void blacklist_DeberiaManejarMultiplesTokensSimultaneos() {
        String jti1 = "token-1";
        String jti2 = "token-2";
        Date exp = new Date(System.currentTimeMillis() + 5000);

        blacklistService.blacklist(jti1, exp);
        blacklistService.blacklist(jti2, exp);

        assertTrue(blacklistService.isBlacklisted(jti1));
        assertTrue(blacklistService.isBlacklisted(jti2));
    }
}
