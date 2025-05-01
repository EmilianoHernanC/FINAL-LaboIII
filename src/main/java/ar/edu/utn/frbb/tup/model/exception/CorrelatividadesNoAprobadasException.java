package ar.edu.utn.frbb.tup.model.exception;

import java.util.List;

public class CorrelatividadesNoAprobadasException extends RuntimeException {
    private List<String> materiasNoAprobadas;

    public CorrelatividadesNoAprobadasException(String message) {
        super(message);
    }

    public CorrelatividadesNoAprobadasException(String message, List<String> materiasNoAprobadas) {
        super(message);
        this.materiasNoAprobadas = materiasNoAprobadas;
    }

    public List<String> getMateriasNoAprobadas() {
        return materiasNoAprobadas;
    }
}