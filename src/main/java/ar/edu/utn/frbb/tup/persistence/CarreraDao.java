package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import java.util.List;

public interface CarreraDao {

    /**
     * Guarda una nueva carrera
     * @param carrera La carrera a guardar
     * @return La carrera guardada con su ID generado
     */
    Carrera saveCarrera(Carrera carrera);

    /**
     * Busca una carrera por su ID
     * @param id El ID de la carrera a buscar
     * @return La carrera encontrada o null si no existe
     */
    Carrera findById(Long id);

    /**
     * Obtiene todas las carreras
     * @return Lista de todas las carreras
     */
    List<Carrera> findAll();

    /**
     * Actualiza una carrera existente
     * @param carrera La carrera con los datos actualizados
     * @return La carrera actualizada
     */
    Carrera updateCarrera(Carrera carrera);

    /**
     * Elimina una carrera por su ID
     * @param id El ID de la carrera a eliminar
     */
    void deleteCarrera(Long id);
}