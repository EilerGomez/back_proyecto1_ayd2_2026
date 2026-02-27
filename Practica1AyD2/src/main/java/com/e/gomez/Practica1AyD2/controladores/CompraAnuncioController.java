/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

import com.e.gomez.Practica1AyD2.dtoAnuncios.CompraAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.CompraAnuncioResponseDetallado;
import com.e.gomez.Practica1AyD2.dtoAnuncios.CompraAnuncioResponseSimple;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.CompraAnuncioService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author eiler
 */
@RestController
@RequestMapping("/v1/anuncios/compras")
public class CompraAnuncioController {

    private final CompraAnuncioService compraService;

    public CompraAnuncioController(CompraAnuncioService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<CompraAnuncioResponseSimple> realizarCompra(@RequestBody CompraAnuncioRequest request) throws ExcepcionNoExiste {
        return ResponseEntity.status(HttpStatus.CREATED).body(compraService.comprar(request));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<CompraAnuncioResponseDetallado>> listarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(compraService.listarPorEstado(estado));
    }

    @GetMapping("/desactivado-por/{quien}")
    public ResponseEntity<List<CompraAnuncioResponseDetallado>> listarPorDesactivado(@PathVariable String quien) {
        return ResponseEntity.ok(compraService.listarPorDesactivadoPor(quien));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(
            @PathVariable Integer id, 
            @RequestParam String responsable,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) throws ExcepcionNoExiste {
        compraService.desactivarManualmente(id, responsable, fecha);
        return ResponseEntity.noContent().build();
    }
}
