/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoRevistas.CostoGlobalRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.CostoGlobalResponse;
import com.e.gomez.Practica1AyD2.servicios.CostoGlobalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/costo-global")
public class CostoGlobalController {

    private final CostoGlobalService service;

    public CostoGlobalController(CostoGlobalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<CostoGlobalResponse> obtener() {
        return ResponseEntity.ok(service.obtenerCostoGlobal());
    }

  
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CostoGlobalResponse> actualizar(@RequestBody CostoGlobalRequest request) {
        return ResponseEntity.ok(service.actualizarCostoGlobal(request));
    }
}
