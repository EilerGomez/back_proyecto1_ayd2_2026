/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoUsuarios.NuevoPerfilRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.servicios.PerfilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/perfiles")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    // GET /v1/perfiles/usuario/10
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntidadPerfil> getByUsuarioId(@PathVariable Integer usuarioId) throws ExcepcionNoExiste {
        
        return ResponseEntity.ok(perfilService.findByUsuarioId(usuarioId));
        
    }

    // PATCH /v1/perfiles/usuario/10
    @PatchMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntidadPerfil> patchPerfil(
            @PathVariable Integer usuarioId,
            @RequestBody NuevoPerfilRequest req
    ) throws ExcepcionNoExiste {
        
        return ResponseEntity.ok(perfilService.updatePerfil(usuarioId, req));
        
    }

    @PutMapping("/usuario/{usuarioId}")
    public ResponseEntity<EntidadPerfil> putPerfil(
            @PathVariable Integer usuarioId,
            @RequestBody NuevoPerfilRequest req
    ) throws ExcepcionNoExiste {
        
        return ResponseEntity.ok(perfilService.updatePerfil(usuarioId, req));
        
    }
}
