package ar.edu.utn.frbb.tup.business;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.exception.CarreraNotFoundException;

import java.util.List;

public interface CarreraService {

    /**
     * Crea una nueva carrera a partir de un DTO
     * @param carreraDto Datos de la carrera a crear
     * @return La carrera creada
     */
    Carrera crearCarrera(CarreraDto carreraDto);

    /**
     * Obtiene una carrera por su ID
     * @param id Identificador de la carrera
     * @return La carrera encontrada
     * @throws CarreraNotFoundException Si no existe la carrera con ese ID
     */
    Carrera getCarrera(Long id) throws CarreraNotFoundException;

    /**
     * Obtiene todas las carreras
     * @return Lista de todas las carreras
     */
    List<Carrera> getAllCarreras();

    /**
     * Actualiza una carrera existente
     * @param id Identificador de la carrera a actualizar
     * @param carreraDto Datos actualizados de la carrera
     * @return La carrera actualizada
     * @throws CarreraNotFoundException Si no existe la carrera con ese ID
     */
    Carrera actualizarCarrera(Long id, CarreraDto carreraDto) throws CarreraNotFoundException;

    List<Carrera> getCarrerasByNombre(String nombre);

    /**
     * Elimina una carrera por su ID
     * @param id Identificador de la carrera a eliminar
     * @throws CarreraNotFoundException Si no existe la carrera con ese ID
     */
    void eliminarCarrera(Long id) throws CarreraNotFoundException;
}