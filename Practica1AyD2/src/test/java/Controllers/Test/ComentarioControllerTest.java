/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */
//package Controller.Comentarios.Test;

import com.e.gomez.Practica1AyD2.controladores.ComentarioController;
import com.e.gomez.Practica1AyD2.dtoComentarios.ComentarioRequest;
import com.e.gomez.Practica1AyD2.dtoComentarios.ComentarioResponse;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.ComentarioService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {ComentarioController.class, GlobalExceptionHandler.class})
@WithMockUser
public class ComentarioControllerTest extends CommonMvcTest {

    @MockBean
    private ComentarioService comentarioService;

    @Test
    void testCrearComentario() throws Exception {
        ComentarioRequest req = new ComentarioRequest();
        req.setContenido("Excelente artículo");

        when(comentarioService.crear(any())).thenReturn(new ComentarioResponse());

        mockMvc.perform(post("/v1/comentarios")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizarComentarioConMapa() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("contenido", "Contenido actualizado");

        when(comentarioService.actualizar(eq(1), eq("Contenido actualizado")))
                .thenReturn(new ComentarioResponse());

        mockMvc.perform(put("/v1/comentarios/{id}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }
}
