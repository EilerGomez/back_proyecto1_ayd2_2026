/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionRequest;
import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.EdicionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ediciones")
public class EdicionController {

    private final EdicionService edicionService;

    public EdicionController(EdicionService edicionService) {
        this.edicionService = edicionService;
    }

//creacion
    @PostMapping
    public ResponseEntity<EdicionResponse> crearEdicion(@RequestBody EdicionRequest request) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(edicionService.crearEdicion(request));
    }

    // consultas

    @GetMapping
    public ResponseEntity<List<EdicionResponse>> listarTodas() {
        return ResponseEntity.ok(edicionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EdicionResponse> obtenerPorId(@PathVariable Integer id) throws ExcepcionNoExiste {
        return ResponseEntity.ok(edicionService.getById(id));
    }

    @GetMapping("/revista/{revistaId}")
    public ResponseEntity<List<EdicionResponse>> listarPorRevista(@PathVariable Integer revistaId) {
        return ResponseEntity.ok(edicionService.listarPorRevista(revistaId));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEdicion(@PathVariable Integer id) {
        edicionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}