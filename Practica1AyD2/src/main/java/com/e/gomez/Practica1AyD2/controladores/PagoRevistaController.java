/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoPagosyCostos.PagoRevistaRequest;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.PagoRevistaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.PagoRevistaService;
import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pagos-revista")
public class PagoRevistaController {

    private final PagoRevistaService pagoService;

    public PagoRevistaController(PagoRevistaService pagoService) {
        this.pagoService = pagoService;
    }

    // Procesar un pago de mantenimiento (Requiere el ID de la cartera del usuario por Query Param)
    @PostMapping("/procesar")
    public ResponseEntity<PagoRevistaResponse> procesarPago(
            @RequestBody PagoRevistaRequest request,
            @RequestParam Integer carteraId) throws ExcepcionNoExiste {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagoService.procesarPago(request, carteraId));
    }

    // Consultar todos los pagos realizados por una revista específica
    @GetMapping("/revista/{revistaId}")
    public ResponseEntity<List<PagoRevistaResponse>> listarPorRevista(@PathVariable Integer revistaId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(pagoService.listarPagosPorRevista(revistaId));
    }

    // Consultar todos los pagos realizados por un editor (todas sus revistas)
    @GetMapping("/editor/{editorId}")
    public ResponseEntity<List<PagoRevistaResponse>> listarPorEditor(@PathVariable Integer editorId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(pagoService.listarPagosPorEditor(editorId));
    }

    @PatchMapping("/{id}/fecha-fin")
    public ResponseEntity<PagoRevistaResponse> actualizarFechaFin(
            @PathVariable Integer id,
            @RequestParam LocalDate fechaFin) throws ExcepcionNoExiste {
        
        return ResponseEntity.ok(pagoService.actualizarFechaFin(id, fechaFin));
    }
}
