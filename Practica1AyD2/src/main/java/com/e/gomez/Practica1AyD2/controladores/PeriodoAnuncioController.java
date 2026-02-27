/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.PeriodoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.PeriodoAnuncioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/anuncios/periodos")
public class PeriodoAnuncioController {

    private final PeriodoAnuncioService periodoAnuncioService;

    public PeriodoAnuncioController(PeriodoAnuncioService periodoAnuncioService) {
        this.periodoAnuncioService = periodoAnuncioService;
    }

    @GetMapping
    public ResponseEntity<List<PeriodoAnuncioResponse>> listarTodos() {
        return ResponseEntity.ok(periodoAnuncioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodoAnuncioResponse> obtenerPorId(@PathVariable Integer id) throws ExcepcionNoExiste {
        return ResponseEntity.ok(periodoAnuncioService.obtenerPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<PeriodoAnuncioResponse> obtenerPorCodigo(@PathVariable String codigo) throws ExcepcionNoExiste {
        return ResponseEntity.ok(periodoAnuncioService.obtenerPorCodigo(codigo));
    }
}
