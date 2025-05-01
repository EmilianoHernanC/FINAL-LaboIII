package ar.edu.utn.frbb.tup.ControllerTest;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.controller.CarreraController;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.exception.CarreraNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarreraControllerTest {

    private CarreraService carreraService;
    private CarreraController carreraController;

    @BeforeEach
    void setUp() {
        carreraService = mock(CarreraService.class);
        carreraController = new CarreraController();
        carreraController.getClass().getDeclaredFields();
        // Inyectamos el mock manualmente
        try {
            var field = CarreraController.class.getDeclaredField("carreraService");
            field.setAccessible(true);
            field.set(carreraController, carreraService);
        } catch (Exception e) {
            throw new RuntimeException("Error al inyectar carreraService", e);
        }
    }

    @Test
    void testCrearCarrera() {
        CarreraDto dto = new CarreraDto();
        dto.setNombre("Ingeniería");
        Carrera mockCarrera = new Carrera();
        mockCarrera.setNombre("Ingeniería");

        when(carreraService.crearCarrera(dto)).thenReturn(mockCarrera);

        ResponseEntity<Carrera> response = carreraController.crearCarrera(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Ingeniería", response.getBody().getNombre());
    }

    @Test
    void testGetCarreraExistente() throws Exception {
        Carrera carrera = new Carrera();
        carrera.setId(1L);
        carrera.setNombre("Bioingeniería");

        when(carreraService.getCarrera(1L)).thenReturn(carrera);

        ResponseEntity<Carrera> response = carreraController.getCarrera(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Bioingeniería", response.getBody().getNombre());
    }

    @Test
    void testGetCarreraInexistente() throws Exception {
        when(carreraService.getCarrera(999L)).thenThrow(new CarreraNotFoundException("No encontrada", 999L));

        ResponseEntity<Carrera> response = carreraController.getCarrera(999L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetAllCarreras() {
        when(carreraService.getAllCarreras()).thenReturn(List.of(new Carrera(), new Carrera()));

        ResponseEntity<List<Carrera>> response = carreraController.getAllCarreras();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAllCarrerasVacia() {
        when(carreraService.getAllCarreras()).thenReturn(List.of());

        ResponseEntity<List<Carrera>> response = carreraController.getAllCarreras();

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testActualizarCarreraExitosa() throws Exception {
        CarreraDto dto = new CarreraDto();
        dto.setNombre("Renovada");

        Carrera actualizada = new Carrera();
        actualizada.setNombre("Renovada");

        when(carreraService.actualizarCarrera(1L, dto)).thenReturn(actualizada);

        ResponseEntity<Carrera> response = carreraController.actualizarCarrera(1L, dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Renovada", response.getBody().getNombre());
    }

    @Test
    void testActualizarCarreraInexistente() throws Exception {
        CarreraDto dto = new CarreraDto();
        when(carreraService.actualizarCarrera(100L, dto)).thenThrow(new CarreraNotFoundException("No", 100L));

        ResponseEntity<Carrera> response = carreraController.actualizarCarrera(100L, dto);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testBuscarCarreraPorNombre() {
        Carrera c = new Carrera();
        c.setNombre("Electrónica");
        when(carreraService.getCarrerasByNombre("Electro")).thenReturn(List.of(c));

        List<Carrera> lista = carreraController.buscarCarrerasPorNombre("Electro");

        assertEquals(1, lista.size());
        assertEquals("Electrónica", lista.get(0).getNombre());
    }

    @Test
    void testEliminarCarreraExitosa() throws Exception {
        doNothing().when(carreraService).eliminarCarrera(10L);

        ResponseEntity<Void> response = carreraController.eliminarCarrera(10L);

        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testEliminarCarreraInexistente() throws Exception {
        doThrow(new CarreraNotFoundException("No existe", 123L)).when(carreraService).eliminarCarrera(123L);

        ResponseEntity<Void> response = carreraController.eliminarCarrera(123L);

        assertEquals(404, response.getStatusCodeValue());
    }
}
