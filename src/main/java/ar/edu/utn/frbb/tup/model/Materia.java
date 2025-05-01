package ar.edu.utn.frbb.tup.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Materia {
    // Atributos
    private Long id; // Cambiado a Long para consistencia
    private String nombre;
    private int anio;
    private int cuatrimestre;
    private String codigo;
    private Profesor profesor;
    private List<Materia> correlatividades;

    @JsonBackReference
    private Carrera carrera;

    // Constructores
    public Materia() {
        correlatividades = new ArrayList<>();
    }

    public Materia(String nombre, int anio, int cuatrimestre, Profesor profesor) {
        this.nombre = nombre;
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.profesor = profesor;
        correlatividades = new ArrayList<>();
    }

    // Constructor con ID
    public Materia(Long id, String nombre, int anio, int cuatrimestre, Profesor profesor) {
        this.id = id;
        this.nombre = nombre;
        this.anio = anio;
        this.cuatrimestre = cuatrimestre;
        this.profesor = profesor;
        correlatividades = new ArrayList<>();
    }

    // Métodos de negocio
    public void agregarCorrelatividad(Materia m) {
        this.correlatividades.add(m);
    }

    public void validar() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacío");
        }
        if (anio <= 0) {
            throw new IllegalArgumentException("El año debe ser un número positivo");
        }
        if (cuatrimestre <= 0 || cuatrimestre > 2) {
            throw new IllegalArgumentException("El cuatrimestre debe ser 1 o 2");
        }
    }

    // Getters y Setters actualizados
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Para mantener compatibilidad con código existente
    public Long getMateriaId() {
        return id;
    }

    public void setMateriaId(Long materiaId) {
        this.id = materiaId;
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

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public List<Materia> getCorrelatividades() {
        return this.correlatividades;
    }

    public void setCorrelatividades(List<Materia> correlatividades) {
        this.correlatividades = correlatividades;
    }

    // Métodos equals y hashCode actualizados
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Materia materia = (Materia) o;
        return anio == materia.anio
                && cuatrimestre == materia.cuatrimestre
                && Objects.equals(id, materia.id)
                && Objects.equals(nombre, materia.nombre)
                && Objects.equals(codigo, materia.codigo)
                && Objects.equals(profesor, materia.profesor)
                && Objects.equals(carrera, materia.carrera);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, codigo, anio, cuatrimestre, profesor, carrera);
    }

    @Override
    public String toString() {
        return "Materia{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", anio=" + anio +
                ", cuatrimestre=" + cuatrimestre +
                ", carrera=" + (carrera != null ? carrera.getNombre() : "sin carrera") +
                '}';
    }
}