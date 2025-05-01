package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;

public interface AsignaturaEstadoService {

    // Obtener una asignatura por su ID
    Asignatura getAsignatura(Long asignaturaId) throws AsignaturaInexistenteException;

    // Obtener una asignatura específica para un alumno y una materia
    Asignatura getAsignatura(Long materiaId, Long dni) throws AsignaturaInexistenteException;

    // Cambiar el estado a CURSADA
    void cursarAsignatura(Long asignaturaId) throws AsignaturaInexistenteException, EstadoIncorrectoException;

    // Cambiar el estado a APROBADA con una nota
    void aprobarAsignatura(Long asignaturaId, int nota) throws AsignaturaInexistenteException, EstadoIncorrectoException;

    // Cambiar el estado a NO_CURSADA (perder regularidad)
    void perderRegularidad(Long asignaturaId) throws AsignaturaInexistenteException, EstadoIncorrectoException;

    // Actualizar toda la información de una asignatura
    void actualizarAsignatura(Asignatura asignatura) throws AsignaturaInexistenteException;

    // Obtener el estado actual de una asignatura
    EstadoAsignatura getEstadoAsignatura(Long asignaturaId) throws AsignaturaInexistenteException;
}