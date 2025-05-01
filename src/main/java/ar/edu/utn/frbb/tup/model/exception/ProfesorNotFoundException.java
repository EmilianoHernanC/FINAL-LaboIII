package ar.edu.utn.frbb.tup.model.exception;

public class ProfesorNotFoundException extends RuntimeException {
    private Long profesorId;

    public ProfesorNotFoundException(String message) {
        super(message);
    }

    public ProfesorNotFoundException(String message, Long profesorId) {
        super(message);
        this.profesorId = profesorId;
    }

    public Long getProfesorId() {
        return profesorId;
    }
}

