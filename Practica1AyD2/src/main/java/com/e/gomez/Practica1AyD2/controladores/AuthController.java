/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAuth.LoginRequest;
import com.e.gomez.Practica1AyD2.dtoAuth.LoginResponse;
import com.e.gomez.Practica1AyD2.dtoAuth.LogoutResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionCredencialesInvalidas;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req)
            throws ExcepcionNoExiste, ExcepcionCredencialesInvalidas {
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("Authorization") String authHeader)
            throws ExcepcionCredencialesInvalidas {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ExcepcionCredencialesInvalidas("Falta Bearer token");
        }
        String token = authHeader.substring(7);
        authService.logout(token);

        return ResponseEntity.ok(new LogoutResponse("Logout exitoso"));
    }

    @ExceptionHandler(ExcepcionCredencialesInvalidas.class)
    public ResponseEntity<String> handleCred(ExcepcionCredencialesInvalidas ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(ExcepcionNoExiste.class)
    public ResponseEntity<String> handleNoExiste(ExcepcionNoExiste ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
