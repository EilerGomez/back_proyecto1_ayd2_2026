/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package repositorios;

import modelos.Entidad_Usuario_Rol;
import modelos.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author eiler
 */
public interface UsuarioRolRepositorio extends JpaRepository<Entidad_Usuario_Rol, UsuarioRolId> {
    
}
