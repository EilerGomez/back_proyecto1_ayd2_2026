/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.PrecioAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.PrecioAnuncioService;
import Controllers.Test.CommonMvcTest;
import com.e.gomez.Practica1AyD2.controladores.PrecioAnuncioController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {PrecioAnuncioController.class, GlobalExceptionHandler.class})
@WithMockUser
public class PrecioAnuncioControllerTest extends CommonMvcTest {

    @MockBean
    private PrecioAnuncioService precioAnuncioService;

    private static final Integer PRECIO_ID = 1;
    private static final Integer TIPO_ANUNCIO_ID = 2;
    private PrecioAnuncioResponse precioResponse;

    @BeforeEach
    void setUp() {
        precioResponse = new PrecioAnuncioResponse();
        precioResponse.setId(PRECIO_ID);
        precioResponse.setPrecio(new BigDecimal("150.00"));
        precioResponse.setActivo(true);
    }

    @Test
    public void testCrearPrecioAnuncio() throws Exception {
        // Arrange
        PrecioAnuncioRequest request = new PrecioAnuncioRequest();
        request.setTipoAnuncioId(1);
        request.setPeriodoId(1);
        request.setPrecio(new BigDecimal("0.01"));
        request.setAdminId(1);
        
        when(precioAnuncioService.crear(any(PrecioAnuncioRequest.class))).thenReturn(precioResponse);

        // Act
        mockMvc.perform(post("/v1/anuncios/precios")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    PrecioAnuncioResponse resp = objectMapper.readValue(json, PrecioAnuncioResponse.class);
                    Assertions.assertThat(resp.getId()).isEqualTo(PRECIO_ID);
                    Assertions.assertThat(resp.getPrecio()).isEqualByComparingTo("150.00");
                });
    }

    @Test
    public void testListarTodos() throws Exception {
        when(precioAnuncioService.listarTodos()).thenReturn(Collections.singletonList(precioResponse));

        mockMvc.perform(get("/v1/anuncios/precios"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<PrecioAnuncioResponse> list = objectMapper.readValue(json, 
                            objectMapper.getTypeFactory().constructCollectionType(List.class, PrecioAnuncioResponse.class));
                    Assertions.assertThat(list).hasSize(1);
                });
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(precioAnuncioService.obtenerPorId(PRECIO_ID)).thenReturn(precioResponse);

        mockMvc.perform(get("/v1/anuncios/precios/{id}", PRECIO_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    PrecioAnuncioResponse resp = objectMapper.readValue(json, PrecioAnuncioResponse.class);
                    Assertions.assertThat(resp.getId()).isEqualTo(PRECIO_ID);
                });
    }

    @Test
    public void testDesactivarPrecio() throws Exception {
        // Método void en el servicio
        doNothing().when(precioAnuncioService).desactivar(PRECIO_ID);

        mockMvc.perform(patch("/v1/anuncios/precios/{id}/desactivar", PRECIO_ID)
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(precioAnuncioService, times(1)).desactivar(PRECIO_ID);
    }

    @Test
    public void testObtenerPorTipoAnuncio() throws Exception {
        when(precioAnuncioService.obtenerPorTipoAnuncio(TIPO_ANUNCIO_ID))
                .thenReturn(Collections.singletonList(precioResponse));

        mockMvc.perform(get("/v1/anuncios/precios/tipo/{tipoAnuncioId}", TIPO_ANUNCIO_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<PrecioAnuncioResponse> list = objectMapper.readValue(json, 
                            objectMapper.getTypeFactory().constructCollectionType(List.class, PrecioAnuncioResponse.class));
                    Assertions.assertThat(list).isNotEmpty();
                });
    }
}
