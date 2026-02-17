/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtoUsuarios;

import lombok.Data;
import modelos.EntidadUsuario;

/**
 *
 * @author eiler
 */
@Data
public class UsuarioResponse {
    private int id;
    private String nombre;
    private String username;
    private String apellido;
    private String correo;
    private String estado;
    
    public UsuarioResponse(EntidadUsuario entidadUsuario){
        this.id=entidadUsuario.getId();
        this.nombre=entidadUsuario.getNombre();
        this.username=entidadUsuario.getUsername();
        this.apellido=entidadUsuario.getApellido();
        this.correo=entidadUsuario.getCorreo();
        this.estado=entidadUsuario.getEstado();
    }
            
 
}
