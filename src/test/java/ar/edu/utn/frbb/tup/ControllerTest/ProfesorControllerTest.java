package ar.edu.utn.frbb.tup.ControllerTest;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.controller.ProfesorController;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProfesorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProfesorService profesorService;

    @InjectMocks
    private ProfesorController profesorController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(profesorController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCrearProfesor() throws Exception {
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Carlos");
        dto.setApellido("González");
        dto.setTitulo("Ingeniero");

        Profesor profesor = new Profesor(1L, "Carlos", "González", "Ingeniero");

        when(profesorService.crearProfesor(any())).thenReturn(profesor);

        mockMvc.perform(post("/profesor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }

    @Test
    void testGetProfesorExistente() throws Exception {
        Profesor profesor = new Profesor(1L, "Ana", "Pérez", "Licenciada");

        when(profesorService.getProfesorById(1L)).thenReturn(profesor);

        mockMvc.perform(get("/profesor/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.apellido").value("Pérez"));
    }

    @Test
    void testGetProfesorInexistente() throws Exception {
        when(profesorService.getProfesorById(999L)).thenThrow(new ProfesorNotFoundException("No existe", 999L));

        mockMvc.perform(get("/profesor/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllProfesoresConContenido() throws Exception {
        List<Profesor> profesores = List.of(
                new Profesor(1L, "Laura", "Díaz", "Doctora"),
                new Profesor(2L, "Pablo", "Rojas", "Magíster")
        );

        when(profesorService.getAllProfesores()).thenReturn(profesores);

        mockMvc.perform(get("/profesor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetAllProfesoresSinContenido() throws Exception {
        when(profesorService.getAllProfesores()).thenReturn(List.of());

        mockMvc.perform(get("/profesor"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testActualizarProfesorExistente() throws Exception {
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("María");
        dto.setApellido("López");
        dto.setTitulo("Arquitecta");

        Profesor actualizado = new Profesor(1L, "María", "López", "Arquitecta");

        when(profesorService.actualizarProfesor(eq(1L), any())).thenReturn(actualizado);

        mockMvc.perform(put("/profesor/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Arquitecta"));
    }

    @Test
    void testActualizarProfesorInexistente() throws Exception {
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Mario");

        when(profesorService.actualizarProfesor(eq(404L), any()))
                .thenThrow(new ProfesorNotFoundException("No se encontró", 404L));

        mockMvc.perform(put("/profesor/404")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarProfesorExistente() throws Exception {
        doNothing().when(profesorService).eliminarProfesor(1L);

        mockMvc.perform(delete("/profesor/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarProfesorInexistente() throws Exception {
        doThrow(new ProfesorNotFoundException("No existe", 999L)).when(profesorService).eliminarProfesor(999L);

        mockMvc.perform(delete("/profesor/999"))
                .andExpect(status().isNotFound());
    }
}
