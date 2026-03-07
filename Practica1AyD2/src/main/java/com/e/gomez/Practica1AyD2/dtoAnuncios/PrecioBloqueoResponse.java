/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.dtoAnuncios;

import com.e.gomez.Practica1AyD2.modelos.EntidadPrecioBloqueo;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

/**
 *
 * @author eiler
 */
@Getter
@Setter
@NoArgsConstructor
public class PrecioBloqueoResponse {
    int id;
    int revistaId;
    BigDecimal costoPorDia;
    int adminId;

    public PrecioBloqueoResponse(EntidadPrecioBloqueo e) {
        this.id = e.getId();
        this.revistaId = e.getRevistaId();
        this.costoPorDia = e.getCostoPorDia();
        this.adminId = e.getAdminId();
    }
}
