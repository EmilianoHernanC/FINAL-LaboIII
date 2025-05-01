package ar.edu.utn.frbb.tup.model.exception;

public class AsignaturaInexistenteException extends RuntimeException {
    private Long asignaturaId;

    public AsignaturaInexistenteException(String message) {
        super(message);
    }

    public AsignaturaInexistenteException(String message, Long asignaturaId) {
        super(message);
        this.asignaturaId = asignaturaId;
    }

    public Long getAsignaturaId() {
        return asignaturaId;
    }
}
