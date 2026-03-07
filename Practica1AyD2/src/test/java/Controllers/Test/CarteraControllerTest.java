/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.controladores.CarteraController;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.modelos.EntidadCartera;
import com.e.gomez.Practica1AyD2.servicios.CarteraService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {CarteraController.class, GlobalExceptionHandler.class})
@WithMockUser
public class CarteraControllerTest extends CommonMvcTest {

    @MockBean
    private CarteraService carteraService;

    private static final Integer USUARIO_ID = 5;
    private static final Integer CARTERA_ID = 10;
    private EntidadCartera entidadCartera;

    @BeforeEach
    void setUp() {
        entidadCartera = new EntidadCartera();
        entidadCartera.setId(CARTERA_ID);
        entidadCartera.setUsuarioId(USUARIO_ID);
        entidadCartera.setSaldo(new BigDecimal("150.00"));
    }

    @Test
    public void testObtenerPorUsuario() throws Exception {
        // Arrange
        when(carteraService.obtenerPorUsuario(USUARIO_ID)).thenReturn(entidadCartera);

        // Act
        mockMvc.perform(get("/v1/carteras/usuario/{usuarioId}", USUARIO_ID))
                // Assert
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    EntidadCartera response = objectMapper.readValue(json, EntidadCartera.class);
                    Assertions.assertThat(response.getUsuarioId()).isEqualTo(USUARIO_ID);
                    Assertions.assertThat(response.getSaldo()).isEqualByComparingTo("150.00");
                });
    }

    @Test
    public void testRecargarSaldo() throws Exception {
        // Arrange
        Map<String, Object> payload = new HashMap<>();
        payload.put("usuarioId", USUARIO_ID);
        payload.put("monto", 100.50);

        when(carteraService.obtenerPorUsuario(USUARIO_ID)).thenReturn(entidadCartera);

        // Act
        mockMvc.perform(post("/v1/carteras/recargar")
                .with(csrf()) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(payload)))
                // Assert
                .andExpect(status().isOk());

        verify(carteraService).sumarSaldo(eq(CARTERA_ID), any(BigDecimal.class));
    }

    @Test
    public void testObtenerPorUsuarioNoExiste() throws Exception {
        // Arrange
        when(carteraService.obtenerPorUsuario(USUARIO_ID)).thenThrow(new ExcepcionNoExiste("No existe"));

        // Act & Assert
        mockMvc.perform(get("/v1/carteras/usuario/{usuarioId}", USUARIO_ID))
                .andExpect(status().isNotFound());
    }
}
