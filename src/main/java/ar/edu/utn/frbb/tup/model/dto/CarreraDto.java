package ar.edu.utn.frbb.tup.model.dto;

import java.util.ArrayList;
import java.util.List;

public class CarreraDto {
    private Long id;
    private String nombre;
    private String codigo;
    private int departamento;
    private int cantidadCuatrimestres;
    private List<Long> materiasIds;

    // Constructor vacío
    public CarreraDto() {
        this.materiasIds = new ArrayList<>();
    }

    // Getters y Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public int getCantidadCuatrimestres() {
        return cantidadCuatrimestres;
    }

    public void setCantidadCuatrimestres(int cantidadCuatrimestres) {
        this.cantidadCuatrimestres = cantidadCuatrimestres;
    }

    public List<Long> getMateriasIds() {
        return materiasIds;
    }

    public void setMateriasIds(List<Long> materiasIds) {
        this.materiasIds = materiasIds;
    }


    @Override
    public String toString() {
        return "CarreraDto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", departamento=" + departamento +
                ", cantidadCuatrimestres=" + cantidadCuatrimestres +
                ", materiasIds=" + materiasIds +
                '}';
    }
}