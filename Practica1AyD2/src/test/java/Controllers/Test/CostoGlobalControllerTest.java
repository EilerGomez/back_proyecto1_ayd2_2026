/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author eiler
 */
package Controllers.Test;

import com.e.gomez.Practica1AyD2.controladores.CostoGlobalController;
import com.e.gomez.Practica1AyD2.dtoRevistas.CostoGlobalRequest;
import com.e.gomez.Practica1AyD2.dtoRevistas.CostoGlobalResponse;
import com.e.gomez.Practica1AyD2.servicios.CostoGlobalService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {CostoGlobalController.class})
public class CostoGlobalControllerTest extends CommonMvcTest {

    @MockBean private CostoGlobalService service;

    @Test
    @WithMockUser(roles = "ADMIN")
    void testActualizarCostoGlobal() throws Exception {
        CostoGlobalRequest req = new CostoGlobalRequest();
        req.setMonto(BigDecimal.TEN);

        mockMvc.perform(put("/v1/costo-global")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}
