package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.model.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateriaServiceImpl implements MateriaService {

    @Autowired
    private MateriaDao dao;

    @Autowired
    private ProfesorService profesorService;

    public void setDao(MateriaDao dao) {
        this.dao = dao;
    }

    public void setProfesorService(ProfesorService profesorService) {
        this.profesorService = profesorService;
    }

    @Override
    public Materia crearMateria(MateriaDto materiaDto) throws IllegalArgumentException {
        try {
            if (materiaDto.getNombre() == null || materiaDto.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la materia no puede estar vacío");
            }

            if (materiaDto.getAnio() <= 0) {
                throw new IllegalArgumentException("El año debe ser un número positivo");
            }

            if (materiaDto.getCuatrimestre() <= 0 || materiaDto.getCuatrimestre() > 2) {
                throw new IllegalArgumentException("El cuatrimestre debe ser 1 o 2");
            }

            Materia materia = new Materia();
            materia.setNombre(materiaDto.getNombre());
            materia.setAnio(materiaDto.getAnio());
            materia.setCuatrimestre(materiaDto.getCuatrimestre());

            if (materiaDto.getProfesorId() > 0) {
                materia.setProfesor(profesorService.buscarProfesor(materiaDto.getProfesorId()));
            }

            if (materiaDto.getCodigo() != null) {
                materia.setCodigo(materiaDto.getCodigo());
            } else {
                String codigo = materiaDto.getNombre().substring(0, Math.min(3, materiaDto.getNombre().length())).toUpperCase() +
                        String.format("%03d", materiaDto.getAnio()) +
                        materiaDto.getCuatrimestre();
                materia.setCodigo(codigo);
            }

            if (materiaDto.getCorrelatividadesIds() != null && !materiaDto.getCorrelatividadesIds().isEmpty()) {
                List<Materia> correlativas = new ArrayList<>();
                for (Long correlativaId : materiaDto.getCorrelatividadesIds()) {
                    try {
                        Materia correlativa = dao.findById(correlativaId);
                        if (correlativa == null) {
                            throw new MateriaNotFoundException("Correlativa con ID " + correlativaId + " no encontrada");
                        }
                        correlativas.add(correlativa);
                    } catch (MateriaNotFoundException e) {
                        System.out.println("Advertencia: correlativa no encontrada: " + correlativaId);
                    }
                }
                materia.setCorrelatividades(correlativas);
            }

            return dao.save(materia);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la materia", e);
        }
    }

    @Override
    public List<Materia> getAllMaterias() {
        try {
            List<Materia> materias = dao.findAll();
            return materias != null ? materias : new ArrayList<>();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener todas las materias", e);
        }
    }

    @Override
    public Materia getMateriaById(Long idMateria) throws MateriaNotFoundException {
        try {
            Materia materia = dao.findById(idMateria);
            if (materia == null) {
                throw new MateriaNotFoundException("No se encontró la materia con ID: " + idMateria);
            }
            return materia;
        } catch (MateriaNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la materia con ID: " + idMateria, e);
        }
    }

    @Override
    public List<Materia> getMateriasByNombre(String nombre) {
        try {
            if (nombre == null || nombre.trim().isEmpty()) {
                return getAllMaterias();
            }

            List<Materia> todasLasMaterias = getAllMaterias();
            return todasLasMaterias.stream()
                    .filter(m -> m.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar materias por nombre", e);
        }
    }

    @Override
    public List<Materia> getAllMateriasOrdenadas(String ordenamiento) {
        try {
            List<Materia> materias = getAllMaterias();

            if (materias.isEmpty() || ordenamiento == null) {
                return materias;
            }

            switch (ordenamiento.toLowerCase()) {
                case "nombre_asc":
                    materias.sort(Comparator.comparing(Materia::getNombre));
                    break;
                case "nombre_desc":
                    materias.sort(Comparator.comparing(Materia::getNombre).reversed());
                    break;
                case "codigo_asc":
                    materias.sort(Comparator.comparing(Materia::getCodigo));
                    break;
                case "codigo_desc":
                    materias.sort(Comparator.comparing(Materia::getCodigo).reversed());
                    break;
                default:
                    break;
            }

            return materias;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las materias ordenadas", e);
        }
    }

    @Override
    public Materia actualizarMateria(Long idMateria, MateriaDto materiaDto) throws MateriaNotFoundException, IllegalArgumentException {
        try {
            Materia materiaExistente = getMateriaById(idMateria);

            if (materiaDto.getNombre() == null || materiaDto.getNombre().trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre de la materia no puede estar vacío");
            }

            if (materiaDto.getAnio() <= 0) {
                throw new IllegalArgumentException("El año debe ser un número positivo");
            }

            if (materiaDto.getCuatrimestre() <= 0 || materiaDto.getCuatrimestre() > 2) {
                throw new IllegalArgumentException("El cuatrimestre debe ser 1 o 2");
            }

            materiaExistente.setNombre(materiaDto.getNombre());
            materiaExistente.setAnio(materiaDto.getAnio());
            materiaExistente.setCuatrimestre(materiaDto.getCuatrimestre());

            if (materiaDto.getCodigo() != null && !materiaDto.getCodigo().trim().isEmpty()) {
                materiaExistente.setCodigo(materiaDto.getCodigo());
            }

            if (materiaDto.getProfesorId() > 0) {
                materiaExistente.setProfesor(profesorService.buscarProfesor(materiaDto.getProfesorId()));
            }

            if (materiaDto.getCorrelatividadesIds() != null && !materiaDto.getCorrelatividadesIds().isEmpty()) {
                List<Materia> correlativas = new ArrayList<>();
                for (Long correlativaId : materiaDto.getCorrelatividadesIds()) {
                    try {
                        Materia correlativa = dao.findById(correlativaId);
                        if (correlativa == null) {
                            throw new MateriaNotFoundException("Correlativa con ID " + correlativaId + " no encontrada");
                        }
                        correlativas.add(correlativa);
                    } catch (MateriaNotFoundException e) {
                        System.out.println("Advertencia: correlativa no encontrada: " + correlativaId);
                    }
                }
                materiaExistente.setCorrelatividades(correlativas);
            }

            return dao.update(materiaExistente);
        } catch (MateriaNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la materia con ID: " + idMateria, e);
        }
    }

    @Override
    public void eliminarMateria(Long idMateria) throws MateriaNotFoundException {
        try {
            getMateriaById(idMateria);
            dao.delete(idMateria);
        } catch (MateriaNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la materia con ID: " + idMateria, e);
        }
    }


}

