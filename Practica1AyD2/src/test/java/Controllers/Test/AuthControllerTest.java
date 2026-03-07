/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */
//package Controller.Auth.Test;

import com.e.gomez.Practica1AyD2.controladores.AuthController;
import com.e.gomez.Practica1AyD2.dtoAuth.LoginRequest;
import com.e.gomez.Practica1AyD2.dtoAuth.LoginResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionCredencialesInvalidas;
import com.e.gomez.Practica1AyD2.mantenimiento.MantenimientoSistemaService;
import com.e.gomez.Practica1AyD2.servicios.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {AuthController.class})
@WithMockUser
public class AuthControllerTest extends CommonMvcTest {

    @MockBean
    private AuthService authService;

    @MockBean
    private MantenimientoSistemaService mantenimiento;

    @Test
    void testLoginExitoso() throws Exception {
        // Arrange
        LoginRequest req = new LoginRequest();
        req.setIdentificador("eiler");
        req.setPassword("password123");

        LoginResponse resp = new LoginResponse();
        resp.setToken("jwt-token-generado");

        when(authService.login(any(LoginRequest.class))).thenReturn(resp);

        // Act y Assert
        mockMvc.perform(post("/v1/auth/login")
                .with(csrf()) // Prevenir 403
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-generado"));

        verify(mantenimiento, times(1)).desactivarAnunciosExpirados();
        verify(mantenimiento, times(1)).desactivarBloqueosDeAnunciosExpirados();
        verify(mantenimiento, times(1)).desactivarRevistasVencidas();
    }

    @Test
    void testLoginCredencialesInvalidas() throws Exception {
        // Arrange
        when(authService.login(any())).thenThrow(new ExcepcionCredencialesInvalidas("Usuario o clave incorrecta"));

        LoginRequest req = new LoginRequest();
        req.setIdentificador("user");
        req.setPassword("wrong");

        // Act y Assert
        mockMvc.perform(post("/v1/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogoutExitoso() throws Exception {
        // Act y Assert
        mockMvc.perform(post("/v1/auth/logout")
                .with(csrf())
                .header("Authorization", "Bearer token-valido"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Logout exitoso"));

        verify(authService, times(1)).logout("token-valido");
    }

    @Test
    void testLogoutSinBearerToken() throws Exception {
        // Act y Assert
        mockMvc.perform(post("/v1/auth/logout")
                .with(csrf())
                .header("Authorization", "TokenInvalido"))
                .andExpect(status().isUnauthorized());
    }
}