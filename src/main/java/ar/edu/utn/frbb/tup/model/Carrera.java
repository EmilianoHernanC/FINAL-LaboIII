package ar.edu.utn.frbb.tup.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

public class Carrera {
    private Long id; // ID de tipo Long
    private String nombre;
    private String codigo;
    private int cantidadCuatrimestres;
    private int departamento;

    //Para que no entre en bucle infinito en JSON
    @JsonManagedReference
    private List<Materia> materiasList;

    // Constructor vacío
    public Carrera() {
        this.materiasList = new ArrayList<>();
    }

    // Constructor básico
    public Carrera(String nombre, int cantidadCuatrimestres) {
        this.nombre = nombre;
        this.cantidadCuatrimestres = cantidadCuatrimestres;
        this.materiasList = new ArrayList<>();
    }

    public Carrera(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.materiasList = new ArrayList<>();
    }

    // Constructor completo
    public Carrera(Long id, String nombre, String codigo, int cantidadCuatrimestres, int departamento) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.cantidadCuatrimestres = cantidadCuatrimestres;
        this.departamento = departamento;
        this.materiasList = new ArrayList<>();
    }

    public Carrera(String nombre, int codigo, int departamento, int cantidadCuatrimestres) {
        this.nombre = nombre;
        this.codigo = String.valueOf(codigo);
        this.departamento = departamento;
        this.cantidadCuatrimestres = cantidadCuatrimestres;
        this.materiasList = new ArrayList<>();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getCantidadCuatrimestres() {
        return cantidadCuatrimestres;
    }

    public void setCantidadCuatrimestres(int cantidadCuatrimestres) {
        this.cantidadCuatrimestres = cantidadCuatrimestres;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public List<Materia> getMateriasList() {
        return materiasList;
    }

    public void setMateriasList(List<Materia> materiasList) {
        this.materiasList = materiasList;
    }

    // Métodos de negocio
    public void agregarMateria(Materia materia) {
        this.materiasList.add(materia);
        materia.setCarrera(this); // Relación bidireccional
    }

    public void removerMateria(Materia materia) {
        if (this.materiasList.remove(materia)) {
            materia.setCarrera(null);
        }
    }

    public boolean tieneMateria(Materia materia) {
        return this.materiasList.contains(materia);
    }

    public Materia buscarMateriaPorId(Long materiaId) {
        return this.materiasList.stream()
                .filter(m -> m.getId().equals(materiaId))
                .findFirst()
                .orElse(null);
    }

    public Materia buscarMateriaPorNombre(String nombre) {
        return this.materiasList.stream()
                .filter(m -> m.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Carrera{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", cantidadCuatrimestres=" + cantidadCuatrimestres +
                ", departamento=" + departamento +
                ", materias=" + materiasList.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carrera carrera = (Carrera) o;
        return Objects.equals(id, carrera.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
