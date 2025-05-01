package ar.edu.utn.frbb.tup.BusinessTest;

import ar.edu.utn.frbb.tup.business.impl.AlumnoServiceImpl;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaEstadoDto;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AlumnoServiceImplTest {

    @Mock
    private AlumnoDao alumnoDao;

    @Mock
    private AsignaturaDao asignaturaDao;

    @Mock
    private MateriaDao materiaDao;

    @Mock
    private CarreraService carreraService;

    @Mock
    private AsignaturaService asignaturaService;

    @InjectMocks
    private AlumnoServiceImpl alumnoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearAlumno() {
        AlumnoDto alumnoDto = new AlumnoDto("Juan", "Pérez", 12345678L, 1L);

        Carrera carrera = new Carrera();
        carrera.setNombre("Ingeniería");

        Alumno alumno = new Alumno();
        alumno.setNombre("Juan");
        alumno.setApellido("Pérez");
        alumno.setDni(12345678L);
        alumno.setCarrera(carrera);

        when(carreraService.getCarrera(1L)).thenReturn(carrera);
        when(alumnoDao.saveAlumno(any())).thenReturn(alumno);

        Alumno creado = alumnoService.crearAlumno(alumnoDto);

        assertNotNull(creado);
        assertEquals("Juan", creado.getNombre());
        assertEquals("Pérez", creado.getApellido());
        assertEquals(12345678L, creado.getDni());
        assertEquals("Ingeniería", creado.getCarrera().getNombre());
    }

    @Test
    void testBuscarAlumnoPorIdExistente() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno();
        alumno.setId(1L);
        alumno.setNombre("Ana");

        when(alumnoDao.findAlumnoById(1L)).thenReturn(alumno);

        Alumno resultado = alumnoService.buscarAlumnoPorId(1L);

        assertNotNull(resultado);
        assertEquals("Ana", resultado.getNombre());
    }

    @Test
    void testBuscarAlumnoPorIdInexistente() {
        when(alumnoDao.findAlumnoById(99L)).thenReturn(null);

        assertThrows(AlumnoNotFoundException.class, () ->
                alumnoService.buscarAlumnoPorId(99L));
    }

    @Test
    void testModificarAlumnoExistente() throws Exception {
        Long idAlumno = 4L;
        Alumno existente = new Alumno(idAlumno, "Pedro", "Ramírez", 99887766L, new ArrayList<>(), null);
        AlumnoDto dto = new AlumnoDto("Pedro", "Ramírez", 99887766L, 2L);
        Carrera nuevaCarrera = new Carrera("Abogacía", 2, 1, 8);

        when(alumnoDao.findAlumnoById(idAlumno)).thenReturn(existente);
        when(carreraService.getCarrera(2L)).thenReturn(nuevaCarrera);
        when(alumnoDao.saveAlumno(any())).thenReturn(existente);

        Alumno resultado = alumnoService.modificarAlumno(idAlumno, dto);

        assertEquals("Abogacía", resultado.getCarrera().getNombre());
        verify(alumnoDao).saveAlumno(existente);
    }

    @Test
    void testModificarAlumnoInexistente() {
        Long idAlumno = 999L;
        AlumnoDto dto = new AlumnoDto("Lucas", "Desconocido", 11111111L, 3L);
        when(alumnoDao.findAlumnoById(idAlumno)).thenReturn(null);

        AlumnoNotFoundException exception = assertThrows(AlumnoNotFoundException.class, () ->
                alumnoService.modificarAlumno(idAlumno, dto));

        assertEquals("No se encontró el alumno con ID: 999", exception.getMessage());
    }


    @Test
    void testEliminarAlumnoExistente() throws AlumnoNotFoundException {
        Alumno alumno = new Alumno();
        alumno.setId(3L);
        when(alumnoDao.findAlumnoById(3L)).thenReturn(alumno);
        doNothing().when(alumnoDao).deleteAlumno(alumno);

        alumnoService.eliminarAlumno(3L);

        verify(alumnoDao).deleteAlumno(alumno);
    }

    @Test
    void testEliminarAlumnoInexistente() {
        when(alumnoDao.findAlumnoById(999L)).thenReturn(null);

        assertThrows(AlumnoNotFoundException.class, () ->
                alumnoService.eliminarAlumno(999L));
    }

    @Test
    void testGetAllAlumnos() {
        List<Alumno> listaMock = List.of(
                new Alumno(1L, "Pedro", "López", 11223344L, new ArrayList<>(), new Carrera()),
                new Alumno(2L, "Clara", "Ramírez", 99887766L, new ArrayList<>(), new Carrera())
        );

        when(alumnoDao.findAll()).thenReturn(listaMock);

        List<Alumno> resultado = alumnoService.getAllAlumnos();

        assertEquals(2, resultado.size());
        verify(alumnoDao, times(1)).findAll();
    }

    @Test
    void testGetAlumnosByApellido() {
        List<Alumno> alumnos = List.of(
                new Alumno(1L, "Ana", "Gómez", 123L, new ArrayList<>(), null),
                new Alumno(2L, "Juan", "Gómez", 456L, new ArrayList<>(), null),
                new Alumno(3L, "Pedro", "Ramírez", 789L, new ArrayList<>(), null)
        );
        when(alumnoDao.findAll()).thenReturn(alumnos);

        List<Alumno> resultados = alumnoService.getAlumnosByApellido("gómez");

        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().allMatch(a -> a.getApellido().equalsIgnoreCase("gómez")));
    }

    @Test
    void testModificarEstadoAsignaturaCorrecto() throws Exception {
        Long idAlumno = 1L;
        Long idAsignatura = 10L;

        Alumno alumno = new Alumno();
        alumno.setId(idAlumno);

        Asignatura asignatura = new Asignatura();
        asignatura.setAlumnoId(idAlumno);

        AsignaturaEstadoDto estadoDto = new AsignaturaEstadoDto();
        estadoDto.setEstado(EstadoAsignatura.CURSADA);

        when(alumnoDao.findAlumnoById(idAlumno)).thenReturn(alumno);
        when(asignaturaService.getAsignaturaById(idAsignatura)).thenReturn(asignatura);
        when(asignaturaService.modificarEstadoAsignatura(idAsignatura, estadoDto)).thenReturn(asignatura);

        Asignatura resultado = alumnoService.modificarEstadoAsignatura(idAlumno, idAsignatura, estadoDto);

        assertNotNull(resultado);
    }

    @Test
    void testModificarEstadoAsignaturaNoPerteneceAlumno() throws Exception {
        Long idAlumno = 1L;
        Long idAsignatura = 10L;

        Alumno alumno = new Alumno();
        alumno.setId(idAlumno);

        Asignatura asignatura = new Asignatura();
        asignatura.setAlumnoId(999L); // otro alumno

        when(alumnoDao.findAlumnoById(idAlumno)).thenReturn(alumno);
        when(asignaturaService.getAsignaturaById(idAsignatura)).thenReturn(asignatura);

        assertThrows(AlumnoNotFoundException.class, () ->
                alumnoService.modificarEstadoAsignatura(idAlumno, idAsignatura, new AsignaturaEstadoDto()));
    }

    @Test
    void testModificarEstadoAsignatura_Exito() throws Exception {
        Long alumnoId = 1L;
        Long asignaturaId = 10L;

        AsignaturaEstadoDto dto = new AsignaturaEstadoDto();
        dto.setEstado(EstadoAsignatura.CURSADA);

        Alumno alumno = new Alumno();
        alumno.setId(alumnoId);

        Asignatura asignatura = new Asignatura();
        asignatura.setAsignaturaId(asignaturaId);
        asignatura.setAlumnoId(alumnoId);

        when(alumnoDao.findAlumnoById(alumnoId)).thenReturn(alumno);
        when(asignaturaService.getAsignaturaById(asignaturaId)).thenReturn(asignatura);
        when(asignaturaService.modificarEstadoAsignatura(asignaturaId, dto)).thenReturn(asignatura);

        Asignatura resultado = alumnoService.modificarEstadoAsignatura(alumnoId, asignaturaId, dto);

        assertNotNull(resultado);
        verify(asignaturaService).modificarEstadoAsignatura(asignaturaId, dto);
    }

    @Test
    void testModificarEstadoAsignatura_AsignaturaNoPerteneceAlAlumno() throws Exception {
        Long alumnoId = 1L;
        Long asignaturaId = 10L;

        Alumno alumno = new Alumno();
        alumno.setId(alumnoId);

        Asignatura asignatura = new Asignatura();
        asignatura.setAsignaturaId(asignaturaId);
        asignatura.setAlumnoId(99L); // otro alumno

        when(alumnoDao.findAlumnoById(alumnoId)).thenReturn(alumno);
        when(asignaturaService.getAsignaturaById(asignaturaId)).thenReturn(asignatura);

        AsignaturaEstadoDto dto = new AsignaturaEstadoDto();
        dto.setEstado(EstadoAsignatura.APROBADA);

        assertThrows(AlumnoNotFoundException.class, () ->
                alumnoService.modificarEstadoAsignatura(alumnoId, asignaturaId, dto)
        );
    }

    @Test
    void testGetAlumnosByApellidoExistente() {
        List<Alumno> alumnosMock = List.of(
                new Alumno(1L, "Lucas", "Pérez", 12345678L, new ArrayList<>(), new Carrera()),
                new Alumno(2L, "Ana", "Pérez", 87654321L, new ArrayList<>(), new Carrera())
        );

        when(alumnoDao.findAll()).thenReturn(alumnosMock);

        List<Alumno> resultado = alumnoService.getAlumnosByApellido("Pérez");

        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(a -> a.getApellido().equalsIgnoreCase("Pérez")));
    }



}
