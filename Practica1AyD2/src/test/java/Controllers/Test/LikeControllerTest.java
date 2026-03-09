/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoLikes.LikeRequest;
import com.e.gomez.Practica1AyD2.dtoLikes.LikeResponse;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.LikeService;
import Controllers.Test.CommonMvcTest;
import com.e.gomez.Practica1AyD2.controladores.LikeController;
import com.e.gomez.Practica1AyD2.dtoUsuarios.UsuarioResponse;
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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {LikeController.class, GlobalExceptionHandler.class})
@WithMockUser
public class LikeControllerTest extends CommonMvcTest {

    @MockBean
    private LikeService likeService;

    private static final Integer REVISTA_ID = 1;
    private static final Integer USUARIO_ID = 100;
    private LikeResponse likeResponse;
    private  UsuarioResponse ur;

    @BeforeEach
    void setUp() {
        likeResponse = new LikeResponse();
        likeResponse.setRevistaId(REVISTA_ID);
        ur = new UsuarioResponse(USUARIO_ID, "nombre", "eiler123", "apellido", "falso@test.com", "ACTIVO", "https//perfil.com");
        likeResponse.setUsuario(ur);
    }

    @Test
    public void testDarLike() throws Exception {
        // Arrange
        LikeRequest request = new LikeRequest();
        request.setRevistaId(REVISTA_ID);
        request.setUsuarioId(USUARIO_ID);

        when(likeService.darLike(any(LikeRequest.class))).thenReturn(likeResponse);

        // Act
        mockMvc.perform(post("/v1/likes")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                // Assert
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    LikeResponse resp = objectMapper.readValue(json, LikeResponse.class);
                    Assertions.assertThat(resp.getRevistaId()).isEqualTo(REVISTA_ID);
                });
    }

    @Test
    public void testQuitarLike() throws Exception {
        // Al ser un método VOID, usamos doNothing() o simplemente verify después
        doNothing().when(likeService).quitarLike(REVISTA_ID, USUARIO_ID);

        mockMvc.perform(delete("/v1/likes/revista/{revistaId}/usuario/{usuarioId}", REVISTA_ID, USUARIO_ID)
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(likeService).quitarLike(REVISTA_ID, USUARIO_ID);
    }

    @Test
    public void testListarPorRevista() throws Exception {
        when(likeService.findByRevistaId(REVISTA_ID))
                .thenReturn(Collections.singletonList(likeResponse));

        mockMvc.perform(get("/v1/likes/revista/{revistaId}", REVISTA_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<LikeResponse> list = objectMapper.readValue(json, 
                            objectMapper.getTypeFactory().constructCollectionType(List.class, LikeResponse.class));
                    Assertions.assertThat(list).hasSize(1);
                });
    }

    @Test
    public void testContarLikes() throws Exception {
        when(likeService.contarLikesRevista(REVISTA_ID)).thenReturn(5);

        mockMvc.perform(get("/v1/likes/revista/{revistaId}/conteo", REVISTA_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String resp = result.getResponse().getContentAsString();
                    Assertions.assertThat(Integer.valueOf(resp)).isEqualTo(5);
                });
    }

    @Test
    public void testVerificarLike() throws Exception {
        when(likeService.yaDioLike(REVISTA_ID, USUARIO_ID)).thenReturn(true);

        mockMvc.perform(get("/v1/likes/revista/{revistaId}/usuario/{usuarioId}/existe", REVISTA_ID, USUARIO_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String resp = result.getResponse().getContentAsString();
                    Assertions.assertThat(resp).isEqualTo("true");
                });
    }
}