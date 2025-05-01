package ar.edu.utn.frbb.tup.PersistanceTest;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.ProfesorDaoMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProfesorDaoImplTest {

    private ProfesorDaoMemoryImpl profesorDao;
    private Map<Long, Profesor> repositorioPrivado;

    @BeforeEach
    void setUp() throws Exception {
        profesorDao = new ProfesorDaoMemoryImpl();

        Field field = ProfesorDaoMemoryImpl.class.getDeclaredField("repositorioProfesores");
        field.setAccessible(true);
        repositorioPrivado = (Map<Long, Profesor>) field.get(profesorDao);
        repositorioPrivado.clear();
    }

    @Test
    void testSaveProfesor() {
        Profesor profesor = new Profesor("Juan", "Pérez", "Licenciado en Matemática");

        Profesor guardado = profesorDao.saveProfesor(profesor);

        assertNotNull(guardado.getId());
        assertTrue(repositorioPrivado.containsKey(guardado.getId()));
    }

    @Test
    void testFindByIdExistente() {
        Profesor profesor = new Profesor(1L, "Ana", "Gómez", "Doctora en Física");
        repositorioPrivado.put(1L, profesor);

        Profesor encontrado = profesorDao.findById(1L);

        assertNotNull(encontrado);
        assertEquals("Ana", encontrado.getNombre());
    }

    @Test
    void testFindByIdInexistente() {
        Profesor profesor = profesorDao.findById(999L);
        assertNull(profesor);
    }

    @Test
    void testFindAll() {
        repositorioPrivado.put(1L, new Profesor(1L, "Carlos", "López", "Ingeniero"));
        repositorioPrivado.put(2L, new Profesor(2L, "Laura", "Méndez", "Licenciada"));

        List<Profesor> lista = profesorDao.findAll();

        assertEquals(2, lista.size());
    }

    @Test
    void testUpdateProfesorExistente() {
        Profesor profesor = new Profesor(1L, "Pedro", "Sosa", "Profesor de Historia");
        repositorioPrivado.put(1L, profesor);

        profesor.setTitulo("Magister en Historia");
        Profesor actualizado = profesorDao.updateProfesor(profesor);

        assertEquals("Magister en Historia", actualizado.getTitulo());
    }

    @Test
    void testUpdateProfesorInexistente() {
        Profesor profesor = new Profesor(99L, "Lucía", "Martínez", "Contadora");
        assertThrows(IllegalArgumentException.class, () -> profesorDao.updateProfesor(profesor));
    }

    @Test
    void testDeleteProfesorExistente() {
        Profesor profesor = new Profesor(1L, "Javier", "Iglesias", "Técnico");
        repositorioPrivado.put(1L, profesor);

        profesorDao.deleteProfesor(1L);

        assertFalse(repositorioPrivado.containsKey(1L));
    }

    @Test
    void testDeleteProfesorInexistente() {
        // No lanza excepción, solo se asegura que no exista
        profesorDao.deleteProfesor(999L);
        assertFalse(repositorioPrivado.containsKey(999L));
    }
}
