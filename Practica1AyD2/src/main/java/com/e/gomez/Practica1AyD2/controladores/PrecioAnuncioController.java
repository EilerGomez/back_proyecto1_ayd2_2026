/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.PrecioAnuncioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/V1/anuncios/precios")
public class PrecioAnuncioController {

    private final PrecioAnuncioService precioAnuncioService;

    public PrecioAnuncioController(PrecioAnuncioService precioAnuncioService) {
        this.precioAnuncioService = precioAnuncioService;
    }

    // --- CREAR PRECIOS DE ANUNCIOS
    @PostMapping
    public ResponseEntity<PrecioAnuncioResponse> crear(@RequestBody PrecioAnuncioRequest request) throws ExcepcionNoExiste {
        return ResponseEntity.status(HttpStatus.CREATED).body(precioAnuncioService.crear(request));
    }

    // --- LISTAR TODOS CON DETLLES
    @GetMapping
    public ResponseEntity<List<PrecioAnuncioResponse>> listarTodos() {
        return ResponseEntity.ok(precioAnuncioService.listarTodos());
    }

    // --- OBTENER POR ID ---
    @GetMapping("/{id}")
    public ResponseEntity<PrecioAnuncioResponse> obtenerPorId(@PathVariable Integer id) throws ExcepcionNoExiste {
        return ResponseEntity.ok(precioAnuncioService.obtenerPorId(id));
    }

    // --- DESACTIVAR PRECIO ---
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Integer id) throws ExcepcionNoExiste {
        precioAnuncioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
