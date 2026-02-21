/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.e.gomez.Practica1AyD2.servicios;

/**
 *
 * @author eiler
 */
import com.e.gomez.Practica1AyD2.modelos.EntidadCategoria;
import com.e.gomez.Practica1AyD2.dtoCategorias.CategoriaRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;

public interface CategoriaService {
    List<EntidadCategoria> findAll();
    EntidadCategoria getById(Integer id) throws ExcepcionNoExiste;
    EntidadCategoria crear(CategoriaRequest request) throws ExcepcionEntidadDuplicada;
    EntidadCategoria actualizar(Integer id, CategoriaRequest request) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste;
    void eliminar(Integer id) throws ExcepcionNoExiste;
}