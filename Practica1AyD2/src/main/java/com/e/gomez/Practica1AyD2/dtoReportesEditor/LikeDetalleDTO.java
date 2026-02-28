/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoReportesEditor;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author eiler
 */
@Data
@AllArgsConstructor
public class LikeDetalleDTO {
    private String username;
    private LocalDateTime fechaCreacion;
}