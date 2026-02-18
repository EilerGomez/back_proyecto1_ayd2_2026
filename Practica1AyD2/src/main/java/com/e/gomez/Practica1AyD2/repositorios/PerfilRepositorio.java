/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

import java.util.List;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eiler
 */
@Repository
public interface PerfilRepositorio extends JpaRepository<EntidadPerfil, Integer>{
    EntidadPerfil findByUsuarioId(Integer usuarioId);
    
    boolean existsByUsuarioId (Integer usuarioId);
}
