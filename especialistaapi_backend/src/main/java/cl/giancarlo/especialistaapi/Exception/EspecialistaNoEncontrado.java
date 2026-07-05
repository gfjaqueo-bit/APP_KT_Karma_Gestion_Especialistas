package cl.giancarlo.especialistaapi.Exception;

public class EspecialistaNoEncontrado  extends RuntimeException {
    public EspecialistaNoEncontrado(int id) {
        super("No se encontró un especialista con id " + id);
    }
}

