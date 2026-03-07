/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */
//package Controllers.Test;

import com.e.gomez.Practica1AyD2.controladores.CompraAnuncioController;
import com.e.gomez.Practica1AyD2.dtoAnuncios.CompraAnuncioRequest;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.CompraAnuncioService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {CompraAnuncioController.class, GlobalExceptionHandler.class})
@WithMockUser
public class CompraAnuncioControllerTest extends CommonMvcTest {

    @MockBean private CompraAnuncioService service;

    @Test
    void testRealizarCompra() throws Exception {
        CompraAnuncioRequest req = new CompraAnuncioRequest();
        mockMvc.perform(post("/v1/anuncios/compras")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDesactivarCompra() throws Exception {
        mockMvc.perform(patch("/v1/anuncios/compras/{id}/desactivar", 1)
                .param("responsable", "ADMIN")
                .param("fecha", LocalDateTime.now().toString())
                .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
