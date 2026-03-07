/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.ImpresionAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.ImpresionAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.ImpresionAnuncioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/anuncios/impresiones")
public class ImpresionAnuncioController {

    private final ImpresionAnuncioService service;

    public ImpresionAnuncioController(ImpresionAnuncioService service) {
        this.service = service;
    }

    // Endpoint para cuando el frontend muestra el anuncio
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody ImpresionAnuncioRequest request) throws ExcepcionNoExiste {
        service.registrarImpresion(request);
        return ResponseEntity.ok().build();
    }

    // Obtener estadísticas de vistas por anuncio
    @GetMapping("/total-vistas/{anuncioId}")
    public ResponseEntity<Long> obtenerTotal(@PathVariable Integer anuncioId) {
        return ResponseEntity.ok(service.obtenerTotalVistasPorAnuncio(anuncioId));
    }

    // Listar para reportes por revista
    @GetMapping("/revista/{revistaId}")
    public ResponseEntity<List<ImpresionAnuncioResponse>> listarPorRevista(@PathVariable Integer revistaId) {
        return ResponseEntity.ok(service.listarPorRevista(revistaId));
    }
}