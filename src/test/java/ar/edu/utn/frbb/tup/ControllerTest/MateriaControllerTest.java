package ar.edu.utn.frbb.tup.ControllerTest;

import ar.edu.utn.frbb.tup.controller.MateriaController;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MateriaController.class)
public class MateriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MateriaService materiaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCrearMateria_Correcto() throws Exception {
        MateriaDto dto = new MateriaDto();
        dto.setNombre("Matemática");
        dto.setAnio(1);
        dto.setCuatrimestre(1);
        dto.setProfesorId(1L);

        Materia materia = new Materia();
        materia.setNombre("Matemática");

        Mockito.when(materiaService.crearMateria(any())).thenReturn(materia);

        mockMvc.perform(post("/materia")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Matemática"));
    }

    @Test
    void testEliminarMateria_Existente() throws Exception {
        Mockito.doNothing().when(materiaService).eliminarMateria(1L);

        mockMvc.perform(delete("/materia/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarMateria_NoExistente() throws Exception {
        Mockito.doThrow(new MateriaNotFoundException("No encontrada")).when(materiaService).eliminarMateria(1L);

        mockMvc.perform(delete("/materia/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMateriaById_Existente() throws Exception {
        Materia materia = new Materia();
        materia.setId(1L);
        materia.setNombre("Historia");

        Mockito.when(materiaService.getMateriaById(1L)).thenReturn(materia);

        mockMvc.perform(get("/materia/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Historia"));
    }

    @Test
    void testGetMateriaById_Inexistente() throws Exception {
        Mockito.when(materiaService.getMateriaById(1L))
                .thenThrow(new MateriaNotFoundException("No encontrada"));

        mockMvc.perform(get("/materia/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllMaterias() throws Exception {
        Materia m1 = new Materia(); m1.setNombre("Álgebra");
        Materia m2 = new Materia(); m2.setNombre("Programación");

        Mockito.when(materiaService.getAllMaterias()).thenReturn(List.of(m1, m2));

        mockMvc.perform(get("/materia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testBuscarMateriaPorNombre() throws Exception {
        Materia m1 = new Materia(); m1.setNombre("Física");

        Mockito.when(materiaService.getMateriasByNombre("Física"))
                .thenReturn(List.of(m1));

        mockMvc.perform(get("/materia?nombre=Física"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Física"));
    }

    @Test
    void testGetMateriasOrdenadas() throws Exception {
        Materia m1 = new Materia(); m1.setNombre("Aritmética");

        Mockito.when(materiaService.getAllMateriasOrdenadas("nombre_asc"))
                .thenReturn(List.of(m1));

        mockMvc.perform(get("/materia?order=nombre_asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Aritmética"));
    }

    @Test
    void testModificarMateria_Exitosa() throws Exception {
        MateriaDto dto = new MateriaDto();
        dto.setNombre("Redes");
        dto.setAnio(2);
        dto.setCuatrimestre(1);
        dto.setProfesorId(1L);

        Materia modificada = new Materia();
        modificada.setNombre("Redes");

        Mockito.when(materiaService.actualizarMateria(eq(1L), any())).thenReturn(modificada);

        mockMvc.perform(put("/materia/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Redes"));
    }

    @Test
    void testModificarMateria_NoExiste() throws Exception {
        MateriaDto dto = new MateriaDto();
        dto.setNombre("Sistemas");
        dto.setAnio(2);
        dto.setCuatrimestre(2);

        Mockito.when(materiaService.actualizarMateria(eq(1L), any()))
                .thenThrow(new MateriaNotFoundException("No se encuentra"));

        mockMvc.perform(put("/materia/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}
