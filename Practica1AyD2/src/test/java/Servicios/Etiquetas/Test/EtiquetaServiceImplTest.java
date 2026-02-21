/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Etiquetas.Test;

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
import org.mockito.Mockito;
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
    void findAll_deberiaRetornarListaDeResponses() {
        // Arrange
        List<EntidadEtiqueta> entidades = List.of(
                new EntidadEtiqueta(1, "Java"),
                new EntidadEtiqueta(2, "Spring")
        );
        when(repositorio.findAll()).thenReturn(entidades);

        // Act
        List<EtiquetaResponse> resultado = service.findAll();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Java", resultado.get(0).getNombre());
        assertEquals(1, resultado.get(0).getId());
        verify(repositorio, times(1)).findAll();
    }

    @Test
    void getById_deberiaRetornarResponse_siExiste() throws ExcepcionNoExiste {
        // Arrange
        EntidadEtiqueta entidad = new EntidadEtiqueta(10, "Docker");
        when(repositorio.findById(10)).thenReturn(Optional.of(entidad));

        // Act
        EtiquetaResponse resultado = service.getById(10);

        // Assert
        assertNotNull(resultado);
        assertEquals("Docker", resultado.getNombre());
        assertEquals(10, resultado.getId());
    }

    @Test
    void getById_lanzaExcepcion_siNoExiste() {
        when(repositorio.findById(1)).thenReturn(Optional.empty());
        assertThrows(ExcepcionNoExiste.class, () -> service.getById(1));
    }

    @Test
    void crear_deberiaGuardar_siNoExisteNombre() throws ExcepcionEntidadDuplicada {
        // Arrange
        EtiquetaRequest req = new EtiquetaRequest("NuevaTag");
        EntidadEtiqueta entidadGuardada = new EntidadEtiqueta(1, "NuevaTag");

        when(repositorio.existsByNombre("NuevaTag")).thenReturn(false);
        when(repositorio.save(any(EntidadEtiqueta.class))).thenReturn(entidadGuardada);

        // Act
        EtiquetaResponse resultado = service.crear(req);

        // Assert
        assertEquals("NuevaTag", resultado.getNombre());
        verify(repositorio, times(1)).save(any(EntidadEtiqueta.class));
    }

    @Test
    void crear_lanzaExcepcion_siNombreYaExiste() {
        // Arrange
        EtiquetaRequest req = new EtiquetaRequest("Existe");
        when(repositorio.existsByNombre("Existe")).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.crear(req));
        verify(repositorio, never()).save(any());
    }

    @Test
    void actualizar_deberiaModificar_siEsValido() throws Exception {
        // Arrange
        Integer id = 5;
        EntidadEtiqueta existente = new EntidadEtiqueta(id, "Antiguo");
        EtiquetaRequest req = new EtiquetaRequest("Actualizado");

        when(repositorio.findById(id)).thenReturn(Optional.of(existente));
        when(repositorio.existsByNombre("Actualizado")).thenReturn(false);
        when(repositorio.save(any(EntidadEtiqueta.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        EtiquetaResponse resultado = service.actualizar(id, req);

        // Assert
        assertEquals("Actualizado", resultado.getNombre());
        verify(repositorio).save(existente);
    }

    @Test
    void actualizar_lanzaExcepcion_siNombreNuevoYaExisteEnOtraEtiqueta() {
        // Arrange
        Integer id = 1;
        EntidadEtiqueta existente = new EntidadEtiqueta(id, "Tag1");
        EtiquetaRequest req = new EtiquetaRequest("Tag2"); // Tag2 ya lo tiene otra etiqueta

        when(repositorio.findById(id)).thenReturn(Optional.of(existente));
        when(repositorio.existsByNombre("Tag2")).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.actualizar(id, req));
    }

    @Test
    void eliminar_llamaAlRepositorio_siExiste() throws ExcepcionNoExiste {
        // Arrange
        Integer id = 1;
        when(repositorio.existsById(id)).thenReturn(true);

        // Act
        service.eliminar(id);

        // Assert
        verify(repositorio, times(1)).deleteById(id);
    }

    @Test
    void eliminar_lanzaExcepcion_siNoExiste() {
        // Arrange
        Integer id = 1;
        when(repositorio.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.eliminar(id));
        verify(repositorio, never()).deleteById(any());
    }
}
