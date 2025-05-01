package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.exception.EstadoIncorrectoException;

public class Asignatura {
    private Long asignaturaId;
    private Materia materia;
    private EstadoAsignatura estado;
    private Integer nota;
    private Long alumnoId;

    // Constructores
    public Asignatura() {
        this.estado = EstadoAsignatura.NO_CURSADA;
    }

    public Asignatura(Materia materia) {
        this.materia = materia;
        this.estado = EstadoAsignatura.NO_CURSADA;
    }

    public Asignatura(Long asignaturaId, Materia materia) {
        this.asignaturaId = asignaturaId;
        this.materia = materia;
        this.estado = EstadoAsignatura.NO_CURSADA;
    }

    public Asignatura(Long asignaturaId, Materia materia, Long alumnoId) {
        this.asignaturaId = asignaturaId;
        this.materia = materia;
        this.alumnoId = alumnoId;
        this.estado = EstadoAsignatura.NO_CURSADA;
    }

    // Getters y Setters
    public Long getAsignaturaId() {
        return asignaturaId;
    }

    public void setAsignaturaId(Long asignaturaId) {
        this.asignaturaId = asignaturaId;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }

    public void setAlumnoId(Long alumnoId) {
        this.alumnoId = alumnoId;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public void borrarNota() {
        this.nota = null;
    }

    public EstadoAsignatura getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsignatura estado) {
        this.estado = estado;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public String getNombreAsignatura() {
        return (this.materia != null) ? this.materia.getNombre() : "Sin nombre";
    }

    // Métodos de negocio
    public void cursarAsignatura() {
        this.estado = EstadoAsignatura.CURSADA;
    }

    public void aprobarAsignatura(int nota) throws EstadoIncorrectoException {
        if (!this.estado.equals(EstadoAsignatura.CURSADA)) {
            throw new EstadoIncorrectoException("La materia debe estar cursada para poder aprobarla");
        }
        if (nota >= 4) {
            this.estado = EstadoAsignatura.APROBADA;
            this.nota = nota;
        } else {
            throw new EstadoIncorrectoException("La nota debe ser mayor o igual a 4 para aprobar");
        }
    }

    public void perderRegularidad() throws EstadoIncorrectoException {
        if (this.estado.equals(EstadoAsignatura.NO_CURSADA)) {
            throw new EstadoIncorrectoException("No se puede perder la regularidad de una materia no cursada");
        }
        if (this.estado.equals(EstadoAsignatura.APROBADA)) {
            throw new EstadoIncorrectoException("No se puede perder la regularidad de una materia aprobada");
        }
        this.estado = EstadoAsignatura.NO_CURSADA;
        this.nota = null;
    }

    public boolean estaAprobada() {
        return EstadoAsignatura.APROBADA.equals(this.estado);
    }

    public boolean estaCursada() {
        return EstadoAsignatura.CURSADA.equals(this.estado);
    }

    public boolean puedeSerCursada() {
        return EstadoAsignatura.NO_CURSADA.equals(this.estado);
    }

    @Override
    public String toString() {
        return "Asignatura{" +
                "asignaturaId=" + asignaturaId +
                ", materia=" + (materia != null ? materia.getNombre() : "sin materia") +
                ", estado=" + estado +
                ", nota=" + (nota != null ? nota : "sin nota") +
                ", alumnoId=" + (alumnoId != null ? alumnoId : "sin alumno") +
                '}';
    }
}
