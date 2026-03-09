/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscricpionResponseByRevistaId;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.SuscripcionRequest;
import com.e.gomez.Practica1AyD2.dtoSuscripciones.dtoRevistasPorSuscripcionByUsuarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.SuscripcionService;
import Controllers.Test.CommonMvcTest;
import com.e.gomez.Practica1AyD2.controladores.SuscripcionController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {SuscripcionController.class, GlobalExceptionHandler.class})
@WithMockUser
public class SuscripcionControllerTest extends CommonMvcTest {

    @MockBean
    private SuscripcionService suscripcionService;

    private static final Integer USUARIO_ID = 1;
    private static final Integer REVISTA_ID = 5;
    private static final Integer SUSCRIPCION_ID = 10;

    @Test
    public void testSuscribir() throws Exception {
        // Arrange
        SuscripcionRequest request = new SuscripcionRequest();
        request.setUsuarioId(USUARIO_ID);
        request.setRevistaId(REVISTA_ID);

        SuscricpionResponseByRevistaId response = new SuscricpionResponseByRevistaId();
        response.setId(SUSCRIPCION_ID);
        response.setRevistaId(REVISTA_ID);

        when(suscripcionService.suscribir(any(SuscripcionRequest.class))).thenReturn(response);

        // Act
        mockMvc.perform(post("/v1/suscripciones")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    SuscricpionResponseByRevistaId resp = objectMapper.readValue(json, SuscricpionResponseByRevistaId.class);
                    Assertions.assertThat(resp.getId()).isEqualTo(SUSCRIPCION_ID);
                });
    }

    @Test
    public void testListarPorUsuario() throws Exception {
        // Arrange
        when(suscripcionService.listarPorUsuario(USUARIO_ID))
                .thenReturn(Collections.emptyList());

        // Act
        mockMvc.perform(get("/v1/suscripciones/usuario/{usuarioId}", USUARIO_ID))
                // Assert
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<dtoRevistasPorSuscripcionByUsuarioResponse> list = objectMapper.readValue(json,
                            objectMapper.getTypeFactory().constructCollectionType(List.class, dtoRevistasPorSuscripcionByUsuarioResponse.class));
                    Assertions.assertThat(list).isEmpty();
                });
    }

    @Test
    public void testListarPorRevista() throws Exception {
        // Arrange
        SuscricpionResponseByRevistaId item = new SuscricpionResponseByRevistaId();
        item.setId(SUSCRIPCION_ID);
        
        when(suscripcionService.listarPorRevista(REVISTA_ID))
                .thenReturn(Collections.singletonList(item));

        // Act
        mockMvc.perform(get("/v1/suscripciones/revista/{revistaId}", REVISTA_ID))
                // Assert
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<SuscricpionResponseByRevistaId> list = objectMapper.readValue(json,
                            objectMapper.getTypeFactory().constructCollectionType(List.class, SuscricpionResponseByRevistaId.class));
                    Assertions.assertThat(list).hasSize(1);
                    Assertions.assertThat(list.get(0).getId()).isEqualTo(SUSCRIPCION_ID);
                });
    }

    @Test
    public void testCambiarEstado() throws Exception {
        // Arrange - método void
        doNothing().when(suscripcionService).cambiarEstado(eq(SUSCRIPCION_ID), anyBoolean());

        // Act
        mockMvc.perform(patch("/v1/suscripciones/{id}/estado", SUSCRIPCION_ID)
                .param("activa", "true")
                .with(csrf()))
                // Assert
                .andExpect(status().isNoContent());

        verify(suscripcionService).cambiarEstado(SUSCRIPCION_ID, true);
    }

    @Test
    public void testCancelarSuscripcion() throws Exception {
        // Arrange - método void
        doNothing().when(suscripcionService).cancelarSuscripcion(SUSCRIPCION_ID);

        // Act
        mockMvc.perform(delete("/v1/suscripciones/{id}", SUSCRIPCION_ID)
                .with(csrf()))
                // Assert
                .andExpect(status().isNoContent());

        verify(suscripcionService).cancelarSuscripcion(SUSCRIPCION_ID);
    }
}
