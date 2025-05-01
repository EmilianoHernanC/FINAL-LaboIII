package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AlumnoService;
import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AlumnoDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaEstadoDto;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlumnoServiceImpl implements AlumnoService {

    @Autowired
    private AlumnoDao alumnoDao;

    @Autowired
    private AsignaturaDao asignaturaDao;

    @Autowired
    private MateriaDao materiaDao;

    @Autowired
    private CarreraService carreraService;

    @Autowired
    private AsignaturaService asignaturaService;

    @Override
    public Asignatura modificarEstadoAsignatura(Long idAlumno, Long idAsignatura, AsignaturaEstadoDto estadoDto)
            throws AlumnoNotFoundException, EstadoIncorrectoException {
        try {
            Alumno alumno = buscarAlumnoPorId(idAlumno);
            Asignatura asignatura = asignaturaService.getAsignaturaById(idAsignatura);

            // Validar que esa asignatura pertenece al alumno
            if (!asignatura.getAlumnoId().equals(alumno.getId())) {
                throw new AlumnoNotFoundException("La asignatura no pertenece al alumno con ID " + idAlumno);
            }

            return asignaturaService.modificarEstadoAsignatura(idAsignatura, estadoDto);
        } catch (AsignaturaInexistenteException e) {
            throw new RuntimeException("Asignatura no encontrada", e);
        }
    }

    @Override
    public Alumno crearAlumno(AlumnoDto alumnoDto) {
        try {
            Alumno alumno = new Alumno();
            alumno.setNombre(alumnoDto.getNombre());
            alumno.setApellido(alumnoDto.getApellido());
            alumno.setDni(alumnoDto.getDni());

            Carrera carrera = carreraService.getCarrera(alumnoDto.getCarreraId());
            alumno.setCarrera(carrera);

            return alumnoDao.saveAlumno(alumno);
        } catch (Exception e) {
            System.err.println("Error al crear alumno: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Alumno buscarAlumno(String apellidoAlumno) throws AlumnoNotFoundException {
        try {
            return alumnoDao.findAlumno(apellidoAlumno);
        } catch (Exception e) {
            System.err.println("Error al buscar alumno por apellido: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Alumno buscarAlumnoPorId(Long idAlumno) throws AlumnoNotFoundException {
        Alumno alumno = alumnoDao.findAlumnoById(idAlumno);
        if (alumno == null) {
            throw new AlumnoNotFoundException("No se encontró el alumno con ID: " + idAlumno);
        }
        return alumno;
    }

    @Override
    public Alumno modificarAlumno(Long idAlumno, AlumnoDto alumnoDto) throws AlumnoNotFoundException {
        Alumno alumno = alumnoDao.findAlumnoById(idAlumno);

        if (alumno == null) {
            throw new AlumnoNotFoundException("No se encontró el alumno con ID: " + idAlumno);
        }

        alumno.setNombre(alumnoDto.getNombre());
        alumno.setApellido(alumnoDto.getApellido());
        alumno.setDni(alumnoDto.getDni());

        Carrera carrera = carreraService.getCarrera(alumnoDto.getCarreraId());
        alumno.setCarrera(carrera);

        return alumnoDao.saveAlumno(alumno);
    }


    @Override
    public void eliminarAlumno(Long idAlumno) throws AlumnoNotFoundException {
        Alumno alumno = alumnoDao.findAlumnoById(idAlumno);
        if (alumno == null) {
            throw new AlumnoNotFoundException("No se encontró el alumno con ID: " + idAlumno);
        }
        alumnoDao.deleteAlumno(alumno);
    }

    @Override
    public List<Alumno> getAlumnosByApellido(String apellido) {
        return alumnoDao.findAll().stream()
                .filter(a -> a.getApellido().equalsIgnoreCase(apellido))
                .collect(Collectors.toList());
    }

    @Override
    public List<Alumno> getAllAlumnos() {
        return alumnoDao.findAll();
    }
}
