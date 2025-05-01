package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.AsignaturaService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.model.EstadoAsignatura;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaDto;
import ar.edu.utn.frbb.tup.model.dto.AsignaturaEstadoDto;
import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AsignaturaServiceImpl implements AsignaturaService {

    @Autowired
    private AsignaturaDao asignaturaDao;

    @Autowired
    private MateriaDao materiaDao;

    @Autowired
    private MateriaService materiaService;

    @Override
    public Asignatura crearAsignatura(AsignaturaDto asignaturaDto) {
        try {
            Asignatura asignatura = new Asignatura();
            asignatura.setAlumnoId(asignaturaDto.getAlumnoId());
            asignatura.setMateria(materiaService.getMateriaById(asignaturaDto.getMateriaId()));
            asignatura.setEstado(EstadoAsignatura.NO_CURSADA);
            return asignaturaDao.save(asignatura);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la asignatura", e);
        }
    }

    @Override
    public Asignatura getAsignaturaById(Long id) throws AsignaturaInexistenteException {
        try {
            return asignaturaDao.findById(id)
                    .orElseThrow(() -> new AsignaturaInexistenteException("Asignatura no encontrada con ID: " + id, id));
        } catch (AsignaturaInexistenteException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la asignatura con ID: " + id, e);
        }
    }

    @Override
    public Asignatura modificarEstadoAsignatura(Long id, AsignaturaEstadoDto estadoDto)
            throws EstadoIncorrectoException, AsignaturaInexistenteException {
        try {
            Asignatura asignatura = getAsignaturaById(id);

            if (estadoDto.getEstado() != null) {
                asignatura.setEstado(estadoDto.getEstado());
            }

            if (estadoDto.getNota() != null) {
                if (asignatura.getEstado() == EstadoAsignatura.APROBADA) {
                    asignatura.setNota(estadoDto.getNota());
                } else {
                    throw new EstadoIncorrectoException("Solo puedes asignar nota a una asignatura APROBADA.");
                }
            }

            return asignaturaDao.update(asignatura);
        } catch (EstadoIncorrectoException | AsignaturaInexistenteException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al modificar el estado de la asignatura con ID: " + id, e);
        }
    }

    @Override
    public void eliminarAsignatura(Long id) throws AsignaturaInexistenteException {
        try {
            getAsignaturaById(id); // lanza excepción si no existe
            asignaturaDao.delete(id);
        } catch (AsignaturaInexistenteException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la asignatura con ID: " + id, e);
        }
    }

    @Override
    public List<Asignatura> getAllAsignaturas() {
        try {
            return asignaturaDao.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la lista de asignaturas", e);
        }
    }

    public AsignaturaServiceImpl(AsignaturaDao asignaturaDao, MateriaService materiaService) {
        this.asignaturaDao = asignaturaDao;
        this.materiaService = materiaService;
    }
}
