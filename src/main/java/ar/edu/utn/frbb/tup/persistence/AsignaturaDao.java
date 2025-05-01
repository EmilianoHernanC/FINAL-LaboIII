package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import java.util.Optional;
import java.util.List;

public interface AsignaturaDao {
    // Guardar una nueva asignatura
    Asignatura save(Asignatura asignatura);

    // Buscar una asignatura por ID
    Optional<Asignatura> findById(Long asignaturaId);

    // Actualizar una asignatura existente
    Asignatura update(Asignatura asignatura);

    // Eliminar una asignatura
    void delete(Long asignaturaId);

    // Buscar todas las asignaturas
    List<Asignatura> findAll();

    // Buscar asignaturas por ID de materia
    List<Asignatura> findByMateriaId(Long materiaId);

    // Buscar asignaturas por ID de alumno
    List<Asignatura> findByAlumnoId(Long alumnoId);
}