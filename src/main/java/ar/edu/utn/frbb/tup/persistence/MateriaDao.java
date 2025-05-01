package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.exception.MateriaNotFoundException;

import java.util.List;

public interface MateriaDao {
    Materia save(Materia materia);
    Materia findById(Long idMateria) throws MateriaNotFoundException;
    List<Materia> findAll();
    Materia update(Materia materia) throws MateriaNotFoundException;
    void delete(Long idMateria) throws MateriaNotFoundException;
}