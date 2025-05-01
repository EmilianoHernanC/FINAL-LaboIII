package ar.edu.utn.frbb.tup.PersistanceTest;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.persistence.CarreraDaoMemoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CarreraDaoImplTest {

    private CarreraDaoMemoryImpl carreraDao;
    private Map<Long, Carrera> repositorioPrivado;

    @BeforeEach
    void setUp() throws Exception {
        carreraDao = new CarreraDaoMemoryImpl();

        Field field = CarreraDaoMemoryImpl.class.getDeclaredField("carrerasMap");
        field.setAccessible(true);
        repositorioPrivado = (Map<Long, Carrera>) field.get(carreraDao);
        repositorioPrivado.clear();
    }

    @Test
    void testSaveCarrera() {
        Carrera carrera = new Carrera();
        carrera.setNombre("Ingeniería en Sistemas");

        Carrera guardada = carreraDao.saveCarrera(carrera);

        assertNotNull(guardada.getId());
        assertTrue(repositorioPrivado.containsKey(guardada.getId()));
        assertEquals("Ingeniería en Sistemas", guardada.getNombre());
    }

    @Test
    void testFindByIdExistente() {
        Carrera carrera = new Carrera();
        carrera.setId(1L);
        carrera.setNombre("Bioquímica");

        repositorioPrivado.put(1L, carrera);

        Carrera encontrada = carreraDao.findById(1L);
        assertNotNull(encontrada);
        assertEquals("Bioquímica", encontrada.getNombre());
    }

    @Test
    void testFindByIdInexistente() {
        Carrera resultado = carreraDao.findById(999L);
        assertNull(resultado);
    }

    @Test
    void testFindAll() {
        repositorioPrivado.put(1L, new Carrera(1L, "Derecho"));
        repositorioPrivado.put(2L, new Carrera(2L, "Arquitectura"));

        List<Carrera> lista = carreraDao.findAll();
        assertEquals(2, lista.size());
    }

    @Test
    void testUpdateCarreraExistente() {
        Carrera carrera = new Carrera(1L, "Administración");
        repositorioPrivado.put(1L, carrera);

        carrera.setNombre("Administración de Empresas");
        Carrera actualizada = carreraDao.updateCarrera(carrera);

        assertEquals("Administración de Empresas", actualizada.getNombre());
    }

    @Test
    void testUpdateCarreraInexistente() {
        Carrera carrera = new Carrera(99L, "Psicología");
        Carrera resultado = carreraDao.updateCarrera(carrera);
        assertNull(resultado);
    }

    @Test
    void testDeleteCarreraExistente() {
        Carrera carrera = new Carrera(1L, "Contador Público");
        repositorioPrivado.put(1L, carrera);

        carreraDao.deleteCarrera(1L);
        assertFalse(repositorioPrivado.containsKey(1L));
    }

    @Test
    void testDeleteCarreraInexistente() {
        carreraDao.deleteCarrera(999L);
        // No se espera excepción, solo que siga sin existir
        assertFalse(repositorioPrivado.containsKey(999L));
    }
}
