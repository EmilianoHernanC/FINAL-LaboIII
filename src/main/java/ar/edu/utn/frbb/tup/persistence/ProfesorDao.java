package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Profesor;
import java.util.List;

public interface ProfesorDao {
    Profesor saveProfesor(Profesor profesor);
    Profesor findById(Long id);
    List<Profesor> findAll();
    Profesor updateProfesor(Profesor profesor);
    void deleteProfesor(Long id);
}
