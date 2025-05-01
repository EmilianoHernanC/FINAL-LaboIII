package ar.edu.utn.frbb.tup.model.exception;

public class AlumnoNotFoundException extends RuntimeException {
    private Long alumnoId;

    public AlumnoNotFoundException(String message) {
        super(message);
    }

    public AlumnoNotFoundException(String message, Long alumnoId) {
        super(message);
        this.alumnoId = alumnoId;
    }

    public AlumnoNotFoundException(Long alumnoId) {
        super("No se encontró el alumno con ID: " + alumnoId);
        this.alumnoId = alumnoId;
    }

    public Long getAlumnoId() {
        return alumnoId;
    }
}