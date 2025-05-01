package ar.edu.utn.frbb.tup.model.dto;
import ar.edu.utn.frbb.tup.model.Materia;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MateriaDto {
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private long profesorId;
    private Long carreraId;
    private String codigo;

    // Constructor desde entidad Materia
    public static MateriaDto fromMateria(Materia materia) {
        MateriaDto dto = new MateriaDto();
        dto.setNombre(materia.getNombre());
        dto.setAnio(materia.getAnio());
        dto.setCuatrimestre(materia.getCuatrimestre());
        dto.setCodigo(materia.getCodigo());
        if (materia.getProfesor() != null) {
            dto.setProfesorId(materia.getProfesor().getId());
        }
        if (materia.getCarrera() != null) {
            dto.setCarreraId(materia.getCarrera().getId());
        }
        if (materia.getCorrelatividades() != null) {
            List<Long> correlativasIds = materia.getCorrelatividades().stream()
                    .map(Materia::getId)
                    .collect(Collectors.toList());
            dto.setCorrelatividadesIds(correlativasIds);
        }
        return dto;
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Long carreraId) {
        this.carreraId = carreraId;
    }

    // Getters y setters existentes
    public long getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(long profesorId) {
        this.profesorId = profesorId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getCuatrimestre() {
        return cuatrimestre;
    }

    public void setCuatrimestre(int cuatrimestre) {
        this.cuatrimestre = cuatrimestre;
    }

    private List<Long> correlatividadesIds = new ArrayList<>();

    public List<Long> getCorrelatividadesIds() {
        return correlatividadesIds;
    }

    public void setCorrelatividadesIds(List<Long> correlatividadesIds) {
        this.correlatividadesIds = correlatividadesIds;
    }
}