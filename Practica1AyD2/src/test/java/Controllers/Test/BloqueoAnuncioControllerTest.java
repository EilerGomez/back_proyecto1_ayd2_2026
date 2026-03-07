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

import com.e.gomez.Practica1AyD2.controladores.BloqueoAnuncioController;
import com.e.gomez.Practica1AyD2.dtoAnuncios.BloqueoAnuncioRequest;
import com.e.gomez.Practica1AyD2.dtoAnuncios.BloqueoAnuncioResponse;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.BloqueoAnuncioService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {BloqueoAnuncioController.class, GlobalExceptionHandler.class})
@WithMockUser
public class BloqueoAnuncioControllerTest extends CommonMvcTest {

    @MockBean private BloqueoAnuncioService service;

    @Test
    void testContratarBloqueo() throws Exception {
        BloqueoAnuncioRequest req = new BloqueoAnuncioRequest();
        when(service.contratarBloqueo(any())).thenReturn(new BloqueoAnuncioResponse());

        mockMvc.perform(post("/v1/anuncios/bloqueos/contratar")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizarFechaFin() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        mockMvc.perform(patch("/v1/anuncios/bloqueos/{id}/fecha-fin", 1)
                .param("fechaFin", now.toString())
                .with(csrf()))
                .andExpect(status().isOk());
    }
}
