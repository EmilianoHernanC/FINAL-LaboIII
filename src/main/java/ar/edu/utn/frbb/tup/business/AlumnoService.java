package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaEstadoDto;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;

import java.util.List;

public interface AlumnoService {

    Alumno crearAlumno(AlumnoDto alumnoDto);

    Alumno buscarAlumno(String apellidoAlumno) throws AlumnoNotFoundException;

    Alumno buscarAlumnoPorId(Long idAlumno) throws AlumnoNotFoundException;

    Alumno modificarAlumno(Long idAlumno, AlumnoDto alumnoDto) throws AlumnoNotFoundException;

    void eliminarAlumno(Long idAlumno) throws AlumnoNotFoundException;

    Asignatura modificarEstadoAsignatura(Long idAlumno, Long idAsignatura, AsignaturaEstadoDto estadoDto)
            throws AlumnoNotFoundException, EstadoIncorrectoException, AsignaturaInexistenteException;

    List<Alumno> getAlumnosByApellido(String apellido);

    List<Alumno> getAllAlumnos();
}
