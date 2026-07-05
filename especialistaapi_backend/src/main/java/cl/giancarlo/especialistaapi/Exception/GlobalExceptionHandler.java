package cl.giancarlo.especialistaapi.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Intercepta las excepciones lanzadas desde el service y las traduce
// a una respuesta HTTP real, en vez de dejar que Spring devuelva un 500 genérico.
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EspecialistaNoEncontrado.class)
    public ResponseEntity<String> manejarEspecialistaNoEncontrado(EspecialistaNoEncontrado ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}