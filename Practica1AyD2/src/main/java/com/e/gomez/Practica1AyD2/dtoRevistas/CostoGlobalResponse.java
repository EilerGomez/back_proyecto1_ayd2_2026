package com.e.gomez.Practica1AyD2.dtoRevistas;


import com.e.gomez.Practica1AyD2.modelos.EntidadCostoGlobal;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Value;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author eiler
 */
@Value
@AllArgsConstructor
public class CostoGlobalResponse {
    int id;
    BigDecimal monto;

    public CostoGlobalResponse(EntidadCostoGlobal e) {
        this.id = e.getId();
        this.monto = e.getMonto();
    }
}
