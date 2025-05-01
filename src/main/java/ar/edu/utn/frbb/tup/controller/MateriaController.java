package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    // 1. Crear materia
    @PostMapping("/materia")
    public ResponseEntity<Materia> crearMateria(@RequestBody MateriaDto materiaDto) {
        try {
            Materia nuevaMateria = materiaService.crearMateria(materiaDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMateria);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 2. Modificar materia
    @PutMapping("/materia/{idMateria}")
    public ResponseEntity<Materia> modificarMateria(
            @PathVariable Long idMateria,
            @RequestBody MateriaDto materiaDto) {
        try {
            Materia materiaModificada = materiaService.actualizarMateria(idMateria, materiaDto);
            return ResponseEntity.ok(materiaModificada);
        } catch (MateriaNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 3. Eliminar materia
    @DeleteMapping("/materia/{idMateria}")
    public ResponseEntity<Void> eliminarMateria(@PathVariable Long idMateria) {
        try {
            materiaService.eliminarMateria(idMateria);
            return ResponseEntity.noContent().build();
        } catch (MateriaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 4. Obtener todas las materias o filtrar por nombre u orden
    @GetMapping("/materia")
    public ResponseEntity<List<Materia>> getMaterias(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String order) {

        if (nombre != null && !nombre.isEmpty()) {
            return ResponseEntity.ok(materiaService.getMateriasByNombre(nombre));
        } else if (order != null && !order.isEmpty()) {
            return ResponseEntity.ok(materiaService.getAllMateriasOrdenadas(order.trim().toLowerCase()));
        } else {
            return ResponseEntity.ok(materiaService.getAllMaterias());
        }
    }

    // 5. Obtener materia por ID
    @GetMapping("/materia/{idMateria}")
    public ResponseEntity<Materia> getMateriaById(@PathVariable Long idMateria) {
        try {
            return ResponseEntity.ok(materiaService.getMateriaById(idMateria));
        } catch (MateriaNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // 6. Obtener materia por ID
    @GetMapping("/materia/nombre")
    public List<Materia> buscarMateriaPorNombre(@RequestParam String nombre) {
        return materiaService.getMateriasByNombre(nombre);
    }

    // 7. Obtener materias ordenadas (ruta separada como pide el enunciado)
    @GetMapping("/materias")
    public ResponseEntity<List<Materia>> getMateriasOrdenadas(
            @RequestParam String order) {
        return ResponseEntity.ok(materiaService.getAllMateriasOrdenadas(order.trim().toLowerCase()));
    }
}
