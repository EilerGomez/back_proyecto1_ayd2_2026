/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.modelos.EntidadRevista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RevistaRepositorio extends JpaRepository<EntidadRevista, Integer> {


    // Buscar todas las revistas de un editor específico
    List<EntidadRevista> findByEditorId(Integer editorId);

    // Buscar revistas por categoría
    List<EntidadRevista> findByCategoriaId(Integer categoriaId);

    // Buscar revistas activas
    List<EntidadRevista> findByActivaTrue();



    // Verificar si existe una revista con el mismo título (para evitar duplicados al crear)
    boolean existsByTitulo(String titulo);

  
    @Query("SELECT COUNT(r) > 0 FROM EntidadRevista r WHERE r.titulo = :titulo AND r.id != :id")
    boolean existeTituloEnOtraRevista(@Param("titulo") String titulo, @Param("id") Integer id);



    List<EntidadRevista> findByTituloContainingIgnoreCase(String titulo);
}