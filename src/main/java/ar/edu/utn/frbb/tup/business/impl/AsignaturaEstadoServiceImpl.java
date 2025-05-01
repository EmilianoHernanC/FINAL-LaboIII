package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaEstadoService;
import ar.edu.utn.frbb.tup.model.Alumno;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.exception.AlumnoNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AlumnoDao;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AsignaturaEstadoServiceImpl implements AsignaturaEstadoService {

    private final AsignaturaDao asignaturaDao;
    private final AlumnoDao alumnoDao;

    @Autowired
    public AsignaturaEstadoServiceImpl(AsignaturaDao asignaturaDao, AlumnoDao alumnoDao) {
        this.asignaturaDao = asignaturaDao;
        this.alumnoDao = alumnoDao;
    }

    @Override
    public Asignatura getAsignatura(Long materiaId, Long dni) throws AsignaturaInexistenteException {
        try {
            Alumno alumno = alumnoDao.loadAlumno(dni);

            for (Asignatura asignatura : alumno.getAsignaturas()) {
                if (asignatura.getMateria().getMateriaId().equals(materiaId)) {
                    return asignatura;
                }
            }

            throw new AsignaturaInexistenteException("El alumno no tiene una asignatura para la materia con ID: " + materiaId);
        } catch (AlumnoNotFoundException e) {
            throw new AsignaturaInexistenteException("No existe el alumno con DNI: " + dni);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar asignatura por materiaId y dni: " + e.getMessage(), e);
        }
    }

    @Override
    public Asignatura getAsignatura(Long asignaturaId) throws AsignaturaInexistenteException {
        try {
            return asignaturaDao.findById(asignaturaId)
                    .orElseThrow(() -> new AsignaturaInexistenteException("No existe asignatura con ID: " + asignaturaId, asignaturaId));
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar asignatura con ID: " + asignaturaId, e);
        }
    }

    @Override
    public void cursarAsignatura(Long asignaturaId) throws AsignaturaInexistenteException, EstadoIncorrectoException {
        try {
            Asignatura asignatura = getAsignatura(asignaturaId);

            if (!asignatura.puedeSerCursada()) {
                throw new EstadoIncorrectoException("La asignatura no está en estado NO_CURSADA");
            }

            asignatura.cursarAsignatura();
            asignaturaDao.update(asignatura);
        } catch (Exception e) {
            throw new RuntimeException("Error al cursar asignatura: " + e.getMessage(), e);
        }
    }

    @Override
    public void aprobarAsignatura(Long asignaturaId, int nota) throws AsignaturaInexistenteException, EstadoIncorrectoException {
        try {
            Asignatura asignatura = getAsignatura(asignaturaId);

            asignatura.aprobarAsignatura(nota);
            asignaturaDao.update(asignatura);
        } catch (Exception e) {
            throw new RuntimeException("Error al aprobar asignatura: " + e.getMessage(), e);
        }
    }

    @Override
    public void perderRegularidad(Long asignaturaId) throws AsignaturaInexistenteException, EstadoIncorrectoException {
        try {
            Asignatura asignatura = getAsignatura(asignaturaId);

            asignatura.perderRegularidad();
            asignaturaDao.update(asignatura);
        } catch (Exception e) {
            throw new RuntimeException("Error al perder regularidad: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarAsignatura(Asignatura asignatura) throws AsignaturaInexistenteException {
        try {
            asignaturaDao.update(asignatura);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la asignatura: " + e.getMessage(), e);
        }
    }

    @Override
    public EstadoAsignatura getEstadoAsignatura(Long asignaturaId) throws AsignaturaInexistenteException {
        try {
            return getAsignatura(asignaturaId).getEstado();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el estado de la asignatura: " + e.getMessage(), e);
        }
    }
}
