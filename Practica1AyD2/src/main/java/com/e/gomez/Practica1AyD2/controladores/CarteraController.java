/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.servicios.CarteraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/v1/carteras")
@CrossOrigin("*") 
public class CarteraController {

    private final CarteraService carteraService;

    public CarteraController(CarteraService carteraService) {
        this.carteraService = carteraService;
    }

    /**
     * Obtener los datos de la cartera de un usuario (Saldo y Moneda)
     * GET /v1/carteras/usuario/5
     * @param usuarioId
     * @return 
     * @throws com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntidadCartera> obtenerPorUsuario(@PathVariable Integer usuarioId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(carteraService.obtenerPorUsuario(usuarioId));
    }

    /**
     * Endpoint para recargar saldo (Simulación de pago)
     * POST /v1/carteras/recargar
     * Body: { "usuarioId": 5, "monto": 100.50 }
     * @param payload
     * @return 
     * @throws com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste
     */
    @PostMapping("/recargar")
    public ResponseEntity<Void> recargar(@RequestBody Map<String, Object> payload) throws ExcepcionNoExiste {
        Integer usuarioId = (Integer) payload.get("usuarioId");
        BigDecimal monto = new BigDecimal(payload.get("monto").toString());
        
        // Primero obtenemos la cartera para tener el ID real de la cartera
        EntidadCartera cartera = carteraService.obtenerPorUsuario(usuarioId);
        
        // Sumamos el saldo
        carteraService.sumarSaldo(cartera.getId(), monto);
        
        return ResponseEntity.ok().build();
    }
}