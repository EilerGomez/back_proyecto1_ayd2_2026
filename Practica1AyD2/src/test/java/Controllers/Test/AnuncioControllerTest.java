/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.controladores.AnuncioController;
import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.AnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.AnuncioService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {AnuncioController.class, GlobalExceptionHandler.class})
@WithMockUser
public class AnuncioControllerTest extends CommonMvcTest {

    @MockBean
    private AnuncioService anuncioService;

    private static final Integer ANUNCIO_ID = 1;
    private static final Integer ANUNCIANTE_ID = 10;
    private AnuncioResponse anuncioResponse;

    @BeforeEach
    void setUp() {
        anuncioResponse = new AnuncioResponse();
        anuncioResponse.setId(ANUNCIO_ID);
        anuncioResponse.setTexto("Anuncio de Prueba");
        anuncioResponse.setEstado("ACTIVO");
    }

    @Test
    public void testCrearAnuncio() throws Exception {
        // Arrange
        AnuncioRequest request = new AnuncioRequest();
        request.setTexto("Nuevo Anuncio");
        when(anuncioService.crear(any(AnuncioRequest.class))).thenReturn(anuncioResponse);

        // Act
        mockMvc.perform(post("/v1/anuncios")
                .with(csrf()) // Necesario para evitar 403
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                // Assert
                .andExpect(status().isCreated());
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(anuncioService.obtenerPorId(ANUNCIO_ID)).thenReturn(anuncioResponse);

        mockMvc.perform(get("/v1/anuncios/{id}", ANUNCIO_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    AnuncioResponse resp = objectMapper.readValue(json, AnuncioResponse.class);
                    Assertions.assertThat(resp.getId()).isEqualTo(ANUNCIO_ID);
                });
    }

    @Test
    public void testListarPorAnunciante() throws Exception {
        when(anuncioService.listarPorAnunciante(ANUNCIANTE_ID))
                .thenReturn(Collections.singletonList(anuncioResponse));

        mockMvc.perform(get("/v1/anuncios/anunciante/{anuncianteId}", ANUNCIANTE_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<AnuncioResponse> list = objectMapper.readValue(json, 
                            objectMapper.getTypeFactory().constructCollectionType(List.class, AnuncioResponse.class));
                    Assertions.assertThat(list).hasSize(1);
                });
    }

    @Test
    public void testCambiarEstado() throws Exception {
        // Act
        mockMvc.perform(patch("/v1/anuncios/{id}/estado", ANUNCIO_ID)
                .param("nuevoEstado", "VENCIDO") // Simulación del @RequestParam
                .with(csrf()))
                // Assert
                .andExpect(status().isNoContent());

        verify(anuncioService).cambiarEstado(eq(ANUNCIO_ID), eq("VENCIDO"));
    }

    @Test
    public void testObtenerParaRevista() throws Exception {
        Integer revistaId = 5;
        when(anuncioService.obtenerAnunciosParaRevista(revistaId))
                .thenReturn(Collections.singletonList(anuncioResponse));

        mockMvc.perform(get("/v1/anuncios/para-revista/{revistaId}", revistaId))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerPorIdNoExiste() throws Exception {
        // Arrange
        when(anuncioService.obtenerPorId(ANUNCIO_ID)).thenThrow(new ExcepcionNoExiste("No existe"));

        // Act y Assert
        mockMvc.perform(get("/v1/anuncios/{id}", ANUNCIO_ID))
                .andExpect(status().isNotFound());
    }
}
