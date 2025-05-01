package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CarreraDaoMemoryImpl implements CarreraDao {

    // Mapa para almacenar las carreras en memoria, con el ID como clave
    private final Map<Long, Carrera> carrerasMap = new HashMap<>();

    // Generador de IDs únicos
    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    public Carrera saveCarrera(Carrera carrera) {
        // Asignar un ID único a la carrera
        long id = sequence.getAndIncrement();
        carrera.setId(id);

        // Guardar la carrera en el mapa
        carrerasMap.put(id, carrera);

        return carrera;
    }

    @Override
    public Carrera findById(Long id) {
        return carrerasMap.get(id);
    }

    @Override
    public List<Carrera> findAll() {
        // Devolver todas las carreras como una lista
        return new ArrayList<>(carrerasMap.values());
    }

    @Override
    public Carrera updateCarrera(Carrera carrera) {
        // Verificar que la carrera tenga un ID válido
        if (carrera.getId() <= 0 || !carrerasMap.containsKey(carrera.getId())) {
            return null; // O podríamos lanzar una excepción
        }

        // Actualizar la carrera en el mapa
        carrerasMap.put(carrera.getId(), carrera);

        return carrera;
    }

    @Override
    public void deleteCarrera(Long id) {
        carrerasMap.remove(id);
    }
}