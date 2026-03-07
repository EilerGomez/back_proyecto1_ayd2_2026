package Servicios.Revistas.Test;

import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionRequest;
import com.e.gomez.Practica1AyD2.dtoEdicion.EdicionResponse;
import com.e.gomez.Practica1AyD2.excepciones.ExcepcionNoExiste;
import com.e.gomez.Practica1AyD2.modelos.EntidadEdicion;
import com.e.gomez.Practica1AyD2.repositorios.EdicionRepositorio;
import com.e.gomez.Practica1AyD2.servicios.EdicionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EdicionServiceImplTest {

    @Mock
    private EdicionRepositorio repo;

    @InjectMocks
    private EdicionServiceImpl service;

    @Test
    void crearEdicion_DeberiaGenerarFormatoOrdinalCorrecto() {
        EdicionRequest req = new EdicionRequest();
        req.setRevistaId(1);
        req.setTitulo("Edición Verano");
        req.setPdfUrl("http://cdn.com/revista.pdf");

        when(repo.countByRevistaId(1)).thenReturn(2L); 
        when(repo.save(any(EntidadEdicion.class))).thenAnswer(i -> {
            EntidadEdicion ed = i.getArgument(0);
            ed.setId(500); // Seteamos ID para evitar NPE en el Response
            ed.setFechaPublicacion(LocalDateTime.now());
            return ed;
        });

        EdicionResponse res = service.crearEdicion(req);

        assertNotNull(res);
        assertEquals("3° Edición", res.getNumeroEdicion());
        verify(repo).save(any(EntidadEdicion.class));
    }

    @Test
    void getById_DeberiaRetornarEdicion_SiExiste() throws ExcepcionNoExiste {
        EntidadEdicion entidad = new EntidadEdicion();
        entidad.setId(5);
        entidad.setRevistaId(1); // Importante: setear para evitar NPE
        entidad.setTitulo("Test");
        entidad.setNumeroEdicion("1° Edición");
        entidad.setFechaPublicacion(LocalDateTime.now());

        when(repo.findById(5)).thenReturn(Optional.of(entidad));

        EdicionResponse res = service.getById(5);

        assertNotNull(res);
        assertEquals(5, res.getId());
    }

    @Test
    void getById_DeberiaLanzarExcepcion_SiNoExiste() {
        when(repo.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ExcepcionNoExiste.class, () -> service.getById(99));
    }

    @Test
    void listarPorRevista_DeberiaRetornarLista() {
        EntidadEdicion e1 = new EntidadEdicion();
        e1.setId(10);
        e1.setRevistaId(1);
        e1.setTitulo("Revista 1");
        e1.setNumeroEdicion("1°");
        e1.setFechaPublicacion(LocalDateTime.now());

        when(repo.findByRevistaIdOrderByFechaPublicacionDesc(1)).thenReturn(List.of(e1));

        List<EdicionResponse> lista = service.listarPorRevista(1);

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        assertEquals(10, lista.get(0).getId());
    }

    @Test
    void findAll_DeberiaMapearListaCompleta() {
        EntidadEdicion e1 = new EntidadEdicion();
        e1.setId(1);
        e1.setRevistaId(1);
        e1.setFechaPublicacion(LocalDateTime.now());

        when(repo.findAll()).thenReturn(List.of(e1));

        List<EdicionResponse> resultado = service.findAll();

        assertEquals(1, resultado.size());
        assertNotNull(resultado.get(0).getId());
        verify(repo).findAll();
    }

    @Test
    void eliminar_DeberiaLlamarDeleteById() {
        service.eliminar(10);
        verify(repo, times(1)).deleteById(10);
    }
}