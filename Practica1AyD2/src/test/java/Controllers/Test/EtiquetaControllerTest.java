/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaRequest;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.EtiquetaResponse;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.EtiquetaService;
import Controllers.Test.CommonMvcTest;
import com.e.gomez.Practica1AyD2.controladores.EtiquetaController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {EtiquetaController.class, GlobalExceptionHandler.class})
@WithMockUser
public class EtiquetaControllerTest extends CommonMvcTest {

    @MockBean
    private EtiquetaService etiquetaService;

    private static final Integer ETIQUETA_ID = 1;
    private static final String ETIQUETA_NOMBRE = "Tecnología";

    @Test
    public void testListarEtiquetas() throws Exception {
        // Arrange
        EtiquetaResponse response = mock(EtiquetaResponse.class);
        when(etiquetaService.findAll()).thenReturn(Collections.singletonList(response));

        // Act
        mockMvc.perform(get("/v1/etiquetas"))
                // Assert
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<EtiquetaResponse> list = objectMapper.readValue(json, 
                            objectMapper.getTypeFactory().constructCollectionType(List.class, EtiquetaResponse.class));
                    Assertions.assertThat(list).hasSize(1);
                });
    }

    @Test
    public void testObtenerEtiqueta() throws Exception {
        // Arrange
        EtiquetaResponse response = mock(EtiquetaResponse.class);
        when(response.getId()).thenReturn(ETIQUETA_ID);
        when(etiquetaService.getById(ETIQUETA_ID)).thenReturn(response);

        // Act
        mockMvc.perform(get("/v1/etiquetas/{id}", ETIQUETA_ID))
                // Assert
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    EtiquetaResponse resp = objectMapper.readValue(json, EtiquetaResponse.class);
                    Assertions.assertThat(resp.getId()).isEqualTo(ETIQUETA_ID);
                });
    }

    @Test
    public void testGuardarEtiqueta() throws Exception {
        // Arrange
        EtiquetaRequest request = new EtiquetaRequest();
        request.setNombre(ETIQUETA_NOMBRE);

        EtiquetaResponse response = mock(EtiquetaResponse.class);
        when(response.getNombre()).thenReturn(ETIQUETA_NOMBRE);
        when(etiquetaService.crear(any(EtiquetaRequest.class))).thenReturn(response);

        // Act
        mockMvc.perform(post("/v1/etiquetas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    EtiquetaResponse resp = objectMapper.readValue(json, EtiquetaResponse.class);
                    Assertions.assertThat(resp.getNombre()).isEqualTo(ETIQUETA_NOMBRE);
                });
    }

    @Test
    public void testEditarEtiqueta() throws Exception {
        // Arrange
        EtiquetaRequest request = new EtiquetaRequest();
        request.setNombre("Nuevo Nombre");

        EtiquetaResponse response = mock(EtiquetaResponse.class);
        when(etiquetaService.actualizar(eq(ETIQUETA_ID), any(EtiquetaRequest.class))).thenReturn(response);

        // Act
        mockMvc.perform(put("/v1/etiquetas/{id}", ETIQUETA_ID)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                // Assert
                .andExpect(status().isOk());
    }

    @Test
    public void testEliminarEtiqueta() throws Exception {
        // Arrange - método void
        doNothing().when(etiquetaService).eliminar(ETIQUETA_ID);

        // Act
        mockMvc.perform(delete("/v1/etiquetas/{id}", ETIQUETA_ID)
                .with(csrf()))
                // Assert
                .andExpect(status().isNoContent());

        verify(etiquetaService, times(1)).eliminar(ETIQUETA_ID);
    }
}
