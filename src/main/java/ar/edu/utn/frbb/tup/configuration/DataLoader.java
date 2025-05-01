package ar.edu.utn.frbb.tup.configuration;

import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.persistence.*;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader {

    @Autowired
    private MateriaDao materiaDao;

    @Autowired
    private CarreraDao carreraDao;

    @Autowired
    private ProfesorDao profesorDao;

    @Autowired
    private AlumnoDao alumnoDao;

    @Autowired
    private AsignaturaDao asignaturaDao;

    @PostConstruct
    public void init() {
        System.out.println("\uD83D\uDD04 Cargando datos iniciales...");

        // Profesores
        Profesor prof1 = new Profesor(1L, "Carla", "Rodríguez", "Ing. en Sistemas");
        Profesor prof2 = new Profesor(2L, "Luciano", "Martínez", "Lic. en Matemática");
        Profesor prof3 = new Profesor(3L, "Julia", "Fernández", "Lic. en Computación");

        profesorDao.saveProfesor(prof1);
        profesorDao.saveProfesor(prof2);
        profesorDao.saveProfesor(prof3);

        // Materias
        Materia algebra = new Materia("Álgebra", 1, 1, prof2);
        Materia analisis = new Materia("Análisis Matemático I", 1, 1, prof2);
        Materia prog1 = new Materia("Programación I", 1, 1, prof1);
        Materia prog2 = new Materia("Programación II", 1, 2, prof1);
        prog2.agregarCorrelatividad(prog1);
        Materia logica = new Materia("Lógica", 1, 2, prof3);
        Materia edatos = new Materia("Estructura de Datos", 2, 1, prof1);
        edatos.agregarCorrelatividad(prog1);

        materiaDao.save(algebra);
        materiaDao.save(analisis);
        materiaDao.save(prog1);
        materiaDao.save(prog2);
        materiaDao.save(logica);
        materiaDao.save(edatos);

        // Carrera
        Carrera carreraSis = new Carrera("Ingeniería en Sistemas", 100, 1, 10);
        carreraSis.agregarMateria(algebra);
        carreraSis.agregarMateria(analisis);
        carreraSis.agregarMateria(prog1);
        carreraSis.agregarMateria(prog2);
        carreraSis.agregarMateria(logica);
        carreraSis.agregarMateria(edatos);

        carreraDao.saveCarrera(carreraSis);

        // Asignaturas
        Asignatura a1 = new Asignatura(prog1);
        a1.setEstado(EstadoAsignatura.CURSADA);
        a1.setNota(7);

        Asignatura a2 = new Asignatura(analisis);
        a2.setEstado(EstadoAsignatura.APROBADA);
        a2.setNota(9);

        Asignatura a3 = new Asignatura(algebra);
        a3.setEstado(EstadoAsignatura.NO_CURSADA);

        asignaturaDao.save(a1);
        asignaturaDao.save(a2);
        asignaturaDao.save(a3);

        // Alumno
        List<Asignatura> asignaturasAlumno = new ArrayList<>();
        asignaturasAlumno.add(a1);
        asignaturasAlumno.add(a2);
        asignaturasAlumno.add(a3);

        Alumno alumno1 = new Alumno(1L, "Martina", "Paz", 30000111L, asignaturasAlumno, carreraSis);
        alumnoDao.saveAlumno(alumno1);

        System.out.println("\u2705 Datos iniciales cargados correctamente.");
    }
}
