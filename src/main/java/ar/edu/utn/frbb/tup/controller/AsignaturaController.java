package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaEstadoDto;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asignatura")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    // Crear asignatura
    @PostMapping
    public ResponseEntity<Asignatura> crearAsignatura(@RequestBody AsignaturaDto asignaturaDto) {
        try {
            Asignatura asignatura = asignaturaService.crearAsignatura(asignaturaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(asignatura);
        } catch (AlumnoNotFoundException | EstadoIncorrectoException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Listar todas las asignaturas
    @GetMapping
    public ResponseEntity<List<Asignatura>> listarAsignaturas() {
        try {
            List<Asignatura> asignaturas = asignaturaService.getAllAsignaturas();
            return ResponseEntity.ok(asignaturas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Obtener una asignatura por ID
    @GetMapping("/{id}")
    public ResponseEntity<Asignatura> obtenerAsignatura(@PathVariable Long id) {
        try {
            Asignatura asignatura = asignaturaService.getAsignaturaById(id);
            return ResponseEntity.ok(asignatura);
        } catch (AsignaturaInexistenteException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Modificar estado y nota de una asignatura
    @PutMapping("/{id}")
    public ResponseEntity<Asignatura> modificarEstadoAsignatura(@PathVariable Long id, @RequestBody AsignaturaEstadoDto estadoDto) {
        try {
            Asignatura asignaturaModificada = asignaturaService.modificarEstadoAsignatura(id, estadoDto);
            return ResponseEntity.ok(asignaturaModificada);
        } catch (AsignaturaInexistenteException | EstadoIncorrectoException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Eliminar asignatura
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAsignatura(@PathVariable Long id) {
        try {
            asignaturaService.eliminarAsignatura(id);
            return ResponseEntity.noContent().build();
        } catch (AsignaturaInexistenteException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
