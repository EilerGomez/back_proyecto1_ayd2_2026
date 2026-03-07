/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */
//package Controller.Perfiles.Test;

import com.e.gomez.Practica1AyD2.controladores.PerfilController;
import com.e.gomez.Practica1AyD2.dtoUsuarios.NuevoPerfilRequest;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.modelos.EntidadPerfil;
import com.e.gomez.Practica1AyD2.servicios.PerfilService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {PerfilController.class, GlobalExceptionHandler.class})
@WithMockUser
public class PerfilControllerTest extends CommonMvcTest {

    @MockBean
    private PerfilService perfilService;

    @Test
    void testGetByUsuarioId() throws Exception {
        EntidadPerfil perfil = new EntidadPerfil();
        perfil.setUsuarioId(10);

        when(perfilService.findByUsuarioId(10)).thenReturn(perfil);

        mockMvc.perform(get("/v1/perfiles/usuario/{usuarioId}", 10))
                .andExpect(status().isOk());
    }

    @Test
    void testPatchPerfil() throws Exception {
        NuevoPerfilRequest req = new NuevoPerfilRequest();
        req.setDescripcion("Nueva bio");

        when(perfilService.updatePerfil(anyInt(), any())).thenReturn(new EntidadPerfil());

        mockMvc.perform(patch("/v1/perfiles/usuario/{usuarioId}", 10)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}
