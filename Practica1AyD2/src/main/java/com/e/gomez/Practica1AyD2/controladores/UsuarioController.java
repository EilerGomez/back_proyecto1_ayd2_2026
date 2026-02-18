/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.e.gomez.Practica1AyD2.controladores;

import com.e.gomez.Practica1AyD2.dtoUsuarios.NuevoUsuarioRequest;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioCompletoResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioUpdateRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.modelos.EntidadRol;
import com.e.gomez.Practica1AyD2.modelos.EntidadUsuario;
import com.e.gomez.Practica1AyD2.repositorios.PerfilRepositorio;
import com.e.gomez.Practica1AyD2.servicios.PerfilService;
import com.e.gomez.Practica1AyD2.servicios.RolService;
import com.e.gomez.Practica1AyD2.servicios.UsuarioService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author eiler
 */

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {
    private final UsuarioService servicio;
    private final PerfilService servicioPerfil;
    private final RolService rolService;
    
    @Autowired
    public UsuarioController(UsuarioService servicioU, PerfilService servicioP, RolService rs){
        this.servicio=servicioU;
        this.servicioPerfil = servicioP;
        this.rolService=rs;
    }
    
    @PostMapping()
    public ResponseEntity<UsuarioCompletoResponse> createdUser(@RequestBody NuevoUsuarioRequest nuevoU) throws ExcepcionEntidadDuplicada, ExcepcionNoExiste{
        UsuarioResponse usuarioNuevo;
        usuarioNuevo = new UsuarioResponse(servicio.crearUsuario(nuevoU));
        EntidadPerfil perfil= servicioPerfil.findByUsuarioId(usuarioNuevo.getId());
        EntidadRol rol = rolService.traerRolDeUsuario(usuarioNuevo.getId());
        UsuarioCompletoResponse response = new UsuarioCompletoResponse(usuarioNuevo, perfil, rol);
       return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<UsuarioCompletoResponse>> findAllUsers() throws ExcepcionNoExiste, ExcepcionEntidadDuplicada{
          List<UsuarioCompletoResponse> respuesta = new ArrayList<>();

        for (var usuario : servicio.findAll()) {
            UsuarioResponse usuarioDto = new UsuarioResponse(usuario);
            EntidadPerfil perfil = servicioPerfil.findByUsuarioId(usuario.getId());
            EntidadRol rol = rolService.traerRolDeUsuario(usuario.getId()); // puede lanzar

            respuesta.add(new UsuarioCompletoResponse(usuarioDto, perfil, rol));
        }

        return ResponseEntity.ok(respuesta);

    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioCompletoResponse> getById(@PathVariable Integer id)
            throws ExcepcionNoExiste {

        EntidadUsuario usuario = servicio.getById(id);
        UsuarioResponse usuarioDto = new UsuarioResponse(usuario);

        EntidadPerfil perfil = servicioPerfil.findByUsuarioId(id);
        EntidadRol rol = rolService.traerRolDeUsuario(id);

        UsuarioCompletoResponse response = new UsuarioCompletoResponse(usuarioDto, perfil, rol);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioCompletoResponse> update(@PathVariable Integer id,
            @RequestBody UsuarioUpdateRequest dataAActualizar)
            throws ExcepcionEntidadDuplicada, ExcepcionNoExiste {

        UsuarioResponse usuarioActualizado = servicio.actializarUsuario(id, dataAActualizar);

        EntidadPerfil perfil = servicioPerfil.findByUsuarioId(id);
        EntidadRol rol = rolService.traerRolDeUsuario(id);

        UsuarioCompletoResponse response = new UsuarioCompletoResponse(usuarioActualizado, perfil, rol);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws ExcepcionNoExiste {
        servicio.eliminarUsuario(id);
        return ResponseEntity.noContent().build(); // 204
    }
}
