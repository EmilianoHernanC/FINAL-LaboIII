package ar.edu.utn.frbb.tup.model.exception;

import ar.edu.utn.frbb.tup.model.EstadoAsignatura;

public class EstadoIncorrectoException extends RuntimeException {
    private EstadoAsignatura estadoActual;
    private EstadoAsignatura estadoEsperado;

    public EstadoIncorrectoException(String message) {
        super(message);
    }

    public EstadoIncorrectoException(String message, EstadoAsignatura estadoActual, EstadoAsignatura estadoEsperado) {
        super(message);
        this.estadoActual = estadoActual;
        this.estadoEsperado = estadoEsperado;
    }

    public EstadoAsignatura getEstadoActual() {
        return estadoActual;
    }

    public EstadoAsignatura getEstadoEsperado() {
        return estadoEsperado;
    }
}