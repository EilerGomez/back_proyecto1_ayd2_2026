/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.BloqueoAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.BloqueoAnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioBloqueoResponse;
import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadBloqueoAnuncio;
import com.e.gomez.Practica1AyD2.repositorios.BloqueoAnuncioRepositorio;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Service
public class BloqueoAnuncioServiceImpl implements BloqueoAnuncioService {

    private final BloqueoAnuncioRepositorio repo;
    private final PrecioBloqueoService precioBloqueoService;
    private final CarteraService carteraService;
    private final TransaccionCarteraService transaccionService;

    public BloqueoAnuncioServiceImpl(BloqueoAnuncioRepositorio repo, 
                                     PrecioBloqueoService precioBloqueoService,
                                     CarteraService carteraService,
                                     TransaccionCarteraService transaccionService) {
        this.repo = repo;
        this.precioBloqueoService = precioBloqueoService;
        this.carteraService = carteraService;
        this.transaccionService = transaccionService;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BloqueoAnuncioResponse contratarBloqueo(BloqueoAnuncioRequest req) throws ExcepcionNoExiste {
        
        // 
        PrecioBloqueoResponse precioCfg = precioBloqueoService.obtenerPorRevista(req.getRevistaId());
        BigDecimal montoTotal = precioCfg.getCostoPorDia().multiply(new BigDecimal(req.getDias()));

        // 
        Integer carteraAdminId = carteraService.obtenerPorUsuario(precioCfg.getAdminId()).getId();

        // 
        TransaccionRequest tDebito = new TransaccionRequest();
        tDebito.setCarteraId(req.getCarteraEditorId());
        tDebito.setTipo("BLOQUEO_ANUNCIO");
        tDebito.setDireccion("DEBITO");
        tDebito.setMonto(montoTotal);
        tDebito.setReferenciaTipo("REVISTA");
        tDebito.setReferenciaId(req.getRevistaId());
        tDebito.setNota("Pago bloqueo anuncios revista: " + req.getRevistaId());
        var resDebito = transaccionService.registrar(tDebito);

        // 
        TransaccionRequest tCredito = new TransaccionRequest();
        tCredito.setCarteraId(carteraAdminId);
        tCredito.setTipo("GANANCIA_BLOQUEO");
        tCredito.setDireccion("CREDITO");
        tCredito.setMonto(montoTotal);
        tCredito.setReferenciaTipo("REVISTA");
        tCredito.setReferenciaId(req.getRevistaId());
        tCredito.setNota("Ganancia por bloqueo de revista: " + req.getRevistaId());
        transaccionService.registrar(tCredito);

        //
        LocalDateTime inicio = LocalDateTime.now();
        EntidadBloqueoAnuncio bloqueo = EntidadBloqueoAnuncio.builder()
                .revistaId(req.getRevistaId())
                .editorId(req.getEditorId())
                .dias(req.getDias())
                .fechaInicio(inicio)
                .fechaFin(inicio.plusDays(req.getDias()))
                .monto(montoTotal)
                .estado("ACTIVO")
                .transaccionId(resDebito.getId())
                .build();

        return new BloqueoAnuncioResponse(repo.save(bloqueo));
    }

    @Override
    public List<BloqueoAnuncioResponse> listarPorRevista(Integer revistaId) {
        
        return repo.findByRevistaId(revistaId).stream()
                .map(BloqueoAnuncioResponse::new)
                .toList();
    }

    @Override
    public BloqueoAnuncioResponse obtenerActivoPorRevista(Integer revistaId) throws ExcepcionNoExiste {
        LocalDateTime ahora = LocalDateTime.now();
        
        
        return repo.findByRevistaIdAndEstado(revistaId, "ACTIVO")
                .stream()
                .filter(b -> ahora.isAfter(b.getFechaInicio()) && ahora.isBefore(b.getFechaFin()))
                .findFirst()
                .map(BloqueoAnuncioResponse::new)
                .orElseThrow(() -> new ExcepcionNoExiste("La revista " + revistaId + " no tiene un bloqueo de anuncios vigente."));
    }
}
