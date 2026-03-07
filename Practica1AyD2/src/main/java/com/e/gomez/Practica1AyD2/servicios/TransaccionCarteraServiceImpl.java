package com.e.gomez.Practica1AyD2.servicios;

import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionRequest;
import com.e.gomez.Practica1AyD2.dtoTransacciones.TransaccionResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste; // Sugerida
import com.e.gomez.Practica1AyD2.modelos.EntidadTransaccionCartera;
import com.e.gomez.Practica1AyD2.repositorios.TransaccionCarteraRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransaccionCarteraServiceImpl implements TransaccionCarteraService {

    private final TransaccionCarteraRepositorio repo;
    private final CarteraService carteraService; 

    public TransaccionCarteraServiceImpl(TransaccionCarteraRepositorio repo, CarteraService carteraService) {
        this.repo = repo;
        this.carteraService = carteraService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class) 
    public TransaccionResponse registrar(TransaccionRequest req) throws ExcepcionNoExiste {
        
        if ("CREDITO".equalsIgnoreCase(req.getDireccion())) {
            carteraService.sumarSaldo(req.getCarteraId(), req.getMonto());
        } else if ("DEBITO".equalsIgnoreCase(req.getDireccion())) {
            carteraService.debitar(req.getCarteraId(), req.getMonto());
        } else {
            throw new IllegalArgumentException("Dirección de transacción inválida: " + req.getDireccion());
        }

        EntidadTransaccionCartera transaccion = EntidadTransaccionCartera.builder()
                .carteraId(req.getCarteraId())
                .tipo(req.getTipo())
                .direccion(req.getDireccion().toUpperCase())
                .monto(req.getMonto())
                .referenciaTipo(req.getReferenciaTipo())
                .referenciaId(req.getReferenciaId())
                .nota(req.getNota())
                .fechaCreacion(LocalDateTime.now())
                .fechaRegistradaUsuario(req.getFechaRegistradaUsuario())
                .build();

        return new TransaccionResponse(repo.save(transaccion));
    }

    @Override
    public List<TransaccionResponse> listarPorCartera(Integer carteraId) {
        return repo.findByCarteraIdOrderByFechaCreacionDesc(carteraId)
                .stream()
                .map(TransaccionResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public TransaccionResponse obtenerPorId(Integer id) throws ExcepcionNoExiste {
        EntidadTransaccionCartera t = repo.findById(id)
                .orElseThrow(() -> new ExcepcionNoExiste("La transacción con ID " + id + " no existe."));
        return new TransaccionResponse(t);
    }
}