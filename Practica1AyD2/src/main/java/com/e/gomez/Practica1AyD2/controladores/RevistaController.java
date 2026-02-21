/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.RevistaResponse;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.RevistaEtiquetasRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.RevistaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/v1/revistas")
public class RevistaController {

    private final RevistaService revistaService;

    public RevistaController(RevistaService revistaService) {
        this.revistaService = revistaService;
    }


    @GetMapping
    @PreAuthorize("hasRole('EDITOR')")
    public ResponseEntity<List<RevistaResponse>> listarTodas() {
        return ResponseEntity.ok(revistaService.findAll());
    }

    @GetMapping("/activas")
    @PreAuthorize("hasRole('EDITOR')")
    public ResponseEntity<List<RevistaResponse>> listarActivas() {
        return ResponseEntity.ok(revistaService.findByActivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RevistaResponse> obtenerPorId(@PathVariable Integer id) throws ExcepcionNoExiste {
        return ResponseEntity.ok(revistaService.getById(id));
    }

    @GetMapping("/editor/{editorId}")
    public ResponseEntity<List<RevistaResponse>> listarPorEditor(@PathVariable Integer editorId) {
        return ResponseEntity.ok(revistaService.findByEditorId(editorId));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<RevistaResponse>> listarPorCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(revistaService.findByCategoriaId(categoriaId));
    }


    @PostMapping
    public ResponseEntity<RevistaResponse> crear(@RequestBody RevistaRequest request) throws ExcepcionEntidadDuplicada {
        return ResponseEntity.status(HttpStatus.CREATED).body(revistaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RevistaResponse> actualizar(
            @PathVariable Integer id, 
            @RequestBody RevistaRequest request) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste {
        return ResponseEntity.ok(revistaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) throws ExcepcionNoExiste {
        revistaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

   
    @PostMapping("/asignar-etiquetas")
    public ResponseEntity<String> asignarEtiquetas(@RequestBody RevistaEtiquetasRequest request) throws ExcepcionNoExiste {
        revistaService.guardarEtiquetas(request);
        return ResponseEntity.ok("OK");
    }
}
