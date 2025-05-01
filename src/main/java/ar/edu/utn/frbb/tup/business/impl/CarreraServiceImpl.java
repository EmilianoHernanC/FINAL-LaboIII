package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.model.exception.MateriaNotFoundException;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarreraServiceImpl implements CarreraService {

    @Autowired
    private CarreraDao carreraDao;

    @Autowired
    private MateriaService materiaService;

    @Override
    public Carrera crearCarrera(CarreraDto carreraDto) {
        try {
            Carrera carrera = new Carrera();
            carrera.setNombre(carreraDto.getNombre());
            carrera.setCodigo(carreraDto.getCodigo());
            carrera.setCantidadCuatrimestres(carreraDto.getCantidadCuatrimestres());
            carrera.setDepartamento(carreraDto.getDepartamento());

            if (carreraDto.getMateriasIds() != null && !carreraDto.getMateriasIds().isEmpty()) {
                List<Materia> materias = new ArrayList<>();
                for (Long materiaId : carreraDto.getMateriasIds()) {
                    try {
                        Materia materia = materiaService.getMateriaById(materiaId);
                        materias.add(materia);
                    } catch (MateriaNotFoundException e) {
                        System.out.println("Advertencia: No se encontró la materia con ID: " + materiaId);
                    }
                }
                carrera.setMateriasList(materias);
            }

            return carreraDao.saveCarrera(carrera);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la carrera", e);
        }
    }

    @Override
    public Carrera getCarrera(Long id) throws CarreraNotFoundException {
        try {
            Carrera carrera = carreraDao.findById(id);
            if (carrera == null) {
                throw new CarreraNotFoundException("No se encontró la carrera con ID: " + id, id);
            }
            return carrera;
        } catch (CarreraNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la carrera con ID: " + id, e);
        }
    }

    @Override
    public List<Carrera> getAllCarreras() {
        try {
            return carreraDao.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todas las carreras", e);
        }
    }

    @Override
    public Carrera actualizarCarrera(Long id, CarreraDto dto) throws CarreraNotFoundException {
        try {
            Carrera carrera = getCarrera(id);

            carrera.setNombre(dto.getNombre());
            carrera.setCodigo(dto.getCodigo());
            carrera.setCantidadCuatrimestres(dto.getCantidadCuatrimestres());
            carrera.setDepartamento(dto.getDepartamento());

            if (dto.getMateriasIds() != null) {
                List<Materia> materias = new ArrayList<>();
                for (Long materiaId : dto.getMateriasIds()) {
                    try {
                        Materia materia = materiaService.getMateriaById(materiaId);
                        materias.add(materia);
                    } catch (MateriaNotFoundException e) {
                        System.out.println("Advertencia: No se encontró la materia con ID: " + materiaId);
                    }
                }
                carrera.setMateriasList(materias);
            }

            return carreraDao.updateCarrera(carrera);
        } catch (CarreraNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la carrera con ID: " + id, e);
        }
    }

    @Override
    public List<Carrera> getCarrerasByNombre(String nombre) {
        try {
            List<Carrera> todasLasCarreras = carreraDao.findAll();
            if (nombre == null || nombre.trim().isEmpty()) {
                return todasLasCarreras;
            }
            return todasLasCarreras.stream()
                    .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar carreras por nombre", e);
        }
    }

    @Override
    public void eliminarCarrera(Long id) throws CarreraNotFoundException {
        try {
            Carrera carrera = getCarrera(id);
            carreraDao.deleteCarrera(id);
        } catch (CarreraNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la carrera con ID: " + id, e);
        }
    }

    public CarreraServiceImpl(CarreraDao carreraDao, MateriaService materiaService) {
        this.carreraDao = carreraDao;
        this.materiaService = materiaService;
    }
}
