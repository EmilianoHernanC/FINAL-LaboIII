package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
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
@RequestMapping("/alumno")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private AsignaturaService asignaturaService;

    @PostMapping
    public ResponseEntity<Alumno> crearAlumno(@RequestBody AlumnoDto alumnoDto) {
        Alumno alumnoCreado = alumnoService.crearAlumno(alumnoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(alumnoCreado);
    }

    @GetMapping("/{idAlumno}")
    public ResponseEntity<Alumno> getAlumno(@PathVariable Long idAlumno) {
        try {
            Alumno alumno = alumnoService.buscarAlumnoPorId(idAlumno);
            return ResponseEntity.ok(alumno);
        } catch (AlumnoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{idAlumno}")
    public ResponseEntity<Alumno> modificarAlumno(
            @PathVariable Long idAlumno,
            @RequestBody AlumnoDto alumnoDto) {
        try {
            Alumno alumnoModificado = alumnoService.modificarAlumno(idAlumno, alumnoDto);
            return ResponseEntity.ok(alumnoModificado);
        } catch (AlumnoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/apellido")
    public List<Alumno> getAlumnoByApellido(@RequestParam String apellido) {
        return alumnoService.getAlumnosByApellido(apellido);
    }

    @GetMapping
    public ResponseEntity<List<Alumno>> listarTodosLosAlumnos() {
        return ResponseEntity.ok(alumnoService.getAllAlumnos());
    }

    @DeleteMapping("/{idAlumno}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Long idAlumno) {
        try {
            alumnoService.eliminarAlumno(idAlumno);
            return ResponseEntity.noContent().build();
        } catch (AlumnoNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idAlumno}/asignatura/{idAsignatura}")
    public ResponseEntity<?> modificarEstadoAsignatura(
            @PathVariable Long idAlumno,
            @PathVariable Long idAsignatura,
            @RequestBody AsignaturaEstadoDto estadoDto) {
        try {
            Asignatura asignatura = asignaturaService.getAsignaturaById(idAsignatura);

            // Verificamos que la asignatura pertenezca al alumno
            if (!asignatura.getAlumnoId().equals(idAlumno)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Asignatura no pertenece al alumno");
            }

            Asignatura asignaturaActualizada = asignaturaService.modificarEstadoAsignatura(idAsignatura, estadoDto);
            return ResponseEntity.ok(asignaturaActualizada);

        } catch (AsignaturaInexistenteException | EstadoIncorrectoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
