/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "carteras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntidadCartera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @JoinColumn(name = "usuario_id")
    private Integer usuarioId;
  
    @Column(precision = 12, scale = 2)
    private BigDecimal saldo;

    @Column(length = 3,columnDefinition = "char(3)")
    private String moneda;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;


}