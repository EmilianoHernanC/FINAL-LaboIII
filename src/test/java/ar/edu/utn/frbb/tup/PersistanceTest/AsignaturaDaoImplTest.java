package ar.edu.utn.frbb.tup.PersistanceTest;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDaoMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AsignaturaDaoImplTest {

    private AsignaturaDaoMemoryImpl dao;
    private Map<Long, Asignatura> repositorio;

    @BeforeEach
    void setUp() throws Exception {
        dao = new AsignaturaDaoMemoryImpl();

        Field field = AsignaturaDaoMemoryImpl.class.getDeclaredField("repositorioAsignaturas");
        field.setAccessible(true);
        repositorio = (Map<Long, Asignatura>) field.get(null);
        repositorio.clear();
    }

    @Test
    void testSaveAsignatura() {
        Materia materia = new Materia();
        materia.setMateriaId(1L);
        materia.setNombre("Matemática");

        Asignatura asignatura = new Asignatura(1L, materia, 1001L);
        Asignatura guardada = dao.save(asignatura);

        assertNotNull(guardada.getAsignaturaId());
        assertTrue(repositorio.containsKey(guardada.getAsignaturaId()));
    }

    @Test
    void testFindByIdExistente() {
        Asignatura asignatura = new Asignatura(1L, new Materia(), 2002L);
        repositorio.put(1L, asignatura);

        Optional<Asignatura> resultado = dao.findById(1L);
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getAsignaturaId());
    }

    @Test
    void testFindByIdInexistente() {
        Optional<Asignatura> resultado = dao.findById(99L);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void testUpdateAsignaturaExistente() {
        Asignatura asignatura = new Asignatura(1L, new Materia(), 3003L);
        repositorio.put(1L, asignatura);

        asignatura.setNota(9);
        Asignatura actualizada = dao.update(asignatura);

        assertEquals(9, actualizada.getNota());
    }

    @Test
    void testUpdateAsignaturaInexistente() {
        Asignatura asignatura = new Asignatura(999L, new Materia(), 1111L);
        assertThrows(AsignaturaInexistenteException.class, () -> dao.update(asignatura));
    }

    @Test
    void testDeleteAsignaturaExistente() {
        Asignatura asignatura = new Asignatura(1L, new Materia(), 4444L);
        repositorio.put(1L, asignatura);

        dao.delete(1L);
        assertFalse(repositorio.containsKey(1L));
    }

    @Test
    void testDeleteAsignaturaInexistente() {
        assertThrows(AsignaturaInexistenteException.class, () -> dao.delete(999L));
    }

    @Test
    void testFindAll() {
        repositorio.put(1L, new Asignatura(1L, new Materia(), 1L));
        repositorio.put(2L, new Asignatura(2L, new Materia(), 2L));

        List<Asignatura> lista = dao.findAll();
        assertEquals(2, lista.size());
    }

    @Test
    void testFindByMateriaId() {
        Materia m1 = new Materia();
        m1.setMateriaId(10L);

        Materia m2 = new Materia();
        m2.setMateriaId(20L);

        dao.save(new Asignatura(null, m1, 1L));
        dao.save(new Asignatura(null, m1, 2L));
        dao.save(new Asignatura(null, m2, 3L));

        List<Asignatura> resultado = dao.findByMateriaId(10L);
        assertEquals(2, resultado.size());
    }

    @Test
    void testFindByAlumnoId() {
        Materia m = new Materia();
        m.setMateriaId(101L);

        dao.save(new Asignatura(null, m, 100L));
        dao.save(new Asignatura(null, m, 101L));
        dao.save(new Asignatura(null, m, 100L)); // mismo alumnoId

        List<Asignatura> resultado = dao.findByAlumnoId(100L);
        assertEquals(2, resultado.size());
    }
}
