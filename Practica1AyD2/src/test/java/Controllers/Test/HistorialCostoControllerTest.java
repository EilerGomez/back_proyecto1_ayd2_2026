/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author eiler
 */
package Controllers.Test;

import com.e.gomez.Practica1AyD2.controladores.HistorialCostoController;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.HistorialCostoRequest;
import com.e.gomez.Practica1AyD2.servicios.HistorialCostoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {HistorialCostoController.class})
@WithMockUser
public class HistorialCostoControllerTest extends CommonMvcTest {

    @MockBean private HistorialCostoService service;

    @Test
    void testAsignarNuevoCosto() throws Exception {
        HistorialCostoRequest req = new HistorialCostoRequest();
        mockMvc.perform(post("/v1/costos-revista")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void testObtenerCostoVigente() throws Exception {
        mockMvc.perform(get("/v1/costos-revista/vigente/{revistaId}", 1))
                .andExpect(status().isOk());
    }
}
