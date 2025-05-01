package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class MateriaDaoMemoryImpl implements MateriaDao {

    private static final Map<Long, Materia> repositorioMateria = new HashMap<>();

    @Override
    public Materia save(Materia materia) {
        // Generar un ID positivo aleatorio de tipo Long
        Random random = new Random();
        long id = Math.abs(random.nextLong() % 10000L) + 1;

        while (repositorioMateria.containsKey(id)) {
            id = Math.abs(random.nextLong() % 10000L) + 1;
        }

        materia.setMateriaId(id);
        repositorioMateria.put(id, materia);
        return materia;
    }

    @Override
    public Materia findById(Long idMateria) throws MateriaNotFoundException {
        Materia materia = repositorioMateria.get(idMateria);
        if (materia == null) {
            throw new MateriaNotFoundException("No se encontró la materia con id " + idMateria);
        }
        return materia;
    }

    @Override
    public List<Materia> findAll() {
        return new ArrayList<>(repositorioMateria.values());
    }

    @Override
    public Materia update(Materia materia) throws MateriaNotFoundException {
        Long id = materia.getMateriaId();
        if (!repositorioMateria.containsKey(id)) {
            throw new MateriaNotFoundException("No se puede actualizar: la materia con id " + id + " no existe");
        }
        repositorioMateria.put(id, materia);
        return materia;
    }

    @Override
    public void delete(Long idMateria) throws MateriaNotFoundException {
        if (!repositorioMateria.containsKey(idMateria)) {
            throw new MateriaNotFoundException("No se puede eliminar: la materia con id " + idMateria + " no existe");
        }
        repositorioMateria.remove(idMateria);
    }
}