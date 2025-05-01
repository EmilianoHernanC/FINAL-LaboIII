package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesor")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @PostMapping
    public ResponseEntity<Profesor> crearProfesor(@RequestBody ProfesorDto profesorDto) {
        Profesor creado = profesorService.crearProfesor(profesorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> getProfesor(@PathVariable Long id) {
        try {
            Profesor profesor = profesorService.getProfesorById(id);
            return ResponseEntity.ok(profesor);
        } catch (ProfesorNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Profesor>> getAllProfesores() {
        List<Profesor> profesores = profesorService.getAllProfesores();
        if (profesores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(profesores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> actualizarProfesor(
            @PathVariable Long id,
            @RequestBody ProfesorDto profesorDto) {
        try {
            Profesor actualizado = profesorService.actualizarProfesor(id, profesorDto);
            return ResponseEntity.ok(actualizado);
        } catch (ProfesorNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProfesor(@PathVariable Long id) {
        try {
            profesorService.eliminarProfesor(id);
            return ResponseEntity.noContent().build();
        } catch (ProfesorNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

