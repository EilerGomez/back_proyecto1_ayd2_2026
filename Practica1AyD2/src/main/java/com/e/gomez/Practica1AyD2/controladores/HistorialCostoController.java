/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoRequest;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.HistorialCostoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/costos-revista")
public class HistorialCostoController {

    private final HistorialCostoService costoService;

    public HistorialCostoController(HistorialCostoService costoService) {
        this.costoService = costoService;
    }

    // Definir un nuevo costo diario para una revista
    @PostMapping
    public ResponseEntity<HistorialCostoResponse> asignarNuevoCosto(@RequestBody HistorialCostoRequest request) throws ExcepcionNoExiste {
        return ResponseEntity.status(HttpStatus.CREATED).body(costoService.asignarNuevoCosto(request));
    }

    // Obtener el costo que está activo actualmente para una revista
    @GetMapping("/vigente/{revistaId}")
    public ResponseEntity<HistorialCostoResponse> obtenerCostoVigente(@PathVariable Integer revistaId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(costoService.obtenerCostoVigente(revistaId));
    }

    // Ver todo el historial de precios que ha tenido una revista
    @GetMapping("/historial/{revistaId}")
    public ResponseEntity<List<HistorialCostoResponse>> obtenerHistorial(@PathVariable Integer revistaId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(costoService.obtenerHistorialPorRevista(revistaId));
    }
}