/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

import java.util.List;
import java.util.Optional;
import com.e.gomez.Practica1AyD2.modelos.EntidadRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author eiler
 */

@Repository
public interface RolRepositorio extends JpaRepository<EntidadRol, Integer>{
        Optional<EntidadRol> findByNombre(String nombre);
        Optional<EntidadRol> findById(Integer id);
        boolean existsById(Integer id);
        
        List<EntidadRol> findAll();
}
