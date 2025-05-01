package ar.edu.utn.frbb.tup.model.exception;

public class MateriaNotFoundException extends RuntimeException {
    private Long materiaId;

    public MateriaNotFoundException(String message) {
        super(message);
    }

    public MateriaNotFoundException(String message, Long materiaId) {
        super(message);
        this.materiaId = materiaId;
    }

    public MateriaNotFoundException(int materiaId) {
        super("No se encontró la materia con ID: " + materiaId);
        this.materiaId = Long.valueOf(materiaId);
    }

    public Long getMateriaId() {
        return materiaId;
    }
}