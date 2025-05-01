package ar.edu.utn.frbb.tup.ControllerTest;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.controller.AlumnoController;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaEstadoDto;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlumnoControllerTest {

    @InjectMocks
    private AlumnoController alumnoController;

    @Mock
    private AlumnoService alumnoService;

    @Mock
    private AsignaturaService asignaturaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearAlumno() {
        AlumnoDto dto = new AlumnoDto("Juan", "Gómez", 12345678L, 1L);
        Alumno alumno = new Alumno();
        alumno.setNombre("Juan");

        when(alumnoService.crearAlumno(dto)).thenReturn(alumno);

        ResponseEntity<Alumno> response = alumnoController.crearAlumno(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Juan", response.getBody().getNombre());
    }

    @Test
    void testGetAlumnoExistente() throws Exception {
        Alumno alumno = new Alumno();
        alumno.setId(1L);
        when(alumnoService.buscarAlumnoPorId(1L)).thenReturn(alumno);

        ResponseEntity<Alumno> response = alumnoController.getAlumno(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testGetAlumnoInexistente() throws Exception {
        when(alumnoService.buscarAlumnoPorId(99L)).thenThrow(new AlumnoNotFoundException("No se encontró", 99L));

        ResponseEntity<Alumno> response = alumnoController.getAlumno(99L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testModificarAlumnoExistente() throws Exception {
        AlumnoDto dto = new AlumnoDto("Pedro", "Pérez", 12345678L, 2L);
        Alumno modificado = new Alumno();
        modificado.setNombre("Pedro");

        when(alumnoService.modificarAlumno(1L, dto)).thenReturn(modificado);

        ResponseEntity<Alumno> response = alumnoController.modificarAlumno(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Pedro", response.getBody().getNombre());
    }

    @Test
    void testModificarAlumnoInexistente() throws Exception {
        AlumnoDto dto = new AlumnoDto("Pedro", "Pérez", 12345678L, 2L);
        when(alumnoService.modificarAlumno(999L, dto)).thenThrow(new AlumnoNotFoundException("No existe", 999L));

        ResponseEntity<Alumno> response = alumnoController.modificarAlumno(999L, dto);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testEliminarAlumnoExistente() throws Exception {
        doNothing().when(alumnoService).eliminarAlumno(1L);

        ResponseEntity<Void> response = alumnoController.eliminarAlumno(1L);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testEliminarAlumnoInexistente() throws Exception {
        doThrow(new AlumnoNotFoundException("No", 123L)).when(alumnoService).eliminarAlumno(123L);

        ResponseEntity<Void> response = alumnoController.eliminarAlumno(123L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testListarTodosLosAlumnos() {
        when(alumnoService.getAllAlumnos()).thenReturn(List.of(new Alumno(), new Alumno()));
        ResponseEntity<List<Alumno>> response = alumnoController.listarTodosLosAlumnos();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAlumnosPorApellido() {
        when(alumnoService.getAlumnosByApellido("Pérez")).thenReturn(Arrays.asList(new Alumno(), new Alumno()));
        List<Alumno> resultado = alumnoController.getAlumnoByApellido("Pérez");

        assertEquals(2, resultado.size());
    }

    @Test
    void testModificarEstadoAsignaturaCorrecto() throws Exception {
        Asignatura asignatura = new Asignatura();
        asignatura.setAlumnoId(1L);
        asignatura.setAsignaturaId(2L);

        AsignaturaEstadoDto dto = new AsignaturaEstadoDto();
        dto.setEstado(EstadoAsignatura.CURSADA);

        when(asignaturaService.getAsignaturaById(2L)).thenReturn(asignatura);
        when(asignaturaService.modificarEstadoAsignatura(2L, dto)).thenReturn(asignatura);

        ResponseEntity<?> response = alumnoController.modificarEstadoAsignatura(1L, 2L, dto);

        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testModificarEstadoAsignaturaAsignaturaNoPertenece() throws Exception {
        Asignatura asignatura = new Asignatura();
        asignatura.setAlumnoId(5L); // otro alumno

        when(asignaturaService.getAsignaturaById(2L)).thenReturn(asignatura);

        ResponseEntity<?> response = alumnoController.modificarEstadoAsignatura(1L, 2L, new AsignaturaEstadoDto());

        assertEquals(403, response.getStatusCodeValue());
    }

    @Test
    void testModificarEstadoAsignaturaConError() throws Exception {
        when(asignaturaService.getAsignaturaById(99L))
                .thenThrow(new AsignaturaInexistenteException("No existe", 99L));

        ResponseEntity<?> response = alumnoController.modificarEstadoAsignatura(1L, 99L, new AsignaturaEstadoDto());

        assertEquals(400, response.getStatusCodeValue());
    }
}
