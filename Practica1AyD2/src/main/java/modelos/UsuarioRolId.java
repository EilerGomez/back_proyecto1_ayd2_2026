/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author eiler  esta clase guarda la llave compuesta por la tabla usuario_roles
 */
@Embeddable
@Data
public class UsuarioRolId implements Serializable{
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "rol_id")
    private Integer rolId;

    public UsuarioRolId(Integer idUsuario, Integer idRol){
        this.rolId=idRol;
        this.usuarioId=idUsuario;
    }
}
