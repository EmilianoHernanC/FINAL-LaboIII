package ar.edu.utn.frbb.tup.BusinessTest;

import ar.edu.utn.frbb.tup.business.impl.MateriaServiceImpl;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MateriaServiceImplTest {

    private MateriaDao materiaDao;
    private ProfesorService profesorService;
    private MateriaServiceImpl materiaService;

    @BeforeEach
    void setUp() {
        materiaDao = mock(MateriaDao.class);
        profesorService = mock(ProfesorService.class);
        materiaService = new MateriaServiceImpl();
        materiaService.setDao(materiaDao); // setters para evitar el constructor
        materiaService.setProfesorService(profesorService);
    }

    @Test
    void testCrearMateriaExitosa() {
        MateriaDto dto = new MateriaDto();
        dto.setNombre("Matemática I");
        dto.setAnio(1);
        dto.setCuatrimestre(1);
        dto.setProfesorId(1L);

        Profesor profesor = new Profesor();
        profesor.setId(1L);
        when(profesorService.buscarProfesor(1L)).thenReturn(profesor);

        Materia materiaMock = new Materia();
        when(materiaDao.save(any())).thenReturn(materiaMock);

        Materia resultado = materiaService.crearMateria(dto);

        assertNotNull(resultado);
        verify(materiaDao).save(any());
    }

    @Test
    void testGetAllMaterias() {
        when(materiaDao.findAll()).thenReturn(List.of(new Materia(), new Materia()));
        List<Materia> resultado = materiaService.getAllMaterias();
        assertEquals(2, resultado.size());
    }

    @Test
    void testGetMateriaByIdExistente() throws MateriaNotFoundException {
        Materia materia = new Materia();
        materia.setId(1L);
        when(materiaDao.findById(1L)).thenReturn(materia);
        assertEquals(materia, materiaService.getMateriaById(1L));
    }

    @Test
    void testGetMateriaByIdNoExiste() {
        when(materiaDao.findById(99L)).thenReturn(null);
        assertThrows(MateriaNotFoundException.class, () -> materiaService.getMateriaById(99L));
    }

    @Test
    void testGetMateriasByNombre() {
        Materia m1 = new Materia(); m1.setNombre("Matemática");
        Materia m2 = new Materia(); m2.setNombre("Historia");
        when(materiaDao.findAll()).thenReturn(new ArrayList<>(List.of(m1, m2)));

        List<Materia> resultado = materiaService.getMateriasByNombre("mate");

        assertEquals(1, resultado.size());
        assertEquals("Matemática", resultado.get(0).getNombre());
    }

    @Test
    void testGetAllMateriasOrdenadasPorNombreAsc() {
        Materia m1 = new Materia(); m1.setNombre("Zoología");
        Materia m2 = new Materia(); m2.setNombre("Algebra");
        when(materiaDao.findAll()).thenReturn(new ArrayList<>(List.of(m1, m2)));

        List<Materia> resultado = materiaService.getAllMateriasOrdenadas("nombre_asc");

        assertEquals("Algebra", resultado.get(0).getNombre());
    }

    @Test
    void testEliminarMateriaExitosa() throws MateriaNotFoundException {
        Materia m = new Materia();
        m.setId(7L);
        when(materiaDao.findById(7L)).thenReturn(m);

        materiaService.eliminarMateria(7L);

        verify(materiaDao).delete(7L);
    }

    @Test
    void testEliminarMateriaInexistente() {
        when(materiaDao.findById(10L)).thenReturn(null);
        assertThrows(MateriaNotFoundException.class, () -> materiaService.eliminarMateria(10L));
    }

    @Test
    void testActualizarMateriaExitosa() throws Exception {
        Long materiaId = 1L;

        Materia materiaExistente = new Materia();
        materiaExistente.setId(materiaId);
        materiaExistente.setNombre("Historia");
        materiaExistente.setAnio(1);
        materiaExistente.setCuatrimestre(1);

        MateriaDto dto = new MateriaDto();
        dto.setNombre("Historia Argentina");
        dto.setAnio(2);
        dto.setCuatrimestre(2);
        dto.setProfesorId(3L);
        dto.setCodigo("HIS202");

        Profesor profesor = new Profesor();
        profesor.setId(3L);

        when(materiaDao.findById(materiaId)).thenReturn(materiaExistente);
        when(profesorService.buscarProfesor(3L)).thenReturn(profesor);
        when(materiaDao.update(any())).thenReturn(materiaExistente);

        Materia resultado = materiaService.actualizarMateria(materiaId, dto);

        assertEquals("Historia Argentina", resultado.getNombre());
        assertEquals(2, resultado.getAnio());
        assertEquals(2, resultado.getCuatrimestre());
        assertEquals("HIS202", resultado.getCodigo());
        assertEquals(3L, resultado.getProfesor().getId());
    }


    @Test
    void testActualizarMateriaInexistente() {
        Long materiaId = 999L;

        MateriaDto dto = new MateriaDto();
        dto.setNombre("Ciencias");
        dto.setAnio(1);
        dto.setCuatrimestre(1);

        when(materiaDao.findById(materiaId)).thenReturn(null);

        assertThrows(MateriaNotFoundException.class, () ->
                materiaService.actualizarMateria(materiaId, dto));
    }

}
