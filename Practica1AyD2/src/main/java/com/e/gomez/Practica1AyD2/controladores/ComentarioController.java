/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoComentarios.ComentarioRequest;
import com.e.gomez.Practica1AyD2.dtoComentarios.ComentarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.ComentarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    // --- CREAR COMENTARIO ---
    @PostMapping
    public ResponseEntity<ComentarioResponse> crear(@RequestBody ComentarioRequest request) throws ExcepcionNoExiste {
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioService.crear(request));
    }

    // --- ACTUALIZAR COMENTARIO ---
    // Recibe el ID por URL y el nuevo contenido en el cuerpo del JSON
    @PutMapping("/{id}")
    public ResponseEntity<ComentarioResponse> actualizar(
            @PathVariable Integer id, 
            @RequestBody Map<String, String> body) throws ExcepcionNoExiste {
        String nuevoContenido = body.get("contenido");
        return ResponseEntity.ok(comentarioService.actualizar(id, nuevoContenido));
    }

    // --- LISTAR POR REVISTA ---
    @GetMapping("/revista/{revistaId}")
    public ResponseEntity<List<ComentarioResponse>> listarPorRevista(@PathVariable Integer revistaId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(comentarioService.listarPorRevista(revistaId));
    }

    // --- LISTAR POR USUARIO ---
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ComentarioResponse>> listarPorUsuario(@PathVariable Integer usuarioId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(comentarioService.listarPorUsuario(usuarioId));
    }

    // --- ELIMINAR COMENTARIO ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) throws ExcepcionNoExiste {
        comentarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}