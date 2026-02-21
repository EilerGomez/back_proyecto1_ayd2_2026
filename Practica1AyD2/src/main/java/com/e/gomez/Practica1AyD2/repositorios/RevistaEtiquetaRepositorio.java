/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

import com.e.gomez.Practica1AyD2.modelos.EntidadRevistaEtiqueta;
import com.e.gomez.Practica1AyD2.modelos.RevistaEtiquetaId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author eiler
 */
@Repository
public interface RevistaEtiquetaRepositorio extends JpaRepository<EntidadRevistaEtiqueta, RevistaEtiquetaId> {
    List<EntidadRevistaEtiqueta> findById_RevistaId(Integer revistaId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM EntidadRevistaEtiqueta re WHERE re.id.revistaId = :revistaId")
    void eliminarEtiquetasDeRevista(@Param("revistaId") Integer revistaId);
}
