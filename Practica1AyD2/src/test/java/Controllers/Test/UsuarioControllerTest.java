package Controllers.Test;

import com.e.gomez.Practica1AyD2.controladores.UsuarioController;
import com.e.gomez.Practica1AyD2.dtoUsuarios.*;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.excepciones.GlobalExceptionHandler;
import com.e.gomez.Practica1AyD2.modelos.*;
import com.e.gomez.Practica1AyD2.servicios.*;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {UsuarioController.class, GlobalExceptionHandler.class})
@WithMockUser
public class UsuarioControllerTest extends CommonMvcTest {

    @MockBean private UsuarioService usuarioService;
    @MockBean private PerfilService perfilService;
    @MockBean private RolService rolService;
    @MockBean private CarteraService carteraService;

    private static final Integer USER_ID = 1;
    private EntidadUsuario entidadUsuario;
    private EntidadPerfil entidadPerfil;
    private EntidadRol entidadRol;
    private EntidadCartera entidadCartera;

    @BeforeEach
    void setUp() {
        entidadUsuario = new EntidadUsuario();
        entidadUsuario.setId(USER_ID);
        entidadUsuario.setUsername("eiler");

        entidadPerfil = new EntidadPerfil();
        entidadPerfil.setUsuarioId(USER_ID);

        entidadRol = new EntidadRol();
        entidadRol.setNombre("ADMIN");

        entidadCartera = new EntidadCartera();
        entidadCartera.setUsuarioId(USER_ID);
    }

    @Test
    public void testFindAllUsers() throws Exception {
        when(usuarioService.findAll()).thenReturn(Collections.singletonList(entidadUsuario));
        when(perfilService.findByUsuarioId(USER_ID)).thenReturn(entidadPerfil);
        when(rolService.traerRolDeUsuario(USER_ID)).thenReturn(entidadRol);
        when(carteraService.obtenerPorUsuario(USER_ID)).thenReturn(entidadCartera);

        mockMvc.perform(get("/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    List<UsuarioCompletoResponse> response = objectMapper.readValue(json, 
                            objectMapper.getTypeFactory().constructCollectionType(List.class, UsuarioCompletoResponse.class));
                    Assertions.assertThat(response).hasSize(1);
                });
    }

    @Test
    public void testCreatedUser() throws Exception {
        NuevoUsuarioRequest request = new NuevoUsuarioRequest();
        request.setUsername("nuevoUser");
        
        when(usuarioService.crearUsuario(any())).thenReturn(entidadUsuario);
        when(perfilService.findByUsuarioId(USER_ID)).thenReturn(entidadPerfil);
        when(rolService.traerRolDeUsuario(USER_ID)).thenReturn(entidadRol);
        when(carteraService.obtenerPorUsuario(USER_ID)).thenReturn(entidadCartera);

        mockMvc.perform(post("/v1/usuarios")
                .with(csrf()) 
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/v1/usuarios/{id}", USER_ID)
                .with(csrf())) 
                .andExpect(status().isNoContent());

        verify(usuarioService).eliminarUsuario(USER_ID);
    }

    @Test
    public void testGetByIdWhenUserNotExists() throws Exception {
        when(perfilService.findByUsuarioId(USER_ID)).thenThrow(new ExcepcionNoExiste("No existe"));

        mockMvc.perform(get("/v1/usuarios/{id}", USER_ID))
                .andExpect(status().isNotFound());
    }
}