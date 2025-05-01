package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.AsignaturaInexistenteException;
import ar.edu.utn.frbb.tup.model.exception.CorrelatividadException;
import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;

import java.util.ArrayList;
import java.util.List;

public class Alumno {
    private Long id;
    private String nombre;
    private String apellido;
    private Long dni;
    private List<Asignatura> asignaturas;
    private Carrera carrera;

    public Alumno() {
        this.asignaturas = new ArrayList<>();
    }

    public Alumno(String nombre, String apellido, Long dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.asignaturas = new ArrayList<>();
    }

    public Alumno(Long id, String nombre, String apellido, Long dni) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.asignaturas = new ArrayList<>();
    }

    // ✅ Nuevo constructor completo para facilitar los tests
    public Alumno(Long id, String nombre, String apellido, Long dni, List<Asignatura> asignaturas, Carrera carrera) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.asignaturas = asignaturas;
        this.carrera = carrera;
    }

    // Getters y setters
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

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public List<Asignatura> getAsignaturas() {
        return this.asignaturas;
    }

    public void agregarAsignatura(Asignatura asignatura) {
        asignatura.setAlumnoId(this.id);
        this.asignaturas.add(asignatura);
    }

    public Asignatura buscarAsignaturaPorId(Long asignaturaId) throws AsignaturaInexistenteException {
        return asignaturas.stream()
                .filter(a -> a.getAsignaturaId().equals(asignaturaId))
                .findFirst()
                .orElseThrow(() -> new AsignaturaInexistenteException("Asignatura con ID " + asignaturaId + " no encontrada"));
    }

    public Asignatura buscarAsignaturaPorMateria(Materia materia) throws AsignaturaInexistenteException {
        return asignaturas.stream()
                .filter(a -> a.getMateria().getId().equals(materia.getId()))
                .findFirst()
                .orElseThrow(() -> new AsignaturaInexistenteException("No se encontró la asignatura para la materia " + materia.getNombre()));
    }

    public void aprobarAsignatura(Long asignaturaId, int nota)
            throws EstadoIncorrectoException, CorrelatividadException, AsignaturaInexistenteException {
        Asignatura asignaturaAAprobar = buscarAsignaturaPorId(asignaturaId);

        for (Materia correlativa : asignaturaAAprobar.getMateria().getCorrelatividades()) {
            chequearCorrelatividad(correlativa);
        }

        asignaturaAAprobar.aprobarAsignatura(nota);
    }

    public void aprobarAsignatura(Materia materia, int nota)
            throws EstadoIncorrectoException, CorrelatividadException, AsignaturaInexistenteException {
        Asignatura asignaturaAAprobar = buscarAsignaturaPorMateria(materia);

        for (Materia correlativa : materia.getCorrelatividades()) {
            chequearCorrelatividad(correlativa);
        }

        asignaturaAAprobar.aprobarAsignatura(nota);
    }

    private void chequearCorrelatividad(Materia correlativa) throws CorrelatividadException {
        boolean correlativaAprobada = asignaturas.stream()
                .anyMatch(a -> correlativa.getId().equals(a.getMateria().getId()) && a.estaAprobada());

        if (!correlativaAprobada) {
            throw new CorrelatividadException("La materia " + correlativa.getNombre() + " es correlativa y no está aprobada");
        }
    }

    public boolean puedeAprobar(Asignatura asignatura) {
        try {
            for (Materia correlativa : asignatura.getMateria().getCorrelatividades()) {
                chequearCorrelatividad(correlativa);
            }
            return true;
        } catch (CorrelatividadException e) {
            return false;
        }
    }

    public void cursarAsignatura(Long asignaturaId) throws AsignaturaInexistenteException {
        Asignatura asignatura = buscarAsignaturaPorId(asignaturaId);
        asignatura.cursarAsignatura();
    }

    public void perderRegularidadAsignatura(Long asignaturaId)
            throws AsignaturaInexistenteException, EstadoIncorrectoException {
        Asignatura asignatura = buscarAsignaturaPorId(asignaturaId);
        asignatura.perderRegularidad();
    }

    public void actualizarAsignatura(Asignatura asignatura) {
        asignaturas.stream()
                .filter(a -> a.getAsignaturaId().equals(asignatura.getAsignaturaId()))
                .findFirst()
                .ifPresent(a -> {
                    a.setEstado(asignatura.getEstado());
                    if (asignatura.getNota() != null) {
                        a.setNota(asignatura.getNota());
                    }
                });
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni=" + dni +
                ", asignaturas=" + asignaturas.size() +
                '}';
    }
}
