/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoResponse;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.PagoRevistaRequest;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.PagoRevistaResponse;
import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionRequest;
import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.modelos.EntidadHistorialCosto;
import com.e.gomez.Practica1AyD2.modelos.EntidadPagoRevista;
import com.e.gomez.Practica1AyD2.repositorios.PagoRevistaRepositorio;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Service
public class PagoRevistaServiceImpl implements PagoRevistaService {

    private final PagoRevistaRepositorio pagoRepo;
    private final TransaccionCarteraService transaccionService;
    private final RevistaService revistaService;
    private final HistorialCostoService historialCostoService;
    private final CarteraService carteraService;

    public PagoRevistaServiceImpl(PagoRevistaRepositorio pagoRepo, TransaccionCarteraService transaccionService,
            RevistaService revistaService,HistorialCostoService historialCosto,CarteraService carteraService) {
        this.pagoRepo = pagoRepo;
        this.transaccionService = transaccionService;
        this.revistaService=revistaService;
        this.historialCostoService=historialCosto;
        this.carteraService=carteraService;
    }

    @Override
    @Transactional
    public PagoRevistaResponse procesarPago(PagoRevistaRequest req, Integer carteraId) throws ExcepcionNoExiste {
        //Primero intentamos registrar la transacción en la cartera (Débito)
        TransaccionRequest tReq = new TransaccionRequest();
        tReq.setCarteraId(carteraId);
        tReq.setTipo("PAGO_MANTENIMIENTO");
        tReq.setDireccion("DEBITO");
        tReq.setMonto(req.getMonto());
        tReq.setReferenciaTipo("REVISTA");
        tReq.setReferenciaId(req.getRevistaId());
        tReq.setNota("Pago de mantenimiento revista ID: " + req.getRevistaId());
        tReq.setFechaRegistradaUsuario(req.getPeriodoInicio());

        TransaccionResponse tRes = transaccionService.registrar(tReq);
        
        EntidadPagoRevista pago = new EntidadPagoRevista();
        pago.setRevistaId(req.getRevistaId());
        pago.setEditorId(req.getEditorId());
        pago.setMonto(req.getMonto());
        pago.setFechaPago(LocalDate.now());
        pago.setPeriodoInicio(req.getPeriodoInicio());
        pago.setPeriodoFin(req.getPeriodoFin());
        pago.setTransaccionId(tRes.getId());
        
        //acreditar a administrador
        HistorialCostoResponse historial_costo = historialCostoService.obtenerCostoVigente(req.getRevistaId());
        EntidadCartera carteraAdmin = carteraService.obtenerPorUsuario(historial_costo.getAdminId());
        
        TransaccionRequest tReq2 = new TransaccionRequest();
        tReq2.setCarteraId(carteraAdmin.getId());
        tReq2.setTipo("PAGO_MANTENIMIENTO");
        tReq2.setDireccion("CREDITO");
        tReq2.setMonto(req.getMonto());
        tReq2.setReferenciaTipo("REVISTA");
        tReq2.setReferenciaId(req.getRevistaId());
        tReq2.setNota("Pago de mantenimiento revista ID: " + req.getRevistaId());
        tReq2.setFechaRegistradaUsuario(req.getPeriodoInicio());
        
        TransaccionResponse tresponseAdmin = transaccionService.registrar(tReq2);
        
        
        //la revista pasa a estado activo
        revistaService.cambiarEstado(req.getRevistaId(), true);
        
        return new PagoRevistaResponse(pagoRepo.save(pago));
    }

    @Override
    public List<PagoRevistaResponse> listarPagosPorRevista(Integer revistaId) throws ExcepcionNoExiste {
        return pagoRepo.findByRevistaId(revistaId).stream()
                .map(PagoRevistaResponse::new)
                .toList();
    }

    @Override
    public List<PagoRevistaResponse> listarPagosPorEditor(Integer editorId) throws ExcepcionNoExiste {
        return pagoRepo.findByEditorId(editorId).stream()
                .map(PagoRevistaResponse::new)
                .toList();
    }
}
