/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioBloqueoRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioBloqueoResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.PrecioBloqueoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author eiler
 */
@RestController
@RequestMapping("/v1/anuncios/precios-bloqueo")
public class PrecioBloqueoController {

    private final PrecioBloqueoService service;

    public PrecioBloqueoController(PrecioBloqueoService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // hacer esto para las partes restringidas del sistema para el usuario
    public ResponseEntity<PrecioBloqueoResponse> guardar(@RequestBody PrecioBloqueoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.asignarOActualizar(request));
    }

    @GetMapping("/revista/{revistaId}")
    public ResponseEntity<PrecioBloqueoResponse> obtener(@PathVariable Integer revistaId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(service.obtenerPorRevista(revistaId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) throws ExcepcionNoExiste {
        service.eliminarPrecio(id);
        return ResponseEntity.noContent().build();
    }
}
