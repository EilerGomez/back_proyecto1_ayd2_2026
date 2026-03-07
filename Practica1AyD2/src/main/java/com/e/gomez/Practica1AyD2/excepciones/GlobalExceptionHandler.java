/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.excepciones;

/**
 *
 * @author eiler
 */

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404
    @ExceptionHandler(ExcepcionNoExiste.class)
    public ResponseEntity<Object> manejarNoExiste(ExcepcionNoExiste ex) {
        return crearRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    //  (409)
    @ExceptionHandler(ExcepcionEntidadDuplicada.class)
    public ResponseEntity<Object> manejarDuplicado(ExcepcionEntidadDuplicada ex) {
        return crearRespuesta(HttpStatus.CONFLICT, ex.getMessage());
    }

    //  (401)
    @ExceptionHandler(ExcepcionCredencialesInvalidas.class)
    public ResponseEntity<Object> manejarCredenciales(ExcepcionCredencialesInvalidas ex) {
        return crearRespuesta(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    // (500) 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> manejarCualquierError(Exception ex) {
        return crearRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ha ocurrido un error inesperado en el sistema." +ex);
    }

    /**
     * Método helper para estandarizar la estructura del JSON de error
     */
    private ResponseEntity<Object> crearRespuesta(HttpStatus status, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", mensaje);

        return new ResponseEntity<>(body, status);
    }
}
