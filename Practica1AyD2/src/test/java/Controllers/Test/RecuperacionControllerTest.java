/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.RecuperacionService;
import Controllers.Test.CommonMvcTest; 
import com.e.gomez.Practica1AyD2.controladores.RecuperacionController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {RecuperacionController.class, GlobalExceptionHandler.class})
@WithMockUser // Simula un usuario autenticado para saltar filtros de seguridad básicos
public class RecuperacionControllerTest extends CommonMvcTest {

    @MockBean
    private RecuperacionService recuperacionService;

    private static final String CORREO_TEST = "eiler@ejemplo.com";
    private static final String CODIGO_TEST = "123456";

    @Test
    public void testSolicitarRecuperacion() throws Exception {
        // Arrange
        doNothing().when(recuperacionService).solicitarRecuperacion(CORREO_TEST);

        // Act
        mockMvc.perform(post("/v1/auth/recuperacion/solicitar")
                .param("identificador", CORREO_TEST)
                .with(csrf())) // Incluye el token CSRF necesario en métodos POST/PATCH/DELETE
                // Assert
                .andExpect(status().isOk());
    }

    @Test
    public void testValidarCodigo() throws Exception {
        // Arrange
        Map<String, String> payload = new HashMap<>();
        payload.put("correo", CORREO_TEST);
        payload.put("codigo", CODIGO_TEST);

        when(recuperacionService.validarCodigo(CORREO_TEST, CODIGO_TEST)).thenReturn(true);

        // Act
        mockMvc.perform(post("/v1/auth/recuperacion/validar")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                // Assert
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    Assertions.assertThat(responseBody).isEqualTo("true");
                });
    }

    @Test
    public void testCambiarContrasenia() throws Exception {
        // Arrange
        Map<String, String> payload = new HashMap<>();
        payload.put("correo", CORREO_TEST);
        payload.put("codigo", CODIGO_TEST);
        payload.put("nuevaPassword", "NuevaClave123");

        doNothing().when(recuperacionService).cambiarContrasenia(anyString(), anyString(), anyString());

        // Act
        mockMvc.perform(patch("/v1/auth/recuperacion/cambiar")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                // Assert
                .andExpect(status().isOk());
    }

    @Test
    public void testSolicitarUsuarioNoExiste() throws Exception {
        // Arrange
        // Arrange
        String correoInvalido = "noexiste@test.com";

        doThrow(new ExcepcionNoExiste("El usuario no existe"))
            .when(recuperacionService).solicitarRecuperacion(correoInvalido);

        // Act & Assert
        mockMvc.perform(post("/v1/auth/recuperacion/solicitar")
                .param("identificador", correoInvalido)
                .with(csrf()))
                .andExpect(status().isNotFound()); 
    }
}
