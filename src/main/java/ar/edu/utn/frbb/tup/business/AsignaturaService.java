package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaEstadoDto;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;

import java.util.List;

public interface AsignaturaService {

    // Crear una nueva asignatura (alumno se inscribe en materia)
    Asignatura crearAsignatura(AsignaturaDto asignaturaDto);

    // Buscar asignatura por ID
    Asignatura getAsignaturaById(Long id) throws AsignaturaInexistenteException;

    // Modificar estado o nota de la asignatura
    Asignatura modificarEstadoAsignatura(Long id, AsignaturaEstadoDto estadoDto) throws EstadoIncorrectoException, AsignaturaInexistenteException;

    // Eliminar asignatura
    void eliminarAsignatura(Long id) throws AsignaturaInexistenteException;

    // Listar todas las asignaturas
    List<Asignatura> getAllAsignaturas();
}
