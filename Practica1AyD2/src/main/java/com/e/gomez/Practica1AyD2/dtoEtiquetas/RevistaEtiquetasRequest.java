/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoEtiquetas;

import java.util.List;
import lombok.Data;

/**
 *
 * @author eiler
 */
@Data
public class RevistaEtiquetasRequest {
    private Integer idRevista;
    private List<Integer> etiquetasIds;
}
