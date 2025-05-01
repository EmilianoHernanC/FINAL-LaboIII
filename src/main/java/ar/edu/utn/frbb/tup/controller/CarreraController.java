package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.exception.CarreraNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrera")
public class CarreraController {

    @Autowired
    private CarreraService carreraService;

    /**
     * Crea una nueva carrera
     * @param carreraDto Datos de la carrera a crear
     * @return La carrera creada
     */
    @PostMapping
    public ResponseEntity<Carrera> crearCarrera(@RequestBody CarreraDto carreraDto) {
        Carrera nuevaCarrera = carreraService.crearCarrera(carreraDto);
        return new ResponseEntity<>(nuevaCarrera, HttpStatus.CREATED);
    }

    /**
     * Obtiene una carrera por su ID
     * @param id Identificador de la carrera
     * @return La carrera encontrada
     */
    @GetMapping("/{idCarrera}")
    public ResponseEntity<Carrera> getCarrera(@PathVariable("idCarrera") long id) {
        try {
            Carrera carrera = carreraService.getCarrera(id);
            return new ResponseEntity<>(carrera, HttpStatus.OK);
        } catch (CarreraNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene todas las carreras
     * @return Lista de todas las carreras
     */
    @GetMapping
    public ResponseEntity<List<Carrera>> getAllCarreras() {
        List<Carrera> carreras = carreraService.getAllCarreras();

        if (carreras.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(carreras, HttpStatus.OK);
    }

    /**
     * Actualiza una carrera existente
     * @param id Identificador de la carrera a actualizar
     * @param carreraDto Datos actualizados de la carrera
     * @return La carrera actualizada
     */
    @PutMapping("/{idCarrera}")
    public ResponseEntity<Carrera> actualizarCarrera(
            @PathVariable("idCarrera") long id,
            @RequestBody CarreraDto carreraDto) {
        try {
            Carrera carreraActualizada = carreraService.actualizarCarrera(id, carreraDto);
            return new ResponseEntity<>(carreraActualizada, HttpStatus.OK);
        } catch (CarreraNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/nombre")
    public List<Carrera> buscarCarrerasPorNombre(@RequestParam String nombre) {
        return carreraService.getCarrerasByNombre(nombre);
    }

    /**
     * Elimina una carrera por su ID
     * @param id Identificador de la carrera a eliminar
     * @return Respuesta vacía con código de estado
     */
    @DeleteMapping("/{idCarrera}")
    public ResponseEntity<Void> eliminarCarrera(@PathVariable("idCarrera") long id) {
        try {
            carreraService.eliminarCarrera(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (CarreraNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}