/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.Categorias.Test;

import com.e.gomez.Practica1AyD2.dtoCategorias.CategoriaRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionEntidadDuplicada;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadCategoria;
import com.e.gomez.Practica1AyD2.repositorios.CategoriaRepositorio;
import com.e.gomez.Practica1AyD2.servicios.CategoriaServiceImpl;
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
public class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepositorio repositorio;

    @InjectMocks
    private CategoriaServiceImpl service;

    @Test
    void findAll_deberiaRetornarLista() {
        // Arrange
        EntidadCategoria c1 = new EntidadCategoria(1, "Deportes", "Desc");
        EntidadCategoria c2 = new EntidadCategoria(2, "Moda", "Desc");
        when(repositorio.findAll()).thenReturn(List.of(c1, c2));

        // Act
        List<EntidadCategoria> resultado = service.findAll();

        // Assert
        assertEquals(2, resultado.size());
        verify(repositorio, times(1)).findAll();
    }

    @Test
    void getById_deberiaRetornarCategoria_siExiste() throws ExcepcionNoExiste {
        // Arrange
        EntidadCategoria cat = new EntidadCategoria(1, "Deportes", "Desc");
        when(repositorio.findById(1)).thenReturn(Optional.of(cat));

        // Act
        EntidadCategoria resultado = service.getById(1);

        // Assert
        assertNotNull(resultado);
        assertEquals("Deportes", resultado.getNombre());
    }

    @Test
    void getById_deberiaLanzarExcepcion_siNoExiste() {
        // Arrange
        when(repositorio.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionNoExiste.class, () -> service.getById(1));
    }

    @Test
    void crear_deberiaGuardar_siNombreNoExiste() throws ExcepcionEntidadDuplicada {
        // Arrange
        CategoriaRequest req = new CategoriaRequest();
        req.setNombre("Nueva");
        req.setDescripcion("Desc");

        when(repositorio.existsByNombre("Nueva")).thenReturn(false);
        when(repositorio.save(any(EntidadCategoria.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        EntidadCategoria resultado = service.crear(req);

        // Assert
        assertNotNull(resultado);
        assertEquals("Nueva", resultado.getNombre());
        verify(repositorio).save(any(EntidadCategoria.class));
    }

    @Test
    void crear_deberiaLanzarExcepcion_siNombreYaExiste() {
        // Arrange
        CategoriaRequest req = new CategoriaRequest();
        req.setNombre("Existe");
        when(repositorio.existsByNombre("Existe")).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.crear(req));
        verify(repositorio, never()).save(any());
    }

    @Test
    void actualizar_deberiaModificar_siEsValido() throws Exception {
        // Arrange
        Integer id = 1;
        EntidadCategoria existente = new EntidadCategoria(id, "Viejo", "Viejo Desc");
        CategoriaRequest req = new CategoriaRequest();
        req.setNombre("Nuevo");
        req.setDescripcion("Nuevo Desc");

        when(repositorio.findById(id)).thenReturn(Optional.of(existente));
        when(repositorio.existsByNombre("Nuevo")).thenReturn(false);
        when(repositorio.save(any(EntidadCategoria.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        EntidadCategoria resultado = service.actualizar(id, req);

        // Assert
        assertEquals("Nuevo", resultado.getNombre());
        assertEquals("Nuevo Desc", resultado.getDescripcion());
        verify(repositorio).save(existente);
    }

    @Test
    void actualizar_deberiaLanzarExcepcion_siNombreNuevoYaExisteEnOtraCategoria() {
        // Arrange
        Integer id = 1;
        EntidadCategoria existente = new EntidadCategoria(id, "MiNombre", "Desc");
        CategoriaRequest req = new CategoriaRequest();
        req.setNombre("NombreOcupado");

        when(repositorio.findById(id)).thenReturn(Optional.of(existente));
        when(repositorio.existsByNombre("NombreOcupado")).thenReturn(true);

        // Act & Assert
        assertThrows(ExcepcionEntidadDuplicada.class, () -> service.actualizar(id, req));
    }

    @Test
    void eliminar_deberiaLlamarDelete_siExiste() throws ExcepcionNoExiste {
        // Arrange
        Integer id = 1;
        EntidadCategoria cat = new EntidadCategoria(id, "Borrar", "Desc");
        when(repositorio.findById(id)).thenReturn(Optional.of(cat));

        // Act
        service.eliminar(id);

        // Assert
        verify(repositorio, times(1)).delete(cat);
    }
}