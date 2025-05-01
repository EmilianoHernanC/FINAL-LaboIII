package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Profesor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProfesorDaoMemoryImpl implements ProfesorDao {

    private final Map<Long, Profesor> repositorioProfesores = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Profesor saveProfesor(Profesor profesor) {
        if (profesor.getId() == null) {
            profesor.setId(idGenerator.getAndIncrement());
        }
        repositorioProfesores.put(profesor.getId(), profesor);
        return profesor;
    }

    @Override
    public Profesor findById(Long id) {
        return repositorioProfesores.get(id);
    }

    @Override
    public List<Profesor> findAll() {
        return new ArrayList<>(repositorioProfesores.values());
    }

    @Override
    public Profesor updateProfesor(Profesor profesor) {
        if (profesor.getId() == null || !repositorioProfesores.containsKey(profesor.getId())) {
            throw new IllegalArgumentException("No se puede actualizar el profesor: ID inválido");
        }
        repositorioProfesores.put(profesor.getId(), profesor);
        return profesor;
    }

    @Override
    public void deleteProfesor(Long id) {
        repositorioProfesores.remove(id);
    }
}
