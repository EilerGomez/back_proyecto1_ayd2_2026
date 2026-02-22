/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoLikes.LikeRequest;
import com.e.gomez.Practica1AyD2.dtoLikes.LikeResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.servicios.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    //
    @PostMapping
    public ResponseEntity<LikeResponse> darLike(@RequestBody LikeRequest request) throws ExcepcionNoExiste, ExcepcionEntidadDuplicada {
        // Retorna 201 Created con el objeto Like y el Usuario anidado
        return ResponseEntity.status(HttpStatus.CREATED).body(likeService.darLike(request));
    }

    // 
    @DeleteMapping("/revista/{revistaId}/usuario/{usuarioId}")
    public ResponseEntity<Void> quitarLike(
            @PathVariable Integer revistaId, 
            @PathVariable Integer usuarioId) {
        likeService.quitarLike(revistaId, usuarioId);
        return ResponseEntity.noContent().build();
    }

    // 
    @GetMapping("/revista/{revistaId}")
    public ResponseEntity<List<LikeResponse>> listarPorRevista(@PathVariable Integer revistaId) throws ExcepcionNoExiste {
        return ResponseEntity.ok(likeService.findByRevistaId(revistaId));
    }

    //
    @GetMapping("/revista/{revistaId}/conteo")
    public ResponseEntity<Integer> contarLikes(@PathVariable Integer revistaId) {
        return ResponseEntity.ok(likeService.contarLikesRevista(revistaId));
    }

    // 
    @GetMapping("/revista/{revistaId}/usuario/{usuarioId}/existe")
    public ResponseEntity<Boolean> verificarLike(
            @PathVariable Integer revistaId, 
            @PathVariable Integer usuarioId) {
        return ResponseEntity.ok(likeService.yaDioLike(revistaId, usuarioId));
    }
}
