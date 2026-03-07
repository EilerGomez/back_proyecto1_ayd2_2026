/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.RecuperacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth/recuperacion")
public class RecuperacionController {

    private final RecuperacionService recuperacionService;

    public RecuperacionController(RecuperacionService recuperacionService) {
        this.recuperacionService = recuperacionService;
    }

    /**
     * Paso 1: Enviar código al correo del usuario.
     * POST /v1/auth/recuperacion/solicitar?identificador=eiler@ejemplo.com
     */
    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitar(@RequestParam String identificador) throws ExcepcionNoExiste {
        recuperacionService.solicitarRecuperacion(identificador);
        return ResponseEntity.ok("Código de recuperación enviado al correo asociado.");
    }

    /**
     * Paso 2: Validar si el código ingresado es correcto y vigente.
     * POST /v1/auth/recuperacion/validar
     */
    @PostMapping("/validar")
    public ResponseEntity<Boolean> validar(@RequestBody Map<String, String> payload) throws ExcepcionNoExiste {
        String correo = payload.get("correo");
        String codigo = payload.get("codigo");
        boolean esValido = recuperacionService.validarCodigo(correo, codigo);
        return ResponseEntity.ok(esValido);
    }

    /**
     * Paso 3: Actualizar la contraseña tras validar el código.
     * PATCH /v1/auth/recuperacion/cambiar
     */
    @PatchMapping("/cambiar")
    public ResponseEntity<String> cambiar(@RequestBody Map<String, String> payload) throws ExcepcionNoExiste {
        String correo = payload.get("correo");
        String codigo = payload.get("codigo");
        String nuevaPassword = payload.get("nuevaPassword");
        
        recuperacionService.cambiarContrasenia(correo, codigo, nuevaPassword);
        return ResponseEntity.ok("Contraseña actualizada exitosamente.");
    }
}
