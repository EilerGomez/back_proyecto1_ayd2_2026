/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoEdicion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author eiler
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EdicionRequest {
    private Integer revistaId;
    private String titulo;
    private String pdfUrl;
}