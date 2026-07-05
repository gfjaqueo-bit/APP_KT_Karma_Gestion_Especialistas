package cl.giancarlo.especialistaapi.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.giancarlo.especialistaapi.models.entities.Especialista;

@Repository
public interface EspecialistaRepository extends JpaRepository<Especialista, Integer> {
    List<Especialista> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
}
