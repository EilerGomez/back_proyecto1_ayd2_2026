/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servicios.CostoGlobal.Test;

/**
 *
 * @author eiler
 */
//package Servicios.Configuracion.Test;

import com.e.gomez.Practica1AyD2.dtoRevistas.CostoGlobalRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.CostoGlobalResponse;
import com.e.gomez.Practica1AyD2.modelos.EntidadCostoGlobal;
import com.e.gomez.Practica1AyD2.repositorios.CostoGlobalRepositorio;
import com.e.gomez.Practica1AyD2.servicios.CostoGlobalServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CostoGlobalServiceImplTest {

    @Mock
    private CostoGlobalRepositorio repo;

    @InjectMocks
    private CostoGlobalServiceImpl service;


    @Test
    void obtenerCostoGlobal_CuandoExiste_DeberiaRetornarDatos() {
        // Arrange
        EntidadCostoGlobal entidad = new EntidadCostoGlobal();
        entidad.setId(1);
        entidad.setMonto(new BigDecimal("50.00"));
        when(repo.findGlobalConfig()).thenReturn(Optional.of(entidad));

        // Act
        CostoGlobalResponse response = service.obtenerCostoGlobal();

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("50.00"), response.getMonto());
        verify(repo).findGlobalConfig();
    }

    @Test
    void obtenerCostoGlobal_CuandoNoExiste_DeberiaRetornarCero() {
        // Arrange
        when(repo.findGlobalConfig()).thenReturn(Optional.empty());

        // Act
        CostoGlobalResponse response = service.obtenerCostoGlobal();

        // Assert
        assertNotNull(response);
        assertEquals(BigDecimal.ZERO, response.getMonto());
    }


    @Test
    void actualizarCostoGlobal_CuandoYaExiste_DeberiaModificarExistente() {
        // Arrange
        EntidadCostoGlobal existente = new EntidadCostoGlobal();
        existente.setId(1);
        existente.setMonto(new BigDecimal("10.00"));

        CostoGlobalRequest req = new CostoGlobalRequest();
        req.setMonto(new BigDecimal("25.00"));

        when(repo.findGlobalConfig()).thenReturn(Optional.of(existente));
        when(repo.save(any(EntidadCostoGlobal.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CostoGlobalResponse response = service.actualizarCostoGlobal(req);

        // Assert
        assertEquals(new BigDecimal("25.00"), response.getMonto());
        verify(repo).save(existente); 
    }

    @Test
    void actualizarCostoGlobal_CuandoNoExiste_DeberiaCrearNuevaInstancia() {
        // Arrange
        CostoGlobalRequest req = new CostoGlobalRequest();
        req.setMonto(new BigDecimal("100.00"));

        when(repo.findGlobalConfig()).thenReturn(Optional.empty());
        when(repo.save(any(EntidadCostoGlobal.class))).thenAnswer(i -> {
            EntidadCostoGlobal nueva = i.getArgument(0);
            nueva.setId(1); 
            return nueva;
        });

        // Act
        CostoGlobalResponse response = service.actualizarCostoGlobal(req);

        // Assert
        assertNotNull(response);
        assertEquals(new BigDecimal("100.00"), response.getMonto());
        verify(repo).save(any(EntidadCostoGlobal.class));
    }
}