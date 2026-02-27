/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.repositorios;

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPagoRevista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author eiler
 */
public interface PagoRevistaRepositorio extends JpaRepository<EntidadPagoRevista, Integer> {
    List<EntidadPagoRevista> findByRevistaId(Integer revistaId) throws ExcepcionNoExiste;
    List<EntidadPagoRevista> findByEditorId(Integer editorId) throws ExcepcionNoExiste;
}
