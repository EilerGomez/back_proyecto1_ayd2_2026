/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.dtoPagosyCostos.PagoRevistaRequest;
import com.e.gomez.Practica1AyD2.dtoPagosyCostos.PagoRevistaResponse;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.servicios.PagoRevistaService;
import Controllers.Test.CommonMvcTest;
import com.e.gomez.Practica1AyD2.controladores.PagoRevistaController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {PagoRevistaController.class, GlobalExceptionHandler.class})
@WithMockUser
public class PagoRevistaControllerTest extends CommonMvcTest {

    @MockBean
    private PagoRevistaService pagoRevistaService;

    private static final Integer REVISTA_ID = 1;
    private static final Integer EDITOR_ID = 5;
    private static final Integer CARTERA_ID = 100;

    @Test
    public void testProcesarPago() throws Exception {
        // Arrange
        PagoRevistaRequest request = new PagoRevistaRequest(
            REVISTA_ID, EDITOR_ID, new BigDecimal("50.00"), 
            LocalDate.now(), LocalDate.now().plusMonths(1)
        );

        PagoRevistaResponse response = mock(PagoRevistaResponse.class);
        when(response.getId()).thenReturn(1);
        
        when(pagoRevistaService.procesarPago(any(PagoRevistaRequest.class), eq(CARTERA_ID)))
                .thenReturn(response);

        // Act
        mockMvc.perform(post("/v1/pagos-revista/procesar")
                .param("carteraId", CARTERA_ID.toString())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                // Assert
                .andExpect(status().isCreated());
    }

    @Test
    public void testListarPorRevista() throws Exception {
        when(pagoRevistaService.listarPagosPorRevista(REVISTA_ID))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/v1/pagos-revista/revista/{revistaId}", REVISTA_ID))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<PagoRevistaResponse> list = objectMapper.readValue(json, 
                            objectMapper.getTypeFactory().constructCollectionType(List.class, PagoRevistaResponse.class));
                    Assertions.assertThat(list).isEmpty();
                });
    }

    @Test
    public void testActualizarFechaFin() throws Exception {
        LocalDate nuevaFecha = LocalDate.now().plusYears(1);
        PagoRevistaResponse response = mock(PagoRevistaResponse.class);
        
        when(pagoRevistaService.actualizarFechaFin(eq(1), any(LocalDate.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/v1/pagos-revista/{id}/fecha-fin", 1)
                .param("fechaFin", nuevaFecha.toString())
                .with(csrf()))
                .andExpect(status().isOk());
    }
}
