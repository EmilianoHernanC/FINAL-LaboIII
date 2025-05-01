package ar.edu.utn.frbb.tup.BusinessTest;

import ar.edu.utn.frbb.tup.business.impl.AsignaturaServiceImpl;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaEstadoDto;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AsignaturaServiceImplTest {

    private AsignaturaServiceImpl asignaturaService;
    private AsignaturaDao asignaturaDao;
    private MateriaService materiaService;

    @BeforeEach
    void setUp() {
        asignaturaDao = mock(AsignaturaDao.class);
        materiaService = mock(MateriaService.class);
        asignaturaService = new AsignaturaServiceImpl(asignaturaDao, materiaService);
    }

    @Test
    void testCrearAsignatura() {
        AsignaturaDto dto = new AsignaturaDto();
        dto.setAlumnoId(1L);
        dto.setMateriaId(10L);

        Materia materia = new Materia();
        when(materiaService.getMateriaById(10L)).thenReturn(materia);

        Asignatura asignaturaMock = new Asignatura();
        when(asignaturaDao.save(any())).thenReturn(asignaturaMock);

        Asignatura asignaturaCreada = asignaturaService.crearAsignatura(dto);

        assertNotNull(asignaturaCreada);
        assertEquals(EstadoAsignatura.NO_CURSADA, asignaturaCreada.getEstado());
        verify(asignaturaDao).save(any());
    }

    @Test
    void testGetAsignaturaByIdExitosa() throws AsignaturaInexistenteException {
        Asignatura asignatura = new Asignatura();
        when(asignaturaDao.findById(5L)).thenReturn(Optional.of(asignatura));

        Asignatura resultado = asignaturaService.getAsignaturaById(5L);

        assertNotNull(resultado);
    }

    @Test
    void testGetAsignaturaByIdNoExiste() {
        when(asignaturaDao.findById(999L)).thenReturn(Optional.empty());

        assertThrows(AsignaturaInexistenteException.class, () ->
                asignaturaService.getAsignaturaById(999L));
    }

    @Test
    void testModificarEstadoSoloEstado() throws Exception {
        Asignatura asignatura = new Asignatura();
        asignatura.setEstado(EstadoAsignatura.NO_CURSADA);

        when(asignaturaDao.findById(1L)).thenReturn(Optional.of(asignatura));
        when(asignaturaDao.update(any())).thenReturn(asignatura);

        AsignaturaEstadoDto dto = new AsignaturaEstadoDto();
        dto.setEstado(EstadoAsignatura.CURSADA);

        Asignatura modificada = asignaturaService.modificarEstadoAsignatura(1L, dto);

        assertEquals(EstadoAsignatura.CURSADA, modificada.getEstado());
    }

    @Test
    void testModificarEstadoYNotaCorrectamente() throws Exception {
        Asignatura asignatura = new Asignatura();
        asignatura.setEstado(EstadoAsignatura.APROBADA);

        when(asignaturaDao.findById(2L)).thenReturn(Optional.of(asignatura));
        when(asignaturaDao.update(any())).thenReturn(asignatura);

        AsignaturaEstadoDto dto = new AsignaturaEstadoDto();
        dto.setNota(9);

        Asignatura modificada = asignaturaService.modificarEstadoAsignatura(2L, dto);

        assertEquals(9, modificada.getNota());
    }

    @Test
    void testModificarNotaSinEstadoAprobado() {
        Asignatura asignatura = new Asignatura();
        asignatura.setEstado(EstadoAsignatura.CURSADA); // No es APROBADA

        when(asignaturaDao.findById(3L)).thenReturn(Optional.of(asignatura));

        AsignaturaEstadoDto dto = new AsignaturaEstadoDto();
        dto.setNota(10);

        assertThrows(EstadoIncorrectoException.class, () ->
                asignaturaService.modificarEstadoAsignatura(3L, dto));
    }

    @Test
    void testEliminarAsignaturaExistente() throws Exception {
        Asignatura asignatura = new Asignatura();
        when(asignaturaDao.findById(4L)).thenReturn(Optional.of(asignatura));

        asignaturaService.eliminarAsignatura(4L);

        verify(asignaturaDao).delete(4L);
    }

    @Test
    void testEliminarAsignaturaInexistente() {
        when(asignaturaDao.findById(5L)).thenReturn(Optional.empty());

        assertThrows(AsignaturaInexistenteException.class, () ->
                asignaturaService.eliminarAsignatura(5L));
    }

    @Test
    void testGetAllAsignaturas() {
        when(asignaturaDao.findAll()).thenReturn(List.of(new Asignatura(), new Asignatura()));

        List<Asignatura> lista = asignaturaService.getAllAsignaturas();

        assertEquals(2, lista.size());
        verify(asignaturaDao).findAll();
    }
}
