/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.BloqueoAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.BloqueoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.BloqueoAnuncioService;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/anuncios/bloqueos")
public class BloqueoAnuncioController {

    private final BloqueoAnuncioService service;

    public BloqueoAnuncioController(BloqueoAnuncioService service) {
        this.service = service;
    }

    //  Contratar un nuevo bloqueo (Editor)
    @PostMapping("/contratar")
    public ResponseEntity<BloqueoAnuncioResponse> contratar(@RequestBody BloqueoAnuncioRequest request) throws ExcepcionNoExiste {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.contratarBloqueo(request));
    }

    // Obtener el bloqueo vigente de una revista
    @GetMapping("/activo/revista/{revistaId}")
    public ResponseEntity<BloqueoAnuncioResponse> obtenerActivo(@PathVariable Integer revistaId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(service.obtenerActivoPorRevista(revistaId));
    }

    //  Obtener todo el historial de bloqueos de una revista
    @GetMapping("/historial/revista/{revistaId}")
    public ResponseEntity<List<BloqueoAnuncioResponse>> obtenerHistorial(@PathVariable Integer revistaId) {
        return ResponseEntity.ok(service.listarPorRevista(revistaId));
    }
    
    /**
     * Actualiza la fecha de finalización de un bloqueo de anuncios específico.
     * PATCH /v1/anuncios/bloqueos/{id}/fecha-fin
     */
    @PatchMapping("/{id}/fecha-fin")
    public ResponseEntity<BloqueoAnuncioResponse> actualizarFechaFin(
            @PathVariable Integer id,
            @RequestParam LocalDateTime fechaFin) throws ExcepcionNoExiste {
        
        return ResponseEntity.ok(service.actualizarFechaFin(id, fechaFin));
    }
}
