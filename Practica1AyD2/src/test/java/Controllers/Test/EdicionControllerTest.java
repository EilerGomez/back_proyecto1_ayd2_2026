/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author eiler
 */
package Controllers.Test;

import com.e.gomez.Practica1AyD2.controladores.EdicionController;
import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionRequest;
import com.e.gomez.Practica1AyD2.servicios.EdicionService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {EdicionController.class})
@WithMockUser
public class EdicionControllerTest extends CommonMvcTest {

    @MockBean private EdicionService service;

    @Test
    void testCrearEdicion() throws Exception {
        EdicionRequest req = new EdicionRequest();
        mockMvc.perform(post("/v1/ediciones")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void testListarPorRevista() throws Exception {
        mockMvc.perform(get("/v1/ediciones/revista/{revistaId}", 1))
                .andExpect(status().isOk());
    }
}
