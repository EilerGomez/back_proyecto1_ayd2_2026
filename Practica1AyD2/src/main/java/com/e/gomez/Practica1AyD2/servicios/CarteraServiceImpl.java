/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.repositorios.CarteraRepositorio;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author eiler
 */
@Service
public class CarteraServiceImpl implements CarteraService{
    private final CarteraRepositorio carteraRepositorio;

    public CarteraServiceImpl(CarteraRepositorio carteraRepositorio) {
        this.carteraRepositorio = carteraRepositorio;
    }


    @Override
    public EntidadCartera crearCartera(Integer usuarioId) {
        if (carteraRepositorio.existsByUsuarioId(usuarioId)) {
            throw new RuntimeException("El usuario ya tiene una cartera asignada");
        }
        
        EntidadCartera nuevaCartera = new EntidadCartera();
        nuevaCartera.setUsuarioId(usuarioId);
        nuevaCartera.setSaldo(BigDecimal.valueOf(0.0));
        nuevaCartera.setMoneda("GTQ");
        return carteraRepositorio.save(nuevaCartera);    
    }

    @Override
    public EntidadCartera obtenerPorUsuario(Integer usuarioId) throws ExcepcionNoExiste{
        return carteraRepositorio.findByUsuarioId(usuarioId)
                .orElseThrow(()-> new ExcepcionNoExiste("No existe la cartera para el usuario "+ usuarioId));    
    }

    @Override
    public void sumarSaldo(Integer carteraId, BigDecimal delta) {
        int filasAfectadas = carteraRepositorio.sumarSaldo(carteraId, delta);
        if (filasAfectadas == 0) {
            throw new RuntimeException("No se pudo actualizar el saldo: Cartera no encontrada Cartera id: " + carteraId);
        }    
    }

    @Override
    public void debitar(Integer carteraId, BigDecimal monto) {
        int filasAfectadas = carteraRepositorio.debitarSiAlcanza(carteraId, monto);
        if (filasAfectadas == 0) {
            throw new RuntimeException("Fondos insuficientes o cartera no encontrada");
        }    
    }
    
}
