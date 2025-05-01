package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.exception.MateriaNotFoundException;

import java.util.List;

public interface MateriaService {
    // Métodos existentes
    Materia crearMateria(MateriaDto inputData) throws IllegalArgumentException;
    List<Materia> getAllMaterias();
    Materia getMateriaById(Long idMateria) throws MateriaNotFoundException;

    // Nuevos métodos
    List<Materia> getMateriasByNombre(String nombre);
    List<Materia> getAllMateriasOrdenadas(String ordenamiento);
    Materia actualizarMateria(Long idMateria, MateriaDto materiaDto) throws MateriaNotFoundException, IllegalArgumentException;
    void eliminarMateria(Long idMateria) throws MateriaNotFoundException;
}