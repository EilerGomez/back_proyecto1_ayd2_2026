/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.modelos;

/**
 *
 * @author eiler
 */

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "likes_revista", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"revista_id", "usuario_id"})
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EntidadLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "revista_id")
    private Integer revistaId;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
