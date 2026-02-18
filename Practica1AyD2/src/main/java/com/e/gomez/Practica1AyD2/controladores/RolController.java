/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

import com.e.gomez.Practica1AyD2.dtoRoles.RolesResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.e.gomez.Practica1AyD2.servicios.RolService;

/**
 *
 * @author eiler
 */
@RestController
@RequestMapping("/v1/roles")
public class RolController {
    private final RolService rolService;
    
    @Autowired
    public RolController(RolService rolService){
        this.rolService=rolService;
    }
    
   @GetMapping
   public ResponseEntity<List<RolesResponse>> findAllRoles(){
       List<RolesResponse> dtos = rolService.findAll().stream()
               .map(RolesResponse::new)
               .toList();
       
       return ResponseEntity.ok(dtos);
   }
}
