/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoRevistas.CostoGlobalRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.CostoGlobalResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadCostoGlobal;
import com.e.gomez.Practica1AyD2.repositorios.CostoGlobalRepositorio;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Service
public class CostoGlobalServiceImpl implements CostoGlobalService {

    private final CostoGlobalRepositorio repo;

    public CostoGlobalServiceImpl(CostoGlobalRepositorio repo) {
        this.repo = repo;
    }

    @Override
    public CostoGlobalResponse obtenerCostoGlobal() {
        return repo.findGlobalConfig()
                .map(CostoGlobalResponse::new)
                .orElse(new CostoGlobalResponse(1, BigDecimal.ZERO));
    }

    @Override
    @Transactional
    public CostoGlobalResponse actualizarCostoGlobal(CostoGlobalRequest req) {
        
        EntidadCostoGlobal costo = repo.findGlobalConfig()
                .orElse(new EntidadCostoGlobal());
        
        costo.setMonto(req.getMonto());
        return new CostoGlobalResponse(repo.save(costo));
    }
}