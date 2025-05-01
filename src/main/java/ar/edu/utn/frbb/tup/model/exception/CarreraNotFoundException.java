package ar.edu.utn.frbb.tup.model.exception;

public class CarreraNotFoundException extends RuntimeException {
    private Long carreraId;

    public CarreraNotFoundException(String message) {
        super(message);
    }

    public CarreraNotFoundException(String message, Long carreraId) {
        super(message);
        this.carreraId = carreraId;
    }

    public CarreraNotFoundException(Long carreraId) {
        super("No se encontró la carrera con ID: " + carreraId);
        this.carreraId = carreraId;
    }

    public Long getCarreraId() {
        return carreraId;
    }
}
