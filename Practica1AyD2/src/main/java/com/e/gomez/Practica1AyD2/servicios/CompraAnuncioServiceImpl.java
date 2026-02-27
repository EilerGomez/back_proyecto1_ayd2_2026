/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoAnuncios.CompraAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.CompraAnuncioResponseDetallado;
import com.e.gomez.Practica1AyD2.dtoAnuncios.CompraAnuncioResponseSimple;
import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioAnuncioResponse;
import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionRequest;
import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import com.e.gomez.Practica1AyD2.repositorios.CompraAnuncioRepositorio;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Service
public class CompraAnuncioServiceImpl implements CompraAnuncioService {

    private final CompraAnuncioRepositorio repo;
    private final AnuncioService anuncioService;
    private final PrecioAnuncioService precioService;
    private final TransaccionCarteraService transaccionService;
    private final UsuarioService usuarioService;
    private final CarteraService carteraService;

    public CompraAnuncioServiceImpl(CompraAnuncioRepositorio repo, AnuncioService anuncioService, 
                                   PrecioAnuncioService precioService, TransaccionCarteraService transaccionService,
                                   UsuarioService usuarioService,CarteraService carteraService) {
        this.repo = repo;
        this.anuncioService = anuncioService;
        this.precioService = precioService;
        this.transaccionService = transaccionService;
        this.usuarioService=usuarioService;
        this.carteraService=carteraService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CompraAnuncioResponseSimple comprar(CompraAnuncioRequest req) throws ExcepcionNoExiste {
        PrecioAnuncioResponse precioInfo = precioService.obtenerPorId(req.getPrecioId());
        int diasCampaña = precioInfo.getPeriodoAnuncio().getDias();

        //debitar del anunciante
        TransaccionRequest tReq = new TransaccionRequest();
        tReq.setCarteraId(req.getCarteraId());
        tReq.setTipo("COMPRA_ANUNCIO");
        tReq.setDireccion("DEBITO");
        tReq.setMonto(precioInfo.getPrecio());
        tReq.setReferenciaTipo("ANUNCIO");
        tReq.setReferenciaId(req.getAnuncioId());
        tReq.setNota("Compra de anuncio ID: " + req.getAnuncioId());
        tReq.setFechaRegistradaUsuario(LocalDate.now());

        TransaccionResponse tRes = transaccionService.registrar(tReq);

        // acreditar al administrador del sistema
        EntidadCartera carteraAdmin = carteraService.obtenerPorUsuario(precioInfo.getAdmin().getId());
        TransaccionRequest tadmin = new TransaccionRequest();
        tadmin.setCarteraId(carteraAdmin.getId());
        tadmin.setTipo("COMPRA_ANUNCIO");
        tadmin.setDireccion("CREDITO");
        tadmin.setMonto(precioInfo.getPrecio());
        tadmin.setReferenciaTipo("ANUNCIO");
        tadmin.setReferenciaId(req.getAnuncioId());
        tadmin.setNota("Compra de anuncio ID: " + req.getAnuncioId());
        tadmin.setFechaRegistradaUsuario(LocalDate.now());
        
        TransaccionResponse tResa = transaccionService.registrar(tadmin);
        
        //Crear la compra
        LocalDateTime fechaFin = req.getFechaInicio().plusDays(diasCampaña);
        
        EntidadCompraAnuncio compra = EntidadCompraAnuncio.builder()
                .anuncioId(req.getAnuncioId())
                .anuncianteId(req.getAnuncianteId())
                .precioId(req.getPrecioId())
                .fechaInicio(req.getFechaInicio())
                .fechaFin(fechaFin)
                .estado("ACTIVO")
                .transaccionId(tRes.getId())
                .build();

        EntidadCompraAnuncio guardada = repo.save(compra);

        // CAMBIAR ESTADO DEL ANUNCIO A ACTIVO
        anuncioService.cambiarEstado(req.getAnuncioId(), "ACTIVO");
        UsuarioResponse ur = new UsuarioResponse(usuarioService.getById(req.getAnuncianteId()));

        //Retornar Response Detallado
        return new CompraAnuncioResponseSimple(guardada, precioService.obtenerPorId(guardada.getAnuncioId())
        );
    }

    @Override
    public List<CompraAnuncioResponseDetallado> listarPorEstado(String estado) {
        return repo.findByEstado(estado).stream()
                .map(this::mapToDetalladoResponse).toList();
    }

    @Override
    @Transactional
    public void desactivarManualmente(Integer compraId, String responsable, LocalDateTime fecha) throws ExcepcionNoExiste {
        EntidadCompraAnuncio c = repo.findById(compraId).orElseThrow(() -> new ExcepcionNoExiste("Compra no existe"));
        c.setEstado("INACTIVO");
        c.setDesactivadoPor(responsable);
        c.setFechaDesactivacion(fecha);
        repo.save(c);
        
        // También inactivamos el anuncio vinculado
        anuncioService.cambiarEstado(c.getAnuncioId(), "INACTIVO");
    }

    private CompraAnuncioResponseDetallado mapToDetalladoResponse(EntidadCompraAnuncio c) {
        try {
            PrecioAnuncioResponse p = precioService.obtenerPorId(c.getPrecioId());
            UsuarioResponse anunciante = new UsuarioResponse(usuarioService.getById(c.getAnuncianteId()));
            AnuncioResponse anuncio = anuncioService.obtenerPorId(c.getAnuncioId());
            PrecioAnuncioResponse precio = precioService.obtenerPorId(c.getPrecioId());
            return new CompraAnuncioResponseDetallado(c,anunciante,anuncio,p
            );
        } catch (Exception e) { return null; }
    }
    
    @Override
    public List<CompraAnuncioResponseDetallado> listarPorDesactivadoPor(String desactivadoPor) {
        return repo.findByDesactivadoPor(desactivadoPor).stream()
                .map(this::mapToDetalladoResponse).toList();
    }
}
