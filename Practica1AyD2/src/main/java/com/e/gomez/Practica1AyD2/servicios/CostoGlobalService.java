/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoRevistas.CostoGlobalResponse;
import com.e.gomez.Practica1AyD2.dtoRevistas.CostoGlobalRequest;

/**
 *
 * @author eiler
 */
public interface CostoGlobalService {
    CostoGlobalResponse obtenerCostoGlobal();
    CostoGlobalResponse actualizarCostoGlobal(CostoGlobalRequest req);
}
