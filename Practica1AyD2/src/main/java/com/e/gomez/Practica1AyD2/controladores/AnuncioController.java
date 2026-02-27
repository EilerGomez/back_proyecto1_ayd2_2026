/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.AnuncioService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author eiler
 */
@RestController
@RequestMapping("/v1/anuncios")
public class AnuncioController {

    private final AnuncioService anuncioService;

    public AnuncioController(AnuncioService anuncioService) {
        this.anuncioService = anuncioService;
    }

    @PostMapping
    public ResponseEntity<AnuncioResponse> crear(@RequestBody AnuncioRequest request) throws ExcepcionNoExiste {
        return ResponseEntity.status(HttpStatus.CREATED).body(anuncioService.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnuncioResponse> obtenerPorId(@PathVariable Integer id) throws ExcepcionNoExiste {
        return ResponseEntity.ok(anuncioService.obtenerPorId(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<AnuncioResponse>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(anuncioService.listarPorEstado(estado.toUpperCase()));
    }

    @GetMapping("/anunciante/{anuncianteId}")
    public ResponseEntity<List<AnuncioResponse>> listarPorAnunciante(@PathVariable Integer anuncianteId) {
        return ResponseEntity.ok(anuncioService.listarPorAnunciante(anuncianteId));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Integer id, @RequestParam String nuevoEstado) throws ExcepcionNoExiste {
        anuncioService.cambiarEstado(id, nuevoEstado.toUpperCase());
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<AnuncioResponse> actualizar(@PathVariable Integer id, @RequestBody AnuncioRequest request) throws ExcepcionNoExiste {
        return ResponseEntity.ok(anuncioService.actualizar(id, request));
    }
}
