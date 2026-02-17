/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import dtoUsuarios.NuevoUsuarioRequest;
import dtoUsuarios.UsuarioResponse;
import dtoUsuarios.UsuarioUpdateRequest;
import excepciones.ExcepcionEntidadDuplicada;
import excepciones.ExcepcionNoExiste;
import java.util.List;
import modelos.EntidadUsuario;

/**
 *
 * @author eiler
 */
public interface UsuarioService {
    EntidadUsuario crearUsuario(NuevoUsuarioRequest nuevoU) throws ExcepcionEntidadDuplicada,ExcepcionNoExiste;
    EntidadUsuario getById(Integer id) throws ExcepcionNoExiste;
    List<EntidadUsuario> findAll();
    
    void eliminarUsuario (Integer id) throws ExcepcionNoExiste;
    
    UsuarioResponse actializarUsuario (Integer id, UsuarioUpdateRequest dataAActualizar) throws ExcepcionEntidadDuplicada,ExcepcionNoExiste;
    
}
