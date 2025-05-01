package ar.edu.utn.frbb.tup.BusinessTest;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.impl.CarreraServiceImpl;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarreraServiceImplTest {

    private CarreraDao carreraDao;
    private MateriaService materiaService;
    private CarreraServiceImpl carreraService;

    @BeforeEach
    void setUp() {
        carreraDao = mock(CarreraDao.class);
        materiaService = mock(MateriaService.class);
        carreraService = new CarreraServiceImpl(carreraDao, materiaService);
    }

    @Test
    void testCrearCarreraSinMaterias() {
        CarreraDto dto = new CarreraDto();
        dto.setNombre("Ingeniería");
        dto.setCodigo("ING01");
        dto.setCantidadCuatrimestres(8);
        dto.setDepartamento(1);

        Carrera carreraMock = new Carrera();
        when(carreraDao.saveCarrera(any())).thenReturn(carreraMock);

        Carrera creada = carreraService.crearCarrera(dto);

        assertNotNull(creada);
        verify(carreraDao).saveCarrera(any());
    }

    @Test
    void testCrearCarreraConMaterias() throws MateriaNotFoundException {
        CarreraDto dto = new CarreraDto();
        dto.setNombre("Informática");
        dto.setCodigo("INF01");
        dto.setCantidadCuatrimestres(6);
        dto.setDepartamento(2);
        dto.setMateriasIds(List.of(1L, 2L));

        Materia materia1 = new Materia();
        Materia materia2 = new Materia();
        when(materiaService.getMateriaById(1L)).thenReturn(materia1);
        when(materiaService.getMateriaById(2L)).thenReturn(materia2);

        Carrera carreraMock = new Carrera();
        when(carreraDao.saveCarrera(any())).thenReturn(carreraMock);

        Carrera creada = carreraService.crearCarrera(dto);

        assertNotNull(creada);
        verify(materiaService).getMateriaById(1L);
        verify(materiaService).getMateriaById(2L);
        verify(carreraDao).saveCarrera(any());
    }

    @Test
    void testGetCarreraExistente() throws CarreraNotFoundException {
        Carrera carrera = new Carrera();
        carrera.setId(1L);

        when(carreraDao.findById(1L)).thenReturn(carrera);

        Carrera resultado = carreraService.getCarrera(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testGetCarreraInexistente() {
        when(carreraDao.findById(99L)).thenReturn(null);

        assertThrows(CarreraNotFoundException.class, () ->
                carreraService.getCarrera(99L));
    }

    @Test
    void testGetAllCarreras() {
        List<Carrera> mockList = List.of(new Carrera(), new Carrera());

        when(carreraDao.findAll()).thenReturn(mockList);

        List<Carrera> resultado = carreraService.getAllCarreras();

        assertEquals(2, resultado.size());
        verify(carreraDao).findAll();
    }

    @Test
    void testActualizarCarreraExistente() throws CarreraNotFoundException {
        CarreraDto dto = new CarreraDto();
        dto.setNombre("Sistemas");
        dto.setCodigo("SIS01");
        dto.setCantidadCuatrimestres(6);
        dto.setDepartamento(3);
        dto.setMateriasIds(List.of());

        Carrera carrera = new Carrera();
        when(carreraDao.findById(1L)).thenReturn(carrera);
        when(carreraDao.updateCarrera(any())).thenReturn(carrera);

        Carrera actualizada = carreraService.actualizarCarrera(1L, dto);

        assertEquals("Sistemas", actualizada.getNombre());
        verify(carreraDao).updateCarrera(carrera);
    }

    @Test
    void testEliminarCarreraExistente() throws CarreraNotFoundException {
        Carrera carrera = new Carrera();
        carrera.setId(1L);
        when(carreraDao.findById(1L)).thenReturn(carrera);

        carreraService.eliminarCarrera(1L);

        verify(carreraDao).deleteCarrera(1L);
    }

    @Test
    void testEliminarCarreraInexistente() {
        when(carreraDao.findById(404L)).thenReturn(null);

        assertThrows(CarreraNotFoundException.class, () ->
                carreraService.eliminarCarrera(404L));
    }

    @Test
    void testGetCarrerasByNombre() {
        Carrera c1 = new Carrera();
        c1.setNombre("Ingeniería");

        Carrera c2 = new Carrera();
        c2.setNombre("Abogacía");

        when(carreraDao.findAll()).thenReturn(List.of(c1, c2));

        List<Carrera> resultado = carreraService.getCarrerasByNombre("inge");

        assertEquals(1, resultado.size());
        assertEquals("Ingeniería", resultado.get(0).getNombre());
    }
}
