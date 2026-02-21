/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.modelos;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.*;



@Entity
@Table(name = "transacciones_cartera")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntidadTransaccionCartera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @JoinColumn(name = "cartera_id")
    private Integer carteraId;

    @Column(length = 16, nullable = false)
    private String tipo; // RECARGA, COMPRA_ANUNCIO, etc

    @Column(length = 7, nullable = false)
    private String direccion; // CREDITO, DEBITO

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "referencia_tipo", length = 50)
    private String referenciaTipo;

    @Column(name = "referencia_id")
    private int referenciaId;

    @Column(length = 255)
    private String nota;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion ;

    @Column(name = "fecha_registrada_usuario")
    @Temporal(TemporalType.DATE) 
    private LocalDate fechaRegistradaUsuario;
}
