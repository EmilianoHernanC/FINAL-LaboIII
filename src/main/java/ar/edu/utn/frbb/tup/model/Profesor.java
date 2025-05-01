package ar.edu.utn.frbb.tup.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Profesor {
    private Long id; // Cambiado a Long (objeto) para consistencia
    private String nombre;
    private String apellido;
    private String titulo;
    private List<Materia> materiasDictadas;

    public Profesor() {
        this.materiasDictadas = new ArrayList<>();
    }

    public Profesor(String nombre, String apellido, String titulo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.titulo = titulo;
        this.materiasDictadas = new ArrayList<>();
    }

    // Constructor con ID
    public Profesor(Long id, String nombre, String apellido, String titulo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.titulo = titulo;
        this.materiasDictadas = new ArrayList<>();
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Materia> getMateriasDictadas() {
        return materiasDictadas;
    }

    public void setMateriasDictadas(List<Materia> materiasDictadas) {
        this.materiasDictadas = materiasDictadas;
    }

    // Métodos de negocio
    public void agregarMateria(Materia materia) {
        this.materiasDictadas.add(materia);
        // Establecemos la relación bidireccional
        materia.setProfesor(this);
    }

    public void removerMateria(Materia materia) {
        if (this.materiasDictadas.remove(materia)) {
            // Si se removió correctamente, anulamos la relación
            materia.setProfesor(null);
        }
    }

    public Materia buscarMateriaPorId(Long materiaId) {
        return this.materiasDictadas.stream()
                .filter(m -> m.getId().equals(materiaId))
                .findFirst()
                .orElse(null);
    }

    public Materia buscarMateriaPorNombre(String nombre) {
        return this.materiasDictadas.stream()
                .filter(m -> m.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Profesor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", titulo='" + titulo + '\'' +
                ", materiasDictadas=" + materiasDictadas.size() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profesor profesor = (Profesor) o;
        return Objects.equals(id, profesor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}