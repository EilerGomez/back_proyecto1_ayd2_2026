/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author eiler
 */
@Entity(name="Perfil")
@Table(name = "perfiles")
@Data
@NoArgsConstructor
public class EntidadPerfil {
    @Id
    @Column
    private Integer usuario_id;
    @Column
    private String foto_url;
    @Column
    private String hobbies;
    @Column
    private String intereses;
    @Column
    private String descripcion;
    @Column
    private String gustos;
}
