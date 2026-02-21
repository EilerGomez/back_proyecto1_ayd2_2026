/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoCategorias.CategoriaRequest;
import com.e.gomez.Practica1AyD2.dtoCategorias.CategoriaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCategoria;
import com.e.gomez.Practica1AyD2.servicios.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaResponse> listar() {
        List<CategoriaResponse> dtos = service.findAll().stream()
                .map(CategoriaResponse::new)
                .toList();
        
        return dtos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtener(@PathVariable Integer id) throws ExcepcionNoExiste {
        CategoriaResponse dto = new CategoriaResponse(service.getById(id));
        return ResponseEntity.ok((dto));
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> guardar(@RequestBody CategoriaRequest request) throws ExcepcionEntidadDuplicada {
        CategoriaResponse dto = new CategoriaResponse(service.crear(request));
        return ResponseEntity.status(201).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> editar(@PathVariable Integer id, @RequestBody CategoriaRequest request) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste {
        CategoriaResponse dto = new CategoriaResponse(service.actualizar(id, request));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) throws ExcepcionNoExiste {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}