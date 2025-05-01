package ar.edu.utn.frbb.tup.model.dto;

public class AlumnoDto {
    private String nombre;
    private String apellido;
    private Long dni;
    private Long carreraId;

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

    public Long getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Long carreraId) {
        this.carreraId = carreraId;
    }

    public AlumnoDto(String nombre, String apellido, Long dni, Long carreraId) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.carreraId = carreraId;
    }
}
