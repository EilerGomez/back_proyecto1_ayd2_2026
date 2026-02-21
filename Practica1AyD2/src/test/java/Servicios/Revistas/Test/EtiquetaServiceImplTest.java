/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Revistas.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaRequest;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadEtiqueta;
import com.e.gomez.Practica1AyD2.repositorios.EtiquetaRepositorio;
import com.e.gomez.Practica1AyD2.servicios.EtiquetaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EtiquetaServiceImplTest {

    @Mock
    private EtiquetaRepositorio repositorio;

    @InjectMocks
    private EtiquetaServiceImpl service;

    @Test
    void findAll_DeberiaRetornarListaDeResponses() {
        // Arrange
        EntidadEtiqueta e1 = new EntidadEtiqueta(1, "Deportes");
        EntidadEtiqueta e2 = new EntidadEtiqueta(2, "Tecnología");
        when(repositorio.findAll()).thenReturn(List.of(e1, e2));

        // Act
        List<EtiquetaResponse> resultado = service.findAll();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Deportes", resultado.get(0).getNombre());
        verify(repositorio, times(1)).findAll();
    }

    @Test
    void getById_DeberiaRetornarResponse_SiExiste() throws ExcepcionNoExiste {
        // Arrange
        EntidadEtiqueta etiqueta = new EntidadEtiqueta(10, "Cocina");
        when(repositorio.findById(10)).thenReturn(Optional.of(etiqueta));

        // Act
        EtiquetaResponse resultado = service.getById(10);

        // Assert
        assertNotNull(resultado);
        assertEquals(10, resultado.getId());
        assertEquals("Cocina", resultado.getNombre());
    }

    @Test
    void crear_DeberiaGuardar_SiNombreNoExiste() throws ExcepcionEntidadDuplicada {
        // Arrange
        EtiquetaRequest request = new EtiquetaRequest("NuevaTag");
        EntidadEtiqueta entidadGuardada = new EntidadEtiqueta(1, "NuevaTag");

        when(repositorio.existsByNombre("NuevaTag")).thenReturn(false);
        when(repositorio.save(any(EntidadEtiqueta.class))).thenReturn(entidadGuardada);

        // Act
        EtiquetaResponse resultado = service.crear(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("NuevaTag", resultado.getNombre());
        verify(repositorio).save(any(EntidadEtiqueta.class));
    }

    @Test
    void actualizar_DeberiaModificar_SiEsValido() throws Exception {
        // Arrange
        Integer id = 5;
        EntidadEtiqueta existente = new EntidadEtiqueta(id, "NombreViejo");
        EtiquetaRequest request = new EtiquetaRequest("NombreNuevo");

        when(repositorio.findById(id)).thenReturn(Optional.of(existente));
        when(repositorio.existsByNombre("NombreNuevo")).thenReturn(false);
        
        when(repositorio.save(any(EntidadEtiqueta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        EtiquetaResponse resultado = service.actualizar(id, request);

        // Assert
        assertEquals("NombreNuevo", resultado.getNombre());
        assertEquals(5, resultado.getId());
        verify(repositorio).save(existente);
    }

    @Test
    void actualizar_DeberiaLanzarExcepcion_SiNombreYaEstaEnUso() {
        // Arrange
        Integer id = 1;
        EntidadEtiqueta existente = new EntidadEtiqueta(id, "Tag1");
        EtiquetaRequest request = new EtiquetaRequest("Tag2");

        when(repositorio.findById(id)).thenReturn(Optional.of(existente));
        when(repositorio.existsByNombre("Tag2")).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.actualizar(id, request));
    }

    @Test
    void eliminar_DeberiaLlamarDelete_SiExiste() throws ExcepcionNoExiste {
        // Arrange
        when(repositorio.existsById(1)).thenReturn(true);

        // Act
        service.eliminar(1);

        // Assert
        verify(repositorio).deleteById(1);
    }

    @Test
    void eliminar_DeberiaLanzarExcepcion_SiNoExiste() {
        // Arrange
        when(repositorio.existsById(1)).thenReturn(false);

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.eliminar(1));
    }
}
