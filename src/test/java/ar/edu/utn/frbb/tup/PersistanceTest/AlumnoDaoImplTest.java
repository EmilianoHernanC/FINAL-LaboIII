package ar.edu.utn.frbb.tup.PersistanceTest;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDaoMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AlumnoDaoImplTest {

    private AlumnoDaoMemoryImpl alumnoDao;
    private Map<Long, Alumno> repositorioPrivado;

    @BeforeEach
    void setUp() throws Exception {
        alumnoDao = new AlumnoDaoMemoryImpl();

        Field field = AlumnoDaoMemoryImpl.class.getDeclaredField("repositorioAlumnos");
        field.setAccessible(true);

        // Obtener la referencia al mapa estático
        repositorioPrivado = (Map<Long, Alumno>) field.get(null);
        repositorioPrivado.clear(); // Limpiar antes de cada test
    }

    @Test
    void testSaveAlumno() {
        Alumno alumno = new Alumno();
        alumno.setNombre("Juan");
        alumno.setApellido("Pérez");
        alumno.setDni(12345678L);

        Alumno guardado = alumnoDao.saveAlumno(alumno);

        assertNotNull(guardado.getId());
        assertTrue(repositorioPrivado.containsKey(guardado.getId()));
    }

    @Test
    void testFindAlumnoById_Existente() {
        Alumno alumno = new Alumno(1L, "Ana", "Gómez", 87654321L, new ArrayList<>(), null);
        repositorioPrivado.put(1L, alumno);

        Alumno resultado = alumnoDao.findAlumnoById(1L);

        assertEquals("Ana", resultado.getNombre());
    }

    @Test
    void testFindAlumnoById_NoExistente() {
        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.findAlumnoById(99L));
    }

    @Test
    void testLoadAlumnoPorDni_Existente() {
        Alumno alumno = new Alumno(2L, "Lucas", "Méndez", 99999999L, new ArrayList<>(), null);
        repositorioPrivado.put(2L, alumno);

        Alumno resultado = alumnoDao.loadAlumno(99999999L);

        assertEquals("Lucas", resultado.getNombre());
    }

    @Test
    void testLoadAlumnoPorDni_NoExistente() {
        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.loadAlumno(88888888L));
    }

    @Test
    void testFindAlumnoPorApellido_Existente() {
        Alumno alumno = new Alumno(3L, "Carlos", "Ramírez", 22222222L, new ArrayList<>(), null);
        repositorioPrivado.put(3L, alumno);

        Alumno resultado = alumnoDao.findAlumno("Ramírez");

        assertEquals("Carlos", resultado.getNombre());
    }

    @Test
    void testFindAlumnoPorApellido_NoExistente() {
        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.findAlumno("Martínez"));
    }

    @Test
    void testDeleteAlumno_Existente() {
        Alumno alumno = new Alumno(4L, "Eva", "Sosa", 12312312L, new ArrayList<>(), null);
        repositorioPrivado.put(4L, alumno);

        alumnoDao.deleteAlumno(alumno);

        assertFalse(repositorioPrivado.containsKey(4L));
    }

    @Test
    void testDeleteAlumno_NoExistente() {
        Alumno alumno = new Alumno(5L, "Fabián", "Torres", 45645645L, new ArrayList<>(), null);

        assertThrows(AlumnoNotFoundException.class, () -> alumnoDao.deleteAlumno(alumno));
    }

    @Test
    void testFindAll() {
        repositorioPrivado.put(1L, new Alumno(1L, "A", "A", 1L, new ArrayList<>(), null));
        repositorioPrivado.put(2L, new Alumno(2L, "B", "B", 2L, new ArrayList<>(), null));

        List<Alumno> lista = alumnoDao.findAll();

        assertEquals(2, lista.size());
    }
}
