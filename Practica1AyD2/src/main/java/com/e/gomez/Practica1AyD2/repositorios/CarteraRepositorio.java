/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CarteraRepositorio extends JpaRepository<EntidadCartera, Integer> {

    Optional<EntidadCartera> findByUsuarioId(Integer usuarioId) throws ExcepcionNoExiste;

    boolean existsByUsuarioId(Integer usuarioId);

  
    @Modifying
    @Transactional
    @Query("""
        UPDATE EntidadCartera c
        SET c.saldo = c.saldo + :delta
        WHERE c.id = :carteraId
    """)
    int sumarSaldo(@Param("carteraId") Integer carteraId, @Param("delta") BigDecimal delta);

  
    @Modifying
    @Transactional
    @Query("""
        UPDATE EntidadCartera c
        SET c.saldo = c.saldo - :monto
        WHERE c.id = :carteraId
          AND c.saldo >= :monto
    """)
    int debitarSiAlcanza(@Param("carteraId") Integer carteraId, @Param("monto") BigDecimal monto);
}
