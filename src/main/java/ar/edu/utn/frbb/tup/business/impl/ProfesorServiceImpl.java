package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.model.dto.ProfesorDto;
import ar.edu.utn.frbb.tup.model.exception.ProfesorNotFoundException;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfesorServiceImpl implements ProfesorService {

    @Autowired
    private ProfesorDao profesorDao;

    @Autowired
    private MateriaDao materiaDao;

    public void setProfesorDao(ProfesorDao profesorDao) {
        this.profesorDao = profesorDao;
    }

    public void setMateriaDao(MateriaDao materiaDao) {
        this.materiaDao = materiaDao;
    }

    @Override
    public Profesor crearProfesor(ProfesorDto profesorDto) {
        try {
            Profesor profesor = new Profesor();
            profesor.setNombre(profesorDto.getNombre());
            profesor.setApellido(profesorDto.getApellido());
            profesor.setTitulo(profesorDto.getTitulo());

            if (profesorDto.getMateriasDictadasIds() != null) {
                List<Materia> materias = new ArrayList<>();
                for (Long materiaId : profesorDto.getMateriasDictadasIds()) {
                    Materia materia = materiaDao.findById(materiaId);
                    if (materia != null) {
                        materias.add(materia);
                    }
                }
                profesor.setMateriasDictadas(materias);
            }

            return profesorDao.saveProfesor(profesor);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el profesor", e);
        }
    }

    @Override
    public Profesor getProfesorById(Long id) throws ProfesorNotFoundException {
        try {
            Profesor profesor = profesorDao.findById(id);
            if (profesor == null) {
                throw new ProfesorNotFoundException("No se encontró el profesor con ID: " + id, id);
            }
            return profesor;
        } catch (ProfesorNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el profesor con ID: " + id, e);
        }
    }

    @Override
    public List<Profesor> getAllProfesores() {
        try {
            return profesorDao.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todos los profesores", e);
        }
    }

    @Override
    public Profesor actualizarProfesor(Long id, ProfesorDto profesorDto) throws ProfesorNotFoundException {
        try {
            Profesor profesorExistente = getProfesorById(id);
            profesorExistente.setNombre(profesorDto.getNombre());
            profesorExistente.setApellido(profesorDto.getApellido());
            profesorExistente.setTitulo(profesorDto.getTitulo());

            if (profesorDto.getMateriasDictadasIds() != null) {
                List<Materia> materias = new ArrayList<>();
                for (Long materiaId : profesorDto.getMateriasDictadasIds()) {
                    Materia materia = materiaDao.findById(materiaId);
                    if (materia != null) {
                        materias.add(materia);
                    }
                }
                profesorExistente.setMateriasDictadas(materias);
            }

            return profesorDao.saveProfesor(profesorExistente);
        } catch (ProfesorNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el profesor con ID: " + id, e);
        }
    }

    @Override
    public void eliminarProfesor(Long id) throws ProfesorNotFoundException {
        try {
            getProfesorById(id);
            profesorDao.deleteProfesor(id);
        } catch (ProfesorNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el profesor con ID: " + id, e);
        }
    }

    @Override
    public Profesor buscarProfesor(Long id) throws ProfesorNotFoundException {
        try {
            Profesor profesor = profesorDao.findById(id);
            if (profesor == null) {
                throw new ProfesorNotFoundException("No se encontró el profesor con ID: " + id, id);
            }
            return profesor;
        } catch (ProfesorNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el profesor con ID: " + id, e);
        }
    }
}

