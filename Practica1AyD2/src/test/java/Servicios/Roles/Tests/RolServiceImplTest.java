/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Roles.Tests;

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import java.util.List;
import com.e.gomez.Practica1AyD2.modelos.EntidadRol;
import com.e.gomez.Practica1AyD2.modelos.Entidad_Usuario_Rol;
import com.e.gomez.Practica1AyD2.modelos.UsuarioRolId;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import com.e.gomez.Practica1AyD2.repositorios.RolRepositorio;
import com.e.gomez.Practica1AyD2.repositorios.UsuarioRolRepositorio;
import com.e.gomez.Practica1AyD2.servicios.RolServiceImpl;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *
 * @author eiler
 */

@ExtendWith(MockitoExtension.class)
public class RolServiceImplTest {
    
        @Mock RolRepositorio rolRepositorio;
        
         @Mock
        private UsuarioRolRepositorio userRorepo;
        
        @InjectMocks RolServiceImpl service;
        
        private EntidadRol Rol(int id, String nombre){
            return new EntidadRol(id,nombre);
        }
        
        
    //TEST DE GETALL
        
    @Test
    void devolverRoles(){
        //Arrage
        List<EntidadRol> lista = List.of(Rol(1,"ADMIN"),Rol(2,"EDITOR"));
        when(rolRepositorio.findAll()).thenReturn(lista);
        
        //ACT
        List<EntidadRol> result = service.findAll();
        
        //Assert
        assertEquals(2, result.size());
        verify(rolRepositorio, times(1)).findAll();
        verifyNoMoreInteractions(rolRepositorio);
    }  
    
    // Tests de findById(Integer)

    @Test
    void findById_cuandoExiste_retornaRol() throws Exception {
        //Arrage
        Integer id = 1;
        EntidadRol rol = new EntidadRol();
        rol.setId(id);

        
        when(rolRepositorio.findById(id)).thenReturn(Optional.of(rol));

        //ACT
        EntidadRol result = service.findById(id);

        //Assert
        assertNotNull(result);
        assertSame(rol, result);
        verify(rolRepositorio).findById(id);
        verifyNoMoreInteractions(rolRepositorio);
    }

    @Test
    void findById_cuandoNoExiste_lanzaExcepcionNoExiste() {
        Integer id = 999;
        when(rolRepositorio.findById(id)).thenReturn(Optional.empty());

        ExcepcionNoExiste ex = assertThrows(ExcepcionNoExiste.class, () -> service.findById(id));
        assertEquals("Rol no encontrado", ex.getMessage());

        verify(rolRepositorio).findById(id);
        verifyNoMoreInteractions(rolRepositorio);
    }
    
    // Tests de buscarIdRolByIdUsuario(Integer)

    @Test
    void buscarIdRolByIdUsuario_cuandoExiste_retornaEntidadUsuarioRol() throws Exception {
        Integer idUsuario = 10;

        UsuarioRolId embId = new UsuarioRolId();

        embId.setUsuarioId(idUsuario);
        embId.setRolId(2);

        Entidad_Usuario_Rol ur = new Entidad_Usuario_Rol();
        ur.setId(embId);

        when(userRorepo.findByIdUsuarioId(idUsuario)).thenReturn(ur);

        Entidad_Usuario_Rol result = service.buscarIdRolByIdUsuario(idUsuario);

        assertNotNull(result);
        assertEquals(2, result.getId().getRolId());
        verify(userRorepo).findByIdUsuarioId(idUsuario);
        verifyNoMoreInteractions(userRorepo);
    }

    @Test
    void buscarIdRolByIdUsuario_cuandoNoExiste_retornaNull_segunTuImplementacionActual() throws Exception {

        Integer idUsuario = 404;
        when(userRorepo.findByIdUsuarioId(idUsuario)).thenReturn(null);

        Entidad_Usuario_Rol result = service.buscarIdRolByIdUsuario(idUsuario);

        assertNull(result);
        verify(userRorepo).findByIdUsuarioId(idUsuario);
        verifyNoMoreInteractions(userRorepo);
    }
    
    // Tests de traerRolDeUsuario(Integer)

    @Test
    void traerRolDeUsuario_cuandoExisteRelacion_yRolExiste_retornaRol() throws Exception {
        Integer idUsuario = 10;
        Integer idRol = 2;

        UsuarioRolId embId = new UsuarioRolId();
        embId.setUsuarioId(idUsuario);
        embId.setRolId(idRol);

        Entidad_Usuario_Rol ur = new Entidad_Usuario_Rol();
        ur.setId(embId);

        EntidadRol rol = new EntidadRol();
        // rol.setId(idRol);

        when(userRorepo.findByIdUsuarioId(idUsuario)).thenReturn(ur);
        when(rolRepositorio.findById(idRol)).thenReturn(Optional.of(rol));

        EntidadRol result = service.traerRolDeUsuario(idUsuario);

        assertNotNull(result);
        assertSame(rol, result);

        verify(userRorepo).findByIdUsuarioId(idUsuario);
        verify(rolRepositorio).findById(idRol);
        verifyNoMoreInteractions(userRorepo, rolRepositorio);
    }

    @Test
    void traerRolDeUsuario_cuandoNoExisteRol_lanzaExcepcionNoExiste() {
        Integer idUsuario = 10;
        Integer idRol = 999;

        UsuarioRolId embId = new UsuarioRolId();
        embId.setUsuarioId(idUsuario);
        embId.setRolId(idRol);

        Entidad_Usuario_Rol ur = new Entidad_Usuario_Rol();
        ur.setId(embId);

        when(userRorepo.findByIdUsuarioId(idUsuario)).thenReturn(ur);
        when(rolRepositorio.findById(idRol)).thenReturn(Optional.empty());

        ExcepcionNoExiste ex = assertThrows(ExcepcionNoExiste.class, () -> service.traerRolDeUsuario(idUsuario));
        assertEquals("Rol no encontrado", ex.getMessage());

        verify(userRorepo).findByIdUsuarioId(idUsuario);
        verify(rolRepositorio).findById(idRol);
        verifyNoMoreInteractions(userRorepo, rolRepositorio);
    }


    
}
