/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtoUsuarios;

import lombok.Value;
import modelos.EntidadUsuario;

/**
 *
 * @author eiler
 */
@Value
public class NuevoUsuarioRequest {
     String nombre;
     String username;
     String apellido;
     String correo;
     String password;
     String estado;
     Integer id_rol;
     
     
    public EntidadUsuario crearEntidad(){
        EntidadUsuario nuevaEntidad= new EntidadUsuario();
        nuevaEntidad.setNombre(getNombre());
        nuevaEntidad.setUsername(getUsername());
        nuevaEntidad.setApellido(getApellido());
        nuevaEntidad.setCorreo(getCorreo());
        nuevaEntidad.setEstado("ACTIVO");
        
        return nuevaEntidad;
    }
}
