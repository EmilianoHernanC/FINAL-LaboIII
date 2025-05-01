package ar.edu.utn.frbb.tup.model.dto;

import java.util.ArrayList;
import java.util.List;

public class ProfesorDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String titulo;
    private List<Long> materiasDictadasIds = new ArrayList<>();

    public ProfesorDto() {
    }

    // Getters y setters

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

    public List<Long> getMateriasDictadasIds() {
        return materiasDictadasIds;
    }

    public void setMateriasDictadasIds(List<Long> materiasDictadasIds) {
        this.materiasDictadasIds = materiasDictadasIds;
    }

    @Override
    public String toString() {
        return "ProfesorDto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", titulo='" + titulo + '\'' +
                ", materiasDictadasIds=" + materiasDictadasIds +
                '}';
    }
}
