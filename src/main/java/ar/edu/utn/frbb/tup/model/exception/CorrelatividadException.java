package ar.edu.utn.frbb.tup.model.exception;

public class CorrelatividadException extends RuntimeException {
    private String materiaActual;
    private String materiaCorrelativa;

    public CorrelatividadException(String message) {
        super(message);
    }

    public CorrelatividadException(String message, String materiaActual, String materiaCorrelativa) {
        super(message);
        this.materiaActual = materiaActual;
        this.materiaCorrelativa = materiaCorrelativa;
    }

    public String getMateriaActual() {
        return materiaActual;
    }

    public String getMateriaCorrelativa() {
        return materiaCorrelativa;
    }
}