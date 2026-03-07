/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.controladores.RevistaController;
import com.e.gomez.Practica1AyD2.dtoRevistas.*;
import com.e.gomez.Practica1AyD2.dtoEtiquetas.RevistaEtiquetasRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.RevistaService;
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

@ContextConfiguration(classes = {RevistaController.class, GlobalExceptionHandler.class})
@WithMockUser(roles = {"ADMIN", "EDITOR"}) // Simula usuario con roles para @PreAuthorize
public class RevistaControllerTest extends CommonMvcTest {

    @MockBean
    private RevistaService revistaService;

    private static final Integer REVISTA_ID = 1;
    private RevistaResponse revistaResponse;

    @BeforeEach
    void setUp() {
        revistaResponse = new RevistaResponse();
        revistaResponse.setId(REVISTA_ID);
        revistaResponse.setTitulo("Revista de Prueba");
    }

    @Test
    public void testListarTodas() throws Exception {
        when(revistaService.findAll()).thenReturn(Collections.singletonList(revistaResponse));

        mockMvc.perform(get("/v1/revistas"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<RevistaResponse> response = objectMapper.readValue(json, 
                            objectMapper.getTypeFactory().constructCollectionType(List.class, RevistaResponse.class));
                    Assertions.assertThat(response).hasSize(1);
                    Assertions.assertThat(response.get(0).getTitulo()).isEqualTo("Revista de Prueba");
                });
    }

    @Test
    public void testObtenerPorId() throws Exception {
        when(revistaService.getById(REVISTA_ID)).thenReturn(revistaResponse);

        mockMvc.perform(get("/v1/revistas/{id}", REVISTA_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    RevistaResponse resp = objectMapper.readValue(json, RevistaResponse.class);
                    Assertions.assertThat(resp.getId()).isEqualTo(REVISTA_ID);
                });
    }

    @Test
    public void testCrearRevista() throws Exception {
        RevistaRequest request = new RevistaRequest();
        request.setTitulo("Nueva Revista");

        when(revistaService.crear(any(RevistaRequest.class))).thenReturn(revistaResponse);

        mockMvc.perform(post("/v1/revistas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testEliminarRevista() throws Exception {
        mockMvc.perform(delete("/v1/revistas/{id}", REVISTA_ID)
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(revistaService).eliminar(REVISTA_ID);
    }

    @Test
    public void testAsignarEtiquetas() throws Exception {
        RevistaEtiquetasRequest request = new RevistaEtiquetasRequest();
        request.setIdRevista(REVISTA_ID);
        request.setEtiquetasIds(List.of(1, 2, 3));

        mockMvc.perform(post("/v1/revistas/asignar-etiquetas")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(revistaService).guardarEtiquetas(any(RevistaEtiquetasRequest.class));
    }

    @Test
    public void testCambiarEstado() throws Exception {
        when(revistaService.cambiarEstado(eq(REVISTA_ID), eq(true))).thenReturn(revistaResponse);

        mockMvc.perform(patch("/v1/revistas/{id}/estado", REVISTA_ID)
                .param("estado", "true")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testObtenerPorIdNoExiste() throws Exception {
        when(revistaService.getById(REVISTA_ID)).thenThrow(new ExcepcionNoExiste("Revista no encontrada"));

        mockMvc.perform(get("/v1/revistas/{id}", REVISTA_ID))
                .andExpect(status().isNotFound());
    }
}
