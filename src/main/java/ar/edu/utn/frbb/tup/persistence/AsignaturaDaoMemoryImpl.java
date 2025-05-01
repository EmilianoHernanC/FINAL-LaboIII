package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class AsignaturaDaoMemoryImpl implements AsignaturaDao {

    private static final Map<Long, Asignatura> repositorioAsignaturas = new HashMap<>();
    private static final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Asignatura save(Asignatura asignatura) {
        if (asignatura.getAsignaturaId() == null) {
            asignatura.setAsignaturaId(idGenerator.getAndIncrement());
        }
        repositorioAsignaturas.put(asignatura.getAsignaturaId(), asignatura);
        return asignatura;
    }

    @Override
    public Optional<Asignatura> findById(Long asignaturaId) {
        return Optional.ofNullable(repositorioAsignaturas.get(asignaturaId));
    }

    @Override
    public Asignatura update(Asignatura asignatura) {
        if (asignatura.getAsignaturaId() == null || !repositorioAsignaturas.containsKey(asignatura.getAsignaturaId())) {
            throw new AsignaturaInexistenteException("No existe la asignatura que se intenta actualizar", asignatura.getAsignaturaId());
        }
        repositorioAsignaturas.put(asignatura.getAsignaturaId(), asignatura);
        return asignatura;
    }

    @Override
    public void delete(Long asignaturaId) {
        if (!repositorioAsignaturas.containsKey(asignaturaId)) {
            throw new AsignaturaInexistenteException("No existe la asignatura que se intenta eliminar", asignaturaId);
        }
        repositorioAsignaturas.remove(asignaturaId);
    }

    @Override
    public List<Asignatura> findAll() {
        return new ArrayList<>(repositorioAsignaturas.values());
    }

    @Override
    public List<Asignatura> findByMateriaId(Long materiaId) {
        return repositorioAsignaturas.values().stream()
                .filter(a -> a.getMateria().getMateriaId().equals(materiaId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Asignatura> findByAlumnoId(Long alumnoId) {
        return repositorioAsignaturas.values().stream()
                .filter(a -> a.getAlumnoId() != null &&
                        a.getAlumnoId().equals(alumnoId))
                .collect(Collectors.toList());
    }
}