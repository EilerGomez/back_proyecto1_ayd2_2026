/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.mantenimiento;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadCompraAnuncio;
import com.e.gomez.Practica1AyD2.modelos.EntidadPagoRevista;
import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import com.e.gomez.Practica1AyD2.repositorios.AnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.BloqueoAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.CompraAnuncioRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.RevistaRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.PagoRevistaRepositorio;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MantenimientoSistemaService {

    private final RevistaRepositorio revistaRepo;
    private final PagoRevistaRepositorio pagoRepo;
    private final BloqueoAnuncioRepositorio bloqueoRepo;//este es el del editor, cuando compra un bloqueo
    private final CompraAnuncioRepositorio repoCompraAnuncio; // este es el de anunciante, cuando compra un anuncio para mostrar en el sistema
    private final AnuncioRepositorio anuncioRepo;
    
    public MantenimientoSistemaService(RevistaRepositorio revistaRepo, PagoRevistaRepositorio pagoRepo,CompraAnuncioRepositorio repo,
            AnuncioRepositorio anuncioRepo,BloqueoAnuncioRepositorio bloqueoRepo) {
        this.revistaRepo = revistaRepo;
        this.pagoRepo = pagoRepo;
        this.repoCompraAnuncio=repo;
        this.bloqueoRepo=bloqueoRepo;
        this.anuncioRepo=anuncioRepo;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void desactivarRevistasVencidas() throws ExcepcionNoExiste {
        LocalDate hoy = LocalDate.now();
        int inactivadas = 0;
        List<EntidadRevista> revistasActivas = revistaRepo.findByActivaTrue();

        for (EntidadRevista revista : revistasActivas) {
            List<EntidadPagoRevista> pagos = pagoRepo.findByRevistaId(revista.getId());

            // Verificamos si existe AL MENOS UN pago que cubra la fecha de hoy
            boolean tienePagoVigente = pagos.stream()
                    .anyMatch(p -> !hoy.isBefore(p.getPeriodoInicio()) && !hoy.isAfter(p.getPeriodoFin()));

            // Si no tiene ningún pago vigente, la desactivamos
            if (!tienePagoVigente) {
                revista.setActiva(false);
                revistaRepo.save(revista);
                inactivadas++;
            }
        }

        if (inactivadas > 0) {
            System.out.println("Mantenimiento diario completado. Revistas desactivadas: " + inactivadas);
        }
    }
    
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void desactivarBloqueosDeAnunciosExpirados() {
        LocalDateTime hoy = LocalDateTime.now();
        
        int actualizados = bloqueoRepo.desactivarBloqueosExpirados(hoy);
        
        if (actualizados > 0) {
            System.out.println("Mantenimiento de bloqueos de anuncios: " + actualizados + " bloqueos de anuncios han expirado.");
        }
    }
    
   @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void desactivarAnunciosExpirados() {
        LocalDateTime hoy = LocalDateTime.now();

        int anunciosExpirados = anuncioRepo.expirarAnunciosVencidos(hoy);

        int comprasExpiradas = repoCompraAnuncio.expirarComprasVencidas(hoy);

        if (comprasExpiradas > 0) {
            System.out.println("Compras expiradas: " + comprasExpiradas + " compras y " 
                               + anunciosExpirados + " Anuncios expirados");
        }
}
}
