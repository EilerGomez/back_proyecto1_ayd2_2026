/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Revistas.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionRequest;
import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadEdicion;
import com.e.gomez.Practica1AyD2.repositorios.EdicionRepositorio;
import com.e.gomez.Practica1AyD2.servicios.EdicionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EdicionServiceImplTest {

    @Mock
    private EdicionRepositorio repo;

    @InjectMocks
    private EdicionServiceImpl service;

    @Test
    void crearEdicion_DeberiaCalcularOrdinalCorrectamente_ParaPrimeraEdicion() {
        // Arrange
        EdicionRequest req = new EdicionRequest();
        req.setRevistaId(1);
        req.setTitulo("Edición Especial");
        req.setPdfUrl("http://pdf.com");

        when(repo.countByRevistaId(1)).thenReturn(0L); // 0 + 1 = 1
        when(repo.save(any(EntidadEdicion.class))).thenAnswer(invocation -> {
            EntidadEdicion ed = invocation.getArgument(0);
            ed.setId(100); 
            ed.setFechaPublicacion(LocalDateTime.now());
            return ed;
        });

        // Act
        EdicionResponse res = service.crearEdicion(req);

        // Assert
        assertEquals("1era Edición", res.getNumeroEdicion());
        verify(repo).save(any(EntidadEdicion.class));
    }

    @Test
    void crearEdicion_DeberiaCalcularOrdinal_ParaCasosEspeciales() {
        // Probamos el caso n=2 (2da) y n=10 (10va)
        when(repo.countByRevistaId(1)).thenReturn(1L); // 1+1 = 2
        when(repo.save(any(EntidadEdicion.class))).thenAnswer(i -> {
            EntidadEdicion e = i.getArgument(0);
            e.setId(200); 
            e.setFechaPublicacion(LocalDateTime.now());
            return e;
        });

        EdicionResponse res2 = service.crearEdicion(new EdicionRequest(1, "T", "U"));
        assertEquals("2da Edición", res2.getNumeroEdicion());

        when(repo.countByRevistaId(1)).thenReturn(9L); // 9+1 = 10
        EdicionResponse res10 = service.crearEdicion(new EdicionRequest(1, "T", "U"));
        assertEquals("10ma Edición", res10.getNumeroEdicion());
    }

    @Test
    void getById_DeberiaRetornarEdicion_SiExiste() throws ExcepcionNoExiste {
        // Arrange
        EntidadEdicion entidad = new EntidadEdicion();
        entidad.setId(5);
        entidad.setRevistaId(1);
        entidad.setNumeroEdicion("1era Edición");
        entidad.setFechaPublicacion(LocalDateTime.now());

        when(repo.findById(5)).thenReturn(Optional.of(entidad));

        // Act
        EdicionResponse res = service.getById(5);

        // Assert
        assertNotNull(res);
        assertEquals(5, res.getId());
    }

    @Test
    void getById_DeberiaLanzarExcepcion_SiNoExiste() {
        when(repo.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ExcepcionNoExiste.class, () -> service.getById(99));
    }

    @Test
    void listarPorRevista_DeberiaRetornarLista() {
        // Arrange
        EntidadEdicion e1 = new EntidadEdicion();
        e1.setId(1);
        e1.setRevistaId(1);
        e1.setNumeroEdicion("1era"); 
        e1.setFechaPublicacion(LocalDateTime.now());
        
        when(repo.findByRevistaIdOrderByFechaPublicacionDesc(1)).thenReturn(List.of(e1));

        // Act
        List<EdicionResponse> lista = service.listarPorRevista(1);

        // Assert
        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
    }

    @Test
    void eliminar_DeberiaLlamarAlRepositorio() {
        // Act
        service.eliminar(1);

        // Assert
        verify(repo, times(1)).deleteById(1);
    }
}