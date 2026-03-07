/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers.Test;

/**
 *
 * @author eiler
 */

import com.e.gomez.Practica1AyD2.controladores.CategoriaController;
import com.e.gomez.Practica1AyD2.dtoCategorias.CategoriaRequest;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.modelos.EntidadCategoria;
import com.e.gomez.Practica1AyD2.servicios.CategoriaService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {CategoriaController.class, GlobalExceptionHandler.class})
@WithMockUser
public class CategoriaControllerTest extends CommonMvcTest {

    @MockBean
    private CategoriaService categoriaService;

    @Test
    void testListarCategorias() throws Exception {
        EntidadCategoria cat = new EntidadCategoria();
        cat.setId(1);
        cat.setNombre("Tecnología");

        when(categoriaService.findAll()).thenReturn(Collections.singletonList(cat));

        mockMvc.perform(get("/v1/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Tecnología"));
    }

    @Test
    void testGuardarCategoria() throws Exception {
        CategoriaRequest req = new CategoriaRequest();
        req.setNombre("Ciencia");

        EntidadCategoria cat = new EntidadCategoria();
        cat.setId(2);
        cat.setNombre("Ciencia");

        when(categoriaService.crear(any())).thenReturn(cat);

        mockMvc.perform(post("/v1/categorias")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    void testEliminarCategoria() throws Exception {
        mockMvc.perform(delete("/v1/categorias/{id}", 1)
                .with(csrf()))
                .andExpect(status().isNoContent());

        verify(categoriaService, times(1)).eliminar(1);
    }
}
