package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.persistence.exception.DaoException;

import java.util.List;

public interface AlumnoDao {
    Alumno saveAlumno(Alumno a);
    Alumno findAlumno(String apellidoAlumno);
    Alumno loadAlumno(Long dni);
    Alumno findAlumnoById(Long id);
    void deleteAlumno(Alumno alumno);
    List<Alumno> findAll();
}