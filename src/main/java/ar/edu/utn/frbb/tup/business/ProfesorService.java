package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNotFoundException;

import java.util.List;

public interface ProfesorService {

    /**
     * Crea un nuevo profesor a partir de un DTO
     */
    Profesor crearProfesor(ProfesorDto profesorDto);

    /**
     * Busca un profesor por su ID
     */
    Profesor getProfesorById(Long id) throws ProfesorNotFoundException;

    /**
     * Devuelve todos los profesores
     */
    List<Profesor> getAllProfesores();

    /**
     * Actualiza los datos de un profesor
     */
    Profesor actualizarProfesor(Long id, ProfesorDto profesorDto) throws ProfesorNotFoundException;

    /**
     * Elimina un profesor por su ID
     */
    void eliminarProfesor(Long id) throws ProfesorNotFoundException;

    Profesor buscarProfesor(Long id) throws ProfesorNotFoundException;
}
