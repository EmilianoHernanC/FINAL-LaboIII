package ar.edu.utn.frbb.tup.BusinessTest;

import ar.edu.utn.frbb.tup.business.impl.ProfesorServiceImpl;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfesorServiceImplTest {

    private ProfesorDao profesorDao;
    private MateriaDao materiaDao;
    private ProfesorServiceImpl profesorService;

    @BeforeEach
    void setUp() {
        profesorDao = mock(ProfesorDao.class);
        materiaDao = mock(MateriaDao.class);
        profesorService = new ProfesorServiceImpl();
        profesorService.setProfesorDao(profesorDao);
        profesorService.setMateriaDao(materiaDao);
    }

    @Test
    void testCrearProfesorSinMaterias() {
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Juan");
        dto.setApellido("Pérez");
        dto.setTitulo("Licenciado");

        Profesor profesor = new Profesor("Juan", "Pérez", "Licenciado");
        when(profesorDao.saveProfesor(any())).thenReturn(profesor);

        Profesor resultado = profesorService.crearProfesor(dto);

        assertNotNull(resultado);
        assertEquals("Juan", resultado.getNombre());
    }

    @Test
    void testCrearProfesorConMaterias() {
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Lucía");
        dto.setApellido("Sosa");
        dto.setTitulo("Doctora");
        dto.setMateriasDictadasIds(List.of(1L, 2L));

        Materia m1 = new Materia(); m1.setId(1L);
        Materia m2 = new Materia(); m2.setId(2L);

        when(materiaDao.findById(1L)).thenReturn(m1);
        when(materiaDao.findById(2L)).thenReturn(m2);

        when(profesorDao.saveProfesor(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Profesor resultado = profesorService.crearProfesor(dto);

        assertEquals(2, resultado.getMateriasDictadas().size());
        verify(profesorDao).saveProfesor(any());
    }

    @Test
    void testGetProfesorByIdExistente() throws Exception {
        Profesor profesor = new Profesor(1L, "María", "Gómez", "Magíster");
        when(profesorDao.findById(1L)).thenReturn(profesor);

        Profesor resultado = profesorService.getProfesorById(1L);

        assertEquals("María", resultado.getNombre());
    }

    @Test
    void testGetProfesorByIdInexistente() {
        when(profesorDao.findById(999L)).thenReturn(null);

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.getProfesorById(999L));
    }

    @Test
    void testActualizarProfesorExistente() throws Exception {
        Profesor existente = new Profesor(1L, "Juan", "Pérez", "Licenciado");

        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Juan Carlos");
        dto.setApellido("Pérez");
        dto.setTitulo("Doctor");

        when(profesorDao.findById(1L)).thenReturn(existente);
        when(profesorDao.saveProfesor(any())).thenAnswer(i -> i.getArgument(0));

        Profesor actualizado = profesorService.actualizarProfesor(1L, dto);

        assertEquals("Juan Carlos", actualizado.getNombre());
        assertEquals("Doctor", actualizado.getTitulo());
    }

    @Test
    void testActualizarProfesorInexistente() {
        ProfesorDto dto = new ProfesorDto();
        dto.setNombre("Lucas");

        when(profesorDao.findById(123L)).thenReturn(null);

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.actualizarProfesor(123L, dto));
    }

    @Test
    void testEliminarProfesorExistente() throws Exception {
        Profesor profesor = new Profesor(4L, "Tito", "Fernández", "Doctor");
        when(profesorDao.findById(4L)).thenReturn(profesor);

        profesorService.eliminarProfesor(4L);

        verify(profesorDao).deleteProfesor(4L);
    }

    @Test
    void testEliminarProfesorInexistente() {
        when(profesorDao.findById(888L)).thenReturn(null);

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.eliminarProfesor(888L));
    }

    @Test
    void testGetAllProfesores() {
        when(profesorDao.findAll()).thenReturn(List.of(new Profesor(), new Profesor()));
        assertEquals(2, profesorService.getAllProfesores().size());
    }

    @Test
    void testBuscarProfesorExistente() throws Exception {
        Profesor profesor = new Profesor(7L, "Luis", "Aguilar", "Ingeniero");
        when(profesorDao.findById(7L)).thenReturn(profesor);

        Profesor encontrado = profesorService.buscarProfesor(7L);

        assertNotNull(encontrado);
        assertEquals("Luis", encontrado.getNombre());
    }

    @Test
    void testBuscarProfesorInexistente() {
        when(profesorDao.findById(111L)).thenReturn(null);

        assertThrows(ProfesorNotFoundException.class, () -> profesorService.buscarProfesor(111L));
    }
}
