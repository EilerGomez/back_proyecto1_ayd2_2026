/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */
import com.e.gomez.Practica1AyD2.dtoSuscripciones.dtoRevistasPorSuscripcionByUsuarioResponse;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscricpionResponseByRevistaId;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscripcionRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.SuscripcionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/suscripciones")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    public SuscripcionController(SuscripcionService suscripcionService) {
        this.suscripcionService = suscripcionService;
    }

    // --- CREAR SUSCRIPCIÓN ---
    @PostMapping
    public ResponseEntity<SuscricpionResponseByRevistaId> suscribir(@RequestBody SuscripcionRequest request) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste {
        return ResponseEntity.status(HttpStatus.CREATED).body(suscripcionService.suscribir(request));
    }

    // --- LISTAR POR USUARIO ---
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<dtoRevistasPorSuscripcionByUsuarioResponse>> listarPorUsuario(@PathVariable Integer usuarioId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(suscripcionService.listarPorUsuario(usuarioId));
    }

    // --- LISTAR POR REVISTA ---
    @GetMapping("/revista/{revistaId}")
    public ResponseEntity<List<SuscricpionResponseByRevistaId>> listarPorRevista(@PathVariable Integer revistaId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(suscripcionService.listarPorRevista(revistaId));
    }

    // --- CAMBIAR ESTADO (Activar/Desactivar) ---
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Integer id, @RequestParam boolean activa) throws ExcepcionNoExiste {
        suscripcionService.cambiarEstado(id, activa);
        return ResponseEntity.noContent().build();
    }

    // --- ELIMINAR/CANCELAR ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        suscripcionService.cancelarSuscripcion(id);
        return ResponseEntity.noContent().build();
    }
}