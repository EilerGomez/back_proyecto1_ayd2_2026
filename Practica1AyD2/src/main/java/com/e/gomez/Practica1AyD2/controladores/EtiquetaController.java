/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaRequest;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.EtiquetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/etiquetas")
public class EtiquetaController {

    private final EtiquetaService service;

    public EtiquetaController(EtiquetaService service) {
        this.service = service;
    }

    @GetMapping
    public List<EtiquetaResponse> listar() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EtiquetaResponse> obtener(@PathVariable Integer id) throws ExcepcionNoExiste {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<EtiquetaResponse> guardar(@RequestBody EtiquetaRequest request) throws ExcepcionEntidadDuplicada {
        return ResponseEntity.status(201).body(service.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EtiquetaResponse> editar(@PathVariable Integer id, @RequestBody EtiquetaRequest request) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) throws ExcepcionNoExiste {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}