/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.TipoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.TipoAnuncioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/anuncios/tipos")
public class TipoAnuncioController {

    private final TipoAnuncioService tipoAnuncioService;

    public TipoAnuncioController(TipoAnuncioService tipoAnuncioService) {
        this.tipoAnuncioService = tipoAnuncioService;
    }

    @GetMapping
    public ResponseEntity<List<TipoAnuncioResponse>> listarTodos() {
        return ResponseEntity.ok(tipoAnuncioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoAnuncioResponse> obtenerPorId(@PathVariable Integer id) throws ExcepcionNoExiste {
        return ResponseEntity.ok(tipoAnuncioService.obtenerPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<TipoAnuncioResponse> obtenerPorCodigo(@PathVariable String codigo) throws ExcepcionNoExiste {
        return ResponseEntity.ok(tipoAnuncioService.obtenerPorCodigo(codigo));
    }
}
