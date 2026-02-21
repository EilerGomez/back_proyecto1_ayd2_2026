/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.repositorios.CarteraRepositorio;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
public interface CarteraService {


    // Método para crear una cartera nueva
    @Transactional
    public EntidadCartera crearCartera(Integer usuarioId);

    public EntidadCartera obtenerPorUsuario(Integer usuarioId) throws ExcepcionNoExiste;

    @Transactional
    public void sumarSaldo(Integer carteraId, BigDecimal delta);

    @Transactional
    public void debitar(Integer carteraId, BigDecimal monto) ;
}
