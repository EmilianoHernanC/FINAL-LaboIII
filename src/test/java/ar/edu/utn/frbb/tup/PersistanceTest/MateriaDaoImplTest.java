package ar.edu.utn.frbb.tup.PersistanceTest;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.MateriaDaoMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MateriaDaoImplTest {

    private MateriaDaoMemoryImpl materiaDao;
    private Map<Long, Materia> repositorioPrivado;

    @BeforeEach
    void setUp() throws Exception {
        materiaDao = new MateriaDaoMemoryImpl();

        Field field = MateriaDaoMemoryImpl.class.getDeclaredField("repositorioMateria");
        field.setAccessible(true);
        repositorioPrivado = (Map<Long, Materia>) field.get(null);
        repositorioPrivado.clear();
    }

    @Test
    void testSaveMateria() {
        Materia materia = new Materia();
        materia.setNombre("Matemática");

        Materia guardada = materiaDao.save(materia);

        assertNotNull(guardada.getMateriaId());
        assertTrue(repositorioPrivado.containsKey(guardada.getMateriaId()));
    }

    @Test
    void testFindByIdExistente() throws MateriaNotFoundException {
        Materia materia = new Materia();
        materia.setMateriaId(1L);
        materia.setNombre("Historia");

        repositorioPrivado.put(1L, materia);

        Materia encontrada = materiaDao.findById(1L);
        assertEquals("Historia", encontrada.getNombre());
    }

    @Test
    void testFindByIdInexistente() {
        assertThrows(MateriaNotFoundException.class, () -> materiaDao.findById(999L));
    }

    @Test
    void testFindAll() {
        Materia fisica = new Materia();
        fisica.setMateriaId(1L);
        fisica.setNombre("Física");

        Materia quimica = new Materia();
        quimica.setMateriaId(2L);
        quimica.setNombre("Química");

        repositorioPrivado.put(1L, fisica);
        repositorioPrivado.put(2L, quimica);

        List<Materia> lista = materiaDao.findAll();
        assertEquals(2, lista.size());
    }

    @Test
    void testUpdateMateriaExistente() throws MateriaNotFoundException {
        Materia materia = new Materia();
        materia.setMateriaId(1L);
        materia.setNombre("Lengua");

        repositorioPrivado.put(1L, materia);

        materia.setNombre("Lengua y Literatura");
        Materia actualizada = materiaDao.update(materia);

        assertEquals("Lengua y Literatura", actualizada.getNombre());
    }

    @Test
    void testUpdateMateriaInexistente() {
        Materia materia = new Materia();
        materia.setMateriaId(99L);
        materia.setNombre("Filosofía");

        assertThrows(MateriaNotFoundException.class, () -> materiaDao.update(materia));
    }

    @Test
    void testDeleteMateriaExistente() throws MateriaNotFoundException {
        Materia materia = new Materia();
        materia.setMateriaId(1L);
        materia.setNombre("Biología");

        repositorioPrivado.put(1L, materia);

        materiaDao.delete(1L);
        assertFalse(repositorioPrivado.containsKey(1L));
    }

    @Test
    void testDeleteMateriaInexistente() {
        assertThrows(MateriaNotFoundException.class, () -> materiaDao.delete(999L));
    }
}
