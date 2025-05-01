package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AlumnoDaoMemoryImpl implements AlumnoDao {

    private static final Map<Long, Alumno> repositorioAlumnos = new HashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Alumno saveAlumno(Alumno alumno) {
        if (alumno.getId() == null) {
            alumno.setId(idGenerator.getAndIncrement());
        }
        repositorioAlumnos.put(alumno.getId(), alumno);
        return alumno;
    }

    @Override
    public Alumno findAlumno(String apellidoAlumno) {
        return repositorioAlumnos.values().stream()
                .filter(a -> a.getApellido().equals(apellidoAlumno))
                .findFirst()
                .orElseThrow(() -> new AlumnoNotFoundException("No se encontró alumno con apellido: " + apellidoAlumno));
    }

    @Override
    public Alumno loadAlumno(Long dni) {
        return repositorioAlumnos.values().stream()
                .filter(a -> a.getDni().equals(dni))
                .findFirst()
                .orElseThrow(() -> new AlumnoNotFoundException("No se encontró alumno con DNI: " + dni));
    }

    @Override
    public Alumno findAlumnoById(Long id) {
        Alumno alumno = repositorioAlumnos.get(id);
        if (alumno == null) {
            throw new AlumnoNotFoundException("No se encontró alumno con ID: " + id);
        }
        return alumno;
    }

    @Override
    public List<Alumno> findAll() {
        return new ArrayList<>(repositorioAlumnos.values());
    }

    @Override
    public void deleteAlumno(Alumno alumno) {
        if (repositorioAlumnos.remove(alumno.getId()) == null) {
            throw new AlumnoNotFoundException("No se pudo eliminar el alumno con ID: " + alumno.getId());
        }
    }
}